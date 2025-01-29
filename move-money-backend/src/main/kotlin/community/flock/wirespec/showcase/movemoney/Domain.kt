package community.flock.wirespec.showcase.movemoney

import java.math.BigDecimal
import java.time.Instant

enum class Currency {
    EUR, USD
}

data class CreateTransactionRequest(
    val amount: BigDecimal,
    val currency: Currency,
    val recipientAccountNumber: String,
    val recipientAccountName: String,
)

data class Transaction(
    val transactionId: String,
    val timing: Timing,
    val sender: AccountInfo,
    val recipient: AccountInfo,
    val amount: BigDecimal,
    val currency: Currency
) {
    data class Timing(
        val created: Instant,
        val completed: Instant?,
    )
}

data class AccountInfo(
    val accountNumber: String,
    val accountName: String,
)

data class ResourceNotFoundException(override val message: String?, override val cause: Throwable? = null) :
    RuntimeException(message, cause)

data class User(val id: Long, val firstName: String, val lastName: String)
data class UserAccountInfo(val accountInfo: AccountInfo, val user: User)
