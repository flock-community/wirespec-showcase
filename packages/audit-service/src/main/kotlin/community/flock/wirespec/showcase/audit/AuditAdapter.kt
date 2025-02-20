package community.flock.wirespec.showcase.audit

import community.flock.wirespec.showcase.audit.domain.AuditEvent
import org.springframework.stereotype.Service

@Service
internal class AuditAdapter(
    private val auditRepository: AuditRepository,
) {
    fun addAuditEvent(auditEvent: AuditEvent) {
        auditRepository.save(auditEvent.externalize())
    }

    fun getAuditEvents() = auditRepository.findAll().map(AuditEventEntity::internalize)
}

private fun AuditEvent.externalize(): AuditEventEntity =
    AuditEventEntity(
        id = 0L,
        timestamp = timestamp,
        eventType = eventType,
        details = details,
        externalIpAddress = externalIpAddress,
        username = username,
    )

private fun AuditEventEntity.internalize(): AuditEvent =
    AuditEvent(
        timestamp = timestamp,
        eventType = eventType,
        details = details,
        externalIpAddress = externalIpAddress,
        username = username,
    )
