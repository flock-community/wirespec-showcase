package community.flock.wirespec.showcase.audit.domain

import java.time.Instant

data class AuditEvent(
    val timestamp: Instant,
    val eventType: String,
    val details: String,
    val externalIpAddress: String,
    val username: String?,
)
