package community.flock.wirespec.showcase.movemoney

import community.flock.wirespec.showcase.movemoney.api.GetMoveMoneyTransactionByIdEndpoint
import community.flock.wirespec.showcase.movemoney.api.GetMoveMoneyTransactionsEndpoint
import community.flock.wirespec.showcase.movemoney.api.ISO8601DateTime
import community.flock.wirespec.showcase.movemoney.api.MoveMoneyError
import community.flock.wirespec.showcase.movemoney.api.SendMoneyEndpoint
import community.flock.wirespec.showcase.movemoney.audit.AuditEvent
import community.flock.wirespec.showcase.movemoney.audit.AuditEventType
import community.flock.wirespec.showcase.movemoney.audit.AuditService
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.Instant
import community.flock.wirespec.showcase.movemoney.api.Transaction as TransactionDto
import community.flock.wirespec.showcase.movemoney.api.TransactionInput as TransactionInputDto

private const val INTERNAL_SERVER_ERROR = "Internal Server Error"
private const val BAD_REQUEST = "Bad Request"

@RestController
internal class TransactionController(
    private val service: TransactionService,
    private val auditService: AuditService,
) : GetMoveMoneyTransactionByIdEndpoint.Handler,
    SendMoneyEndpoint.Handler,
    GetMoveMoneyTransactionsEndpoint.Handler {
    override suspend fun getMoveMoneyTransactions(
        request: GetMoveMoneyTransactionsEndpoint.Request,
    ): GetMoveMoneyTransactionsEndpoint.Response<*> {
        var user: User? = null
        try {
            user = getUser(request.headers.token)
            check(request.queries.offset < Int.MAX_VALUE) { "Offset exceeds maximum limit." }
            check(request.queries.limit < Int.MAX_VALUE) { "Limit exceeds maximum limit." }
            check(request.queries.offset >= 0) { "Offset cannot be negative." }
            check(request.queries.limit >= 0) { "Limit cannot be negative." }
        } catch (e: IllegalStateException) {
            return GetMoveMoneyTransactionsEndpoint
                .Response400(
                    MoveMoneyError(
                        type = "MoveMoneyError",
                        title = BAD_REQUEST,
                        status = 400,
                        detail = e.message ?: "no detail",
                        instance = "move-money-api",
                    ),
                ).also {
                    // audit non-success
                    auditService.audit(
                        AuditEvent(
                            timestamp = Instant.now(),
                            eventType = AuditEventType.LIST_PAYMENTS,
                            details = "Get all money transactions for user failed: " + (e.message ?: "no detail"),
                            externalIpAddress = request.headers.xforwardedfor,
                            user = user,
                        ),
                    )
                }
        }

        return try {
            service
                .getTransactions(
                    userId = user.id,
                    limit = request.queries.limit.toInt(),
                    offset = request.queries.offset.toInt(),
                ).produce()
                .also {
                    auditService.audit(
                        AuditEvent(
                            timestamp = Instant.now(),
                            eventType = AuditEventType.LIST_PAYMENTS,
                            details = "Get all money transactions for user was successful",
                            externalIpAddress = request.headers.xforwardedfor,
                            user = user,
                        ),
                    )
                }
        } catch (e: Exception) {
            GetMoveMoneyTransactionsEndpoint
                .Response500(
                    MoveMoneyError(
                        type = "MoveMoneyError",
                        title = INTERNAL_SERVER_ERROR,
                        status = 500,
                        detail = e.message ?: "no detail",
                        instance = "move-money-api",
                    ),
                ).also {
                    auditService.audit(
                        AuditEvent(
                            timestamp = Instant.now(),
                            eventType = AuditEventType.LIST_PAYMENTS,
                            details = "Get all money transactions for user failed: ${e.message}",
                            externalIpAddress = request.headers.xforwardedfor,
                            user = user,
                        ),
                    )
                }
        }
    }

    override suspend fun getMoveMoneyTransactionById(
        request: GetMoveMoneyTransactionByIdEndpoint.Request,
    ): GetMoveMoneyTransactionByIdEndpoint.Response<*> {
        val user: User
        try {
            user = getUser(request.headers.token)
        } catch (e: IllegalStateException) {
            return GetMoveMoneyTransactionByIdEndpoint
                .Response400(
                    MoveMoneyError(
                        type = "MoveMoneyError",
                        title = BAD_REQUEST,
                        status = 400,
                        detail = e.message ?: "no detail",
                        instance = "move-money-api",
                    ),
                ).also {
                    auditService.audit(
                        AuditEvent(
                            timestamp = Instant.now(),
                            eventType = AuditEventType.GET_PAYMENT_BY_ID,
                            details = "Get transactions with id ${request.path.id} failed: ${e.message}",
                            externalIpAddress = request.headers.xforwardedfor,
                            user = null,
                        ),
                    )
                }
        }

        return try {
            service
                .getTransaction(
                    transactionId = request.path.id,
                    userId = user.id,
                ).produce()
                .also {
                    auditService.audit(
                        AuditEvent(
                            timestamp = Instant.now(),
                            eventType = AuditEventType.GET_PAYMENT_BY_ID,
                            details = "Get transactions with id ${request.path.id} was successful",
                            externalIpAddress = request.headers.xforwardedfor,
                            user = user,
                        ),
                    )
                }
        } catch (e: Exception) {
            GetMoveMoneyTransactionByIdEndpoint
                .Response500(
                    MoveMoneyError(
                        type = "MoveMoneyError",
                        title = INTERNAL_SERVER_ERROR,
                        status = 500,
                        detail = e.message ?: "no detail",
                        instance = "move-money-api",
                    ),
                ).also {
                    auditService.audit(
                        AuditEvent(
                            timestamp = Instant.now(),
                            eventType = AuditEventType.GET_PAYMENT_BY_ID,
                            details = "Get transactions with id ${request.path.id} failed: ${e.message}",
                            externalIpAddress = request.headers.xforwardedfor,
                            user = user,
                        ),
                    )
                }
        }
    }

    override suspend fun sendMoney(request: SendMoneyEndpoint.Request): SendMoneyEndpoint.Response<*> {
        val user: User
        val createTransactionRequest: CreateTransactionRequest

        try {
            user = getUser(request.headers.token)
            createTransactionRequest = request.body.consume()
        } catch (e: IllegalStateException) {
            return SendMoneyEndpoint
                .Response400(
                    MoveMoneyError(
                        type = "MoveMoneyError",
                        title = BAD_REQUEST,
                        status = 400,
                        detail = e.message ?: "no detail",
                        instance = "move-money-api",
                    ),
                ).also {
                    auditService.audit(
                        AuditEvent(
                            timestamp = Instant.now(),
                            eventType = AuditEventType.CREATE_PAYMENT,
                            details = "Creating a transaction failed: ${e.message}",
                            externalIpAddress = request.headers.xforwardedfor,
                            user = null,
                        ),
                    )
                }
        }

        return try {
            service
                .createTransaction(
                    userId = user.id,
                    createTransactionRequest = createTransactionRequest,
                ).let { SendMoneyEndpoint.Response201(body = it.produceDto()) }
                .also {
                    auditService.audit(
                        AuditEvent(
                            timestamp = Instant.now(),
                            eventType = AuditEventType.CREATE_PAYMENT,
                            details =
                                "Created a transaction successfully from ${user.id}'s account to  " +
                                    createTransactionRequest.recipientAccountNumber,
                            externalIpAddress = request.headers.xforwardedfor,
                            user = user,
                        ),
                    )
                }
        } catch (e: Exception) {
            SendMoneyEndpoint
                .Response500(
                    MoveMoneyError(
                        type = "MoveMoneyError",
                        title = INTERNAL_SERVER_ERROR,
                        status = 500,
                        detail = e.message ?: "no detail",
                        instance = "move-money-api",
                    ),
                ).also {
                    AuditEvent(
                        timestamp = Instant.now(),
                        eventType = AuditEventType.CREATE_PAYMENT,
                        details =
                            "Creating a transaction from ${user.id}'s account to " +
                                "${createTransactionRequest.recipientAccountNumber} failed: ${e.message}",
                        externalIpAddress = request.headers.xforwardedfor,
                        user = user,
                    )
                }
        }
    }
}

