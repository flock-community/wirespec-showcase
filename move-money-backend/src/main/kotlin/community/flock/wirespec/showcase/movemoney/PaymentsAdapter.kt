package community.flock.wirespec.showcase.movemoney

import community.flock.wirespec.showcase.movemoney.api.GetPaymentByIdEndpoint
import community.flock.wirespec.showcase.movemoney.api.GetPaymentsEndpoint
import community.flock.wirespec.showcase.movemoney.api.MonetaryValue
import community.flock.wirespec.showcase.movemoney.api.Payment
import community.flock.wirespec.showcase.movemoney.api.PaymentInput
import community.flock.wirespec.showcase.movemoney.api.PostPaymentEndpoint
import community.flock.wirespec.showcase.movemoney.api.validate
import org.springframework.stereotype.Repository
import java.time.Instant
import community.flock.wirespec.showcase.movemoney.api.Currency as CurrencyDto

@Repository
internal class PaymentsAdapter(
    private val paymentsClient: PaymentsClient
) {
    suspend fun getTransactions(userAccountInfo: AccountInfo, limit: Int, offset: Int): List<Transaction> =
        paymentsClient.getPayments(
            GetPaymentsEndpoint.Request(
                limit = limit.toLong(),
                offset = offset.toLong(),
                accountNumber = userAccountInfo.accountNumber,
            )
        ).let { res ->
            when (res) {
                is GetPaymentsEndpoint.ResponseListPayment -> res.body.internalize()
                is GetPaymentsEndpoint.Response400 -> throw RuntimeException("Something went wrong getting payments for ${userAccountInfo.accountNumber}. Details: ${res.body}")
                is GetPaymentsEndpoint.Response500 -> throw RuntimeException("Something went wrong getting payments for ${userAccountInfo.accountNumber}. Details: ${res.body}")
            }
        }

    suspend fun getTransactionById(transactionId: String, user: User): Transaction =
        paymentsClient.getPayment(
            GetPaymentByIdEndpoint.Request(transactionId)
        ).let { res ->
            when (res) {
                is GetPaymentByIdEndpoint.ResponsePayment -> res.body.internalize()
                is GetPaymentByIdEndpoint.Response400 -> throw RuntimeException("Something went wrong getting payment with id $transactionId for userId ${user.id}. Details: ${res.body}")
                is GetPaymentByIdEndpoint.Response500 -> throw RuntimeException("Something went wrong getting payment with id $transactionId for userId ${user.id}. Details: ${res.body}")
                is GetPaymentByIdEndpoint.Response404 -> throw ResourceNotFoundException("Transaction with id $transactionId was not found. Details: ${res.body}")
            }
        }

    suspend fun createTransaction(
        userAccountInfo: AccountInfo, createTransactionRequest: CreateTransactionRequest
    ): Transaction {
        val paymentInput = PaymentInput(
            senderAccountNumber = userAccountInfo.accountNumber,
            senderAccountName = userAccountInfo.accountName,
            recipientAccountNumber = createTransactionRequest.recipientAccountNumber,
            recipientAccountName = createTransactionRequest.recipientAccountName,
            amount = MonetaryValue(createTransactionRequest.amount.toPlainString()),
            currency = CurrencyDto(createTransactionRequest.currency.name)
        )
        return paymentsClient.postPayment(
            PostPaymentEndpoint.Request(
                body = paymentInput,
            )
        ).let { res ->
            when (res) {
                is PostPaymentEndpoint.ResponsePayment -> res.body.internalize()
                is PostPaymentEndpoint.Response400 -> throw RuntimeException("Something went wrong creating a payments from ${paymentInput.senderAccountNumber} to ${paymentInput.recipientAccountNumber}. Details: ${res.body}")
                is PostPaymentEndpoint.Response500 -> throw RuntimeException("Something went wrong creating a payments from ${paymentInput.senderAccountNumber} to ${paymentInput.recipientAccountNumber}. Details: ${res.body}")
            }

        }

    }
}

private fun List<Payment>.internalize(): List<Transaction> = map(Payment::internalize)

private fun Payment.internalize(): Transaction = Transaction(
    transactionId = id,
    timing = Transaction.Timing(
        Instant.ofEpochMilli(transactionCreatedTimestamp), transactionCompletedTimestamp?.let(Instant::ofEpochMilli)
    ),
    sender = AccountInfo(senderAccountNumber, senderAccountName),
    recipient = AccountInfo(recipientAccountNumber, recipientAccountName),
    amount = amount.also { it.validate() }.value.toBigDecimal(),
    currency = currency.internalize()
)

private fun CurrencyDto.internalize(): Currency = also {
    if (!it.validate()) {
        error("Currency not according to contract")
    }
}.let {
    when (it.value) {
        "EUR" -> Currency.EUR
        "USD" -> Currency.USD
        else -> throw IllegalArgumentException("Unknown currency: $this")

    }

}

