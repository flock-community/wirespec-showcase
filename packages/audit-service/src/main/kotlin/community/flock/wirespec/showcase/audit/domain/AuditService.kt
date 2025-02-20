package community.flock.wirespec.showcase.audit.domain

import community.flock.wirespec.showcase.audit.AuditAdapter
import org.springframework.stereotype.Service

@Service
internal class AuditService(
    private val auditAdapter: AuditAdapter,
) {
    fun getAuditEvents(): List<AuditEvent> = auditAdapter.getAuditEvents()

    fun saveAuditEvent(auditEvent: AuditEvent): Unit = auditAdapter.addAuditEvent(auditEvent)
}
