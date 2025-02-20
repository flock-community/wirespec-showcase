package community.flock.wirespec.showcase.movemoney.audit

import community.flock.wirespec.showcase.movemoney.audit.api.AuditEventRecord
import community.flock.wirespec.showcase.movemoney.audit.api.AuditEventRecordChannel
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.stereotype.Component

/**
 * This class handles the integration with Wirespec for sending audit event records.
 *
 * It utilizes:
 * - `AuditEventRecord` for representing the audit events in a structured format.
 * - `AuditEventRecordChannel` as the functional interface for processing audit event messages.
 */
@Component
internal class AuditKafkaClient(
    kafkaProducerFactory: ProducerFactory<Any, Any>,
) : AuditEventRecordChannel {
    private val template = KafkaTemplate(kafkaProducerFactory)

    override fun invoke(message: AuditEventRecord) {
        val avro = AuditEventRecord.Avro.to(message)
        template.send("audit-messages", avro)
    }
}
