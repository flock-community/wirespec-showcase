package community.flock.wirespec.showcase.movemoney.audit

import community.flock.wirespec.showcase.movemoney.audit.api.AuditEventRecord
import org.springframework.stereotype.Component
import community.flock.wirespec.showcase.movemoney.audit.api.AuditEventType as AuditEventTypeDto

@Component
internal class AuditAdapter(
    private val auditKafkaClient: AuditKafkaClient,
) {
    fun send(auditEvent: AuditEvent) {
        val auditEventRecord = auditEvent.produce()
        println("Sending Message in group audit-service: $auditEventRecord")
        auditKafkaClient(auditEventRecord)
    }
}

private fun AuditEvent.produce(): AuditEventRecord =
    AuditEventRecord(
        timestamp = timestamp.toEpochMilli(),
        eventType = eventType.produce(),
        details = details,
        externalIpAddress = externalIpAddress,
        username = user?.username,
    )

private fun AuditEventType.produce(): AuditEventTypeDto =
    when (this) {
        AuditEventType.LOGIN_SUCCESS -> AuditEventTypeDto.LOGIN_SUCCESS
        AuditEventType.LOGIN_FAILED -> AuditEventTypeDto.LOGIN_FAIL
        AuditEventType.LOGOUT_SUCCESS -> AuditEventTypeDto.LOGOUT_SUCCESS
        AuditEventType.LOGOUT_FAILED -> AuditEventTypeDto.LOGOUT_FAIL
        AuditEventType.LIST_PAYMENTS -> AuditEventTypeDto.LIST_PAYMENTS
        AuditEventType.GET_PAYMENT_BY_ID -> AuditEventTypeDto.GET_PAYMENT_BY_ID
        AuditEventType.CREATE_PAYMENT -> AuditEventTypeDto.CREATE_PAYMENT
    }
