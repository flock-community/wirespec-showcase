package community.flock.wirespec.showcase.payments

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.Instant

/**
 * PaymentsRepository is a persistence layer interface used to perform CRUD operations
 * on payment entities. It extends the CrudRepository to leverage Spring Data JPA capabilities.
 *
 * This repository allows querying PaymentEntity by its unique public identifier.
 */
@Repository
internal interface PaymentsRepository : CrudRepository<PaymentEntity, Long> {
    fun findByPublicId(publicId: String): PaymentEntity?
}

/**
 * Represents a payment entity stored in the database.
 *
 * This class is a JPA entity mapped to the "payments" table. It encapsulates the necessary
 * data related to a payment transaction, including timestamps, account details for both sender
 * and recipient, transaction amount, and currency type.
 *
 * Each PaymentEntity has a unique public identifier (`publicId`) and supports persistence
 * via Spring Data JPA.
 */
@Entity
@Table(name = "payments")
data class PaymentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(unique = true, nullable = false)
    val publicId: String,
    @Column(nullable = false)
    val createdTimestamp: Instant,
    @Column(nullable = true)
    val completedTimestamp: Instant?,
    @Column(nullable = false)
    val senderAccountNumber: String,
    @Column(nullable = false)
    val senderAccountName: String,
    @Column(nullable = false)
    val recipientAccountNumber: String,
    @Column(nullable = false)
    val recipientAccountName: String,
    @Column(nullable = false)
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val currency: Currency,
)

enum class Currency { EUR, USD }
