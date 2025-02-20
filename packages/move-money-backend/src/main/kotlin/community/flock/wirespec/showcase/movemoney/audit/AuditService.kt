package community.flock.wirespec.showcase.movemoney.audit

import community.flock.wirespec.showcase.movemoney.User
import org.springframework.stereotype.Service
import java.time.Instant

@Service
internal class AuditService(
    private val auditAdapter: AuditAdapter,
) {
    fun audit(auditEvent: AuditEvent) = auditAdapter.send(auditEvent)
}

internal data class AuditEvent(
    val eventType: AuditEventType,
    val timestamp: Instant,
    val externalIpAddress: String,
    val details: String,
    val user: User?,
)

internal enum class AuditEventType {
    CREATE_PAYMENT,
    GET_PAYMENT_BY_ID,
    LIST_PAYMENTS,
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    LOGOUT_SUCCESS,
    LOGOUT_FAILED,
}
