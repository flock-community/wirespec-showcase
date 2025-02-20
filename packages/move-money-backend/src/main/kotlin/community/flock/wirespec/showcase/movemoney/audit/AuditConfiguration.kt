package community.flock.wirespec.showcase.movemoney.audit

import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory

@Configuration
class AuditConfiguration {
    @Bean
    fun kafkaProducerFactory(kafkaProperties: KafkaProperties): ProducerFactory<Any, Any> {
        val props: MutableMap<String, Any> = kafkaProperties.properties.toMutableMap()

        props += ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers
        props += ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
        props += ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaAvroSerializer::class.java
        return DefaultKafkaProducerFactory(props.toMap())
    }
}
