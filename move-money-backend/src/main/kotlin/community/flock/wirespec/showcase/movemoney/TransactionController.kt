package community.flock.wirespec.showcase.movemoney

import community.flock.wirespec.showcase.movemoney.api.GetMoveMoneyTransactionByIdEndpoint
import community.flock.wirespec.showcase.movemoney.api.GetMoveMoneyTransactionsEndpoint
import community.flock.wirespec.showcase.movemoney.api.ISO8601DateTime
import community.flock.wirespec.showcase.movemoney.api.MoveMoneyError
import community.flock.wirespec.showcase.movemoney.api.SendMoneyEndpoint
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import community.flock.wirespec.showcase.movemoney.api.Transaction as TransactionDto
import community.flock.wirespec.showcase.movemoney.api.TransactionInput as TransactionInputDto

@RestController
internal class TransactionController(private val service: TransactionService) :
    GetMoveMoneyTransactionByIdEndpoint.Handler, SendMoneyEndpoint.Handler,
    GetMoveMoneyTransactionsEndpoint.Handler {

    override suspend fun getMoveMoneyTransactions(request: GetMoveMoneyTransactionsEndpoint.Request): GetMoveMoneyTransactionsEndpoint.Response<*> {
        try {
            isValidRequest(request)
        } catch (e: IllegalStateException) {
            return GetMoveMoneyTransactionsEndpoint.Response400(
                MoveMoneyError(
                    type = "MoveMoneyError",
                    title = "Invalid Request",
                    status = 400,
                    detail = e.message ?: "no detail",
                    instance = "move-money-api"
                )
            )
        }

        val user = getUser(request.headers.token)
        return service.getTransactions(
            userId = user.id,
            limit = request.queries.limit.toInt(),
            offset = request.queries.offset.toInt()
        )
            .produce()
    }

    override suspend fun getMoveMoneyTransactionById(request: GetMoveMoneyTransactionByIdEndpoint.Request): GetMoveMoneyTransactionByIdEndpoint.Response<*> {
        val user = getUser(request.headers.token)
        return service.getTransaction(
            transactionId = request.path.id,
            userId = user.id
        )
            .produce()
    }

    override suspend fun sendMoney(request: SendMoneyEndpoint.Request): SendMoneyEndpoint.Response<*> {
        val createTransactionRequest = kotlin.runCatching { request.body.consume() }
        if (createTransactionRequest.isFailure) {
            return SendMoneyEndpoint.Response400(
                MoveMoneyError(
                    type = "MoveMoneyError",
                    title = "Invalid Request",
                    status = 400,
                    detail = createTransactionRequest.exceptionOrNull()?.message ?: "no detail",
                    instance = "move-money-api"
                )
            )
        }
        val user = getUser(request.headers.token)
        return service.createTransaction(
            userId = user.id,
            createTransactionRequest = createTransactionRequest.getOrThrow()
        ).produce2()
    }

    private fun isValidRequest(request: GetMoveMoneyTransactionsEndpoint.Request) {
        check(request.queries.offset < Int.MAX_VALUE) { "Offset exceeds maximum limit." }
        check(request.queries.limit < Int.MAX_VALUE) { "Limit exceeds maximum limit." }
        check(request.queries.offset >= 0) { "Offset cannot be negative." }
        check(request.queries.limit >= 0) { "Limit cannot be negative." }
    }
}

private fun TransactionInputDto.consume(): CreateTransactionRequest {
    return CreateTransactionRequest(
        amount = BigDecimal(amount),
        currency = currency.consumeCurrency(),
        recipientAccountNumber = recipientAccountNumber,
        recipientAccountName = recipientAccountName
    )
}

private fun String.consumeCurrency(): Currency {
    check(this in Currency.entries.map(Currency::toString)) {
        "The only accepted currencies are: ${Currency.entries.joinToString(",")}"
    }
    return when (this) {
        "EUR" -> Currency.EUR
        "USD" -> Currency.USD
        else -> throw IllegalStateException("Unknown currency: $this")
    }
}

private fun List<Transaction>.produce(): GetMoveMoneyTransactionsEndpoint.Response<*> =
    GetMoveMoneyTransactionsEndpoint.Response200(body = map(Transaction::produceDto))


private fun Transaction.produce2(): SendMoneyEndpoint.Response<*> =
    SendMoneyEndpoint.Response201(body = this.produceDto())


private fun Transaction.produce(): GetMoveMoneyTransactionByIdEndpoint.Response<*> =
    GetMoveMoneyTransactionByIdEndpoint.Response200(body = this.produceDto())


private fun Transaction.produceDto() = TransactionDto(
    id = transactionId,
    transactionCreatedTimestamp = ISO8601DateTime(timing.created.toString()),
    transactionCompletedTimestamp = timing.completed?.let { ISO8601DateTime(it.toString()) },
    senderAccountNumber = sender.accountNumber,
    senderAccountName = sender.accountName,
    recipientAccountNumber = recipient.accountNumber,
    recipientAccountName = recipient.accountName,
    amount = amount.toDouble(),
    currency = currency.toString()
)
