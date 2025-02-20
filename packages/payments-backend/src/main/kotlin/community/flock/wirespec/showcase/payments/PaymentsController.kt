package community.flock.wirespec.showcase.payments

import community.flock.wirespec.showcase.payments.api.Currency
import community.flock.wirespec.showcase.payments.api.GetPaymentByIdEndpoint
import community.flock.wirespec.showcase.payments.api.GetPaymentsEndpoint
import community.flock.wirespec.showcase.payments.api.MonetaryValue
import community.flock.wirespec.showcase.payments.api.PaymentsApiError
import community.flock.wirespec.showcase.payments.api.PostPaymentEndpoint
import community.flock.wirespec.showcase.payments.api.validate
import org.springframework.web.bind.annotation.RestController
import community.flock.wirespec.showcase.payments.api.Payment as PaymentApi

@RestController
internal class PaymentsController(
    private val service: PaymentsService,
) : GetPaymentsEndpoint.Handler,
    PostPaymentEndpoint.Handler,
    GetPaymentByIdEndpoint.Handler {
    override suspend fun getPayments(request: GetPaymentsEndpoint.Request): GetPaymentsEndpoint.Response<*> =
        service.getAllPayments().let {
            GetPaymentsEndpoint.Response200(
                it.map(Payment::produce),
            )
        }

    override suspend fun postPayment(request: PostPaymentEndpoint.Request): PostPaymentEndpoint.Response<*> =
        service
            .createPayment(request.consume())
            .let { PostPaymentEndpoint.Response201(it.produce()) }

    override suspend fun getPaymentById(request: GetPaymentByIdEndpoint.Request): GetPaymentByIdEndpoint.Response<*> =
        service
            .getPaymentById(request.path.id)
            .toResponse(request.path.id)
}

private fun PostPaymentEndpoint.Request.consume() =
    CreatePaymentRequest(
        amount = body.amount.value.toBigDecimal(),
        currency = body.currency.consume(),
        recipient =
            AccountDetails(
                accountNumber = body.recipientAccountNumber,
                accountName = body.recipientAccountName,
            ),
        sender =
            AccountDetails(
                accountNumber = body.senderAccountNumber,
                accountName = body.senderAccountName,
            ),
    )

private fun Currency.consume(): PaymentCurrency {
    if (!validate()) throw InvalidCurrencyError("Invalid currency: $value")
    return PaymentCurrency.valueOf(value)
}

class InvalidCurrencyError(
    override val message: String?,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)

private fun Payment?.toResponse(id: String): GetPaymentByIdEndpoint.Response<*> =
    when (this) {
        null ->
            GetPaymentByIdEndpoint.Response404(
                PaymentsApiError(
                    type = "PAYMENT_NOT_FOUND",
                    title = "Payment with id $id not found",
                    status = 404,
                    detail = "Some detail about how the payment could not be found in any of the systems",
                    instance = "",
                ),
            )

        else -> GetPaymentByIdEndpoint.Response200(produce())
    }

private fun Payment.produce() =
    PaymentApi(
        id = id,
        transactionCreatedTimestamp = createdTimestamp.toEpochMilli(),
        transactionCompletedTimestamp = completedTimestamp?.toEpochMilli(),
        senderAccountNumber = sender.accountNumber,
        senderAccountName = sender.accountName,
        recipientAccountNumber = recipient.accountNumber,
        recipientAccountName = recipient.accountName,
        amount = MonetaryValue(amount.toPlainString()),
        currency = Currency(currency.toString()),
    )
