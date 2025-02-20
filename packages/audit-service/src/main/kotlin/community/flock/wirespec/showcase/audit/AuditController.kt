package community.flock.wirespec.showcase.audit

import community.flock.wirespec.showcase.audit.api.AuditEvents
import community.flock.wirespec.showcase.audit.api.GetAllAuditEventsEndpoint
import community.flock.wirespec.showcase.audit.domain.AuditEvent
import community.flock.wirespec.showcase.audit.domain.AuditService
import org.springframework.web.bind.annotation.RestController
import community.flock.wirespec.showcase.audit.api.AuditEvent as AuditEventDto

@RestController
internal class AuditController(
    private val auditService: AuditService,
) : GetAllAuditEventsEndpoint.Handler {
    override suspend fun getAllAuditEvents(request: GetAllAuditEventsEndpoint.Request): GetAllAuditEventsEndpoint.Response<*> =
        auditService
            .getAuditEvents()
            .let { GetAllAuditEventsEndpoint.Response200(AuditEvents(it.map(AuditEvent::produce))) }
}

private fun AuditEvent.produce(): AuditEventDto =
    AuditEventDto(
        timestamp = timestamp.toEpochMilli(),
        eventType = eventType,
        details = details,
        externalIpAddress = externalIpAddress,
        username = username,
    )