private fun TransactionInputDto.consume(): CreateTransactionRequest =
    CreateTransactionRequest(
        amount = BigDecimal(amount),
        currency = currency.consumeCurrency(),
        recipientAccountNumber = recipientAccountNumber,
        recipientAccountName = recipientAccountName,
    )

private fun String.consumeCurrency(): Currency {
    check(this in Currency.entries.map(Currency::toString)) {
        "The only accepted currencies are: ${Currency.entries.joinToString(",")}"
    }
    return when (this) {
        "EUR" -> Currency.EUR
        "USD" -> Currency.USD
        else -> error("Unknown currency: $this")
    }
}

private fun List<Transaction>.produce(): GetMoveMoneyTransactionsEndpoint.Response<*> =
    GetMoveMoneyTransactionsEndpoint.Response200(body = map(Transaction::produceDto))

private fun Transaction.produce(): GetMoveMoneyTransactionByIdEndpoint.Response<*> =
    GetMoveMoneyTransactionByIdEndpoint.Response200(body = this.produceDto())

private fun Transaction.produceDto() =
    TransactionDto(
        id = transactionId,
        transactionCreatedTimestamp = ISO8601DateTime(timing.created.toString()),
        transactionCompletedTimestamp = timing.completed?.let { ISO8601DateTime(it.toString()) },
        senderAccountNumber = sender.accountNumber,
        senderAccountName = sender.accountName,
        recipientAccountNumber = recipient.accountNumber,
        recipientAccountName = recipient.accountName,
        amount = amount.toDouble(),
        currency = currency.toString(),
    )
