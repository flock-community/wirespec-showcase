package community.flock.wirespec.showcase.payments

import java.math.BigDecimal
import java.time.Instant

data class Payment(
    val id: String,
    val amount: BigDecimal,
    val currency: PaymentCurrency,
    val recipient: AccountDetails,
    val sender: AccountDetails,
    val createdTimestamp: Instant,
    val completedTimestamp: Instant?,
)

data class CreatePaymentRequest(
    val amount: BigDecimal,
    val currency: PaymentCurrency,
    val recipient: AccountDetails,
    val sender: AccountDetails,
)

data class AccountDetails(
    val accountNumber: String,
    val accountName: String,
)

enum class PaymentCurrency { EUR, USD }
