package community.flock.wirespec.showcase.audit

import community.flock.wirespec.showcase.audit.domain.AuditEvent
import community.flock.wirespec.showcase.audit.domain.AuditService
import community.flock.wirespec.showcase.audit.events.AuditEventRecordChannel
import org.apache.avro.generic.GenericData
import org.slf4j.LoggerFactory.getLogger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.Instant
import community.flock.wirespec.showcase.audit.events.AuditEventRecord as AuditEventDto

/**
 * Something with Wirespec magic
 *
 *
 */
@Component
internal class AuditMessageListener(
    private val handler: AuditEventRecordChannel,
) {
    @KafkaListener(
        topics = ["audit-messages"],
        containerFactory = "kafkaGenericListenerContainerFactory",
        groupId = "audit-service",
    )
    fun listen(message: GenericData.Record) {
        try {
            println("Received Message in group audit-service: $message")
            val auditEvent = AuditEventDto.Avro.from(message)
            handler.invoke(auditEvent)
        } catch (e: Exception) {
            println(e)
            throw e
        }
    }
}

@Component
internal class AuditKafkaMessageHandler(
    val auditService: AuditService,
) : AuditEventRecordChannel {
    private val log = getLogger(javaClass)

    override fun invoke(message: AuditEventDto) {
        log.info("Handling and saving audit message: $message")
        val auditEvent = message.consume()
        auditService.saveAuditEvent(auditEvent)
    }
}

private fun AuditEventDto.consume(): AuditEvent =
    AuditEvent(
        timestamp = Instant.ofEpochMilli(timestamp),
        eventType = eventType.label,
        details = details,
        externalIpAddress = externalIpAddress,
        username = username,
    )
