package community.flock.wirespec.showcase.audit

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
internal interface AuditRepository : CrudRepository<AuditEventEntity, Long>

@Entity
data class AuditEventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(nullable = false)
    val timestamp: Instant,
    @Column(nullable = false)
    val eventType: String,
    @Column(nullable = false)
    val details: String,
    @Column(nullable = false)
    val externalIpAddress: String,
    @Column(nullable = true)
    val username: String?,
)
