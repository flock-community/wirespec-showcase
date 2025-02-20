package community.flock.wirespec.showcase.payments

import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

/**
 * PaymentsAdapter is a service layer class responsible for interacting with the PaymentsRepository.
 * It provides methods for retrieving, creating, and transforming payment-related data between domain
 * and persistence models.
 *
 * @property repository The PaymentsRepository used for CRUD operations on the payment entity.
 */
@Service
internal class PaymentsAdapter(
    private val repository: PaymentsRepository,
) {
    fun getAllPayments(): List<Payment> = repository.findAll().map(PaymentEntity::internalize)

    fun getPaymentById(id: String): Payment? = repository.findByPublicId(id)?.internalize()

    fun createPayment(payment: CreatePaymentRequest): Payment = repository.save(payment.externalize()).internalize()
}

private fun CreatePaymentRequest.externalize() =
    PaymentEntity(
        id = 0L,
        publicId = UUID.randomUUID().toString(),
        amount = amount,
        createdTimestamp = Instant.now(),
        completedTimestamp = null,
        senderAccountNumber = sender.accountNumber,
        senderAccountName = sender.accountName,
        recipientAccountNumber = recipient.accountNumber,
        recipientAccountName = recipient.accountName,
        currency =
            when (currency) {
                PaymentCurrency.EUR -> Currency.EUR
                PaymentCurrency.USD -> Currency.USD
            },
    )

private fun PaymentEntity.internalize() =
    Payment(
        id = publicId,
        amount = amount,
        currency =
            when (currency) {
                Currency.EUR -> PaymentCurrency.EUR
                Currency.USD -> PaymentCurrency.USD
            },
        createdTimestamp = createdTimestamp,
        completedTimestamp = completedTimestamp,
        recipient =
            AccountDetails(
                accountNumber = recipientAccountNumber,
                accountName = recipientAccountName,
            ),
        sender =
            AccountDetails(
                accountNumber = senderAccountName,
                accountName = senderAccountNumber,
            ),
    )
