package community.flock.wirespec.showcase.audit

import community.flock.wirespec.integration.spring.kotlin.configuration.EnableWirespecController
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory

@EnableWirespecController
@EnableKafka
@Configuration
class KafkaConsumerConfig {
    @Bean
    fun consumerFactory(kafkaProperties: KafkaProperties): ConsumerFactory<String, Any> {
        val props: MutableMap<String, Any> = kafkaProperties.properties.toMutableMap()

        props += ConsumerConfig.GROUP_ID_CONFIG to "audit-service"
        props += ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers
        props += ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
        props += ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to KafkaAvroDeserializer::class.java
        return DefaultKafkaConsumerFactory(props.toMap())
    }

    @Bean
    fun kafkaGenericListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, Any>,
    ): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = consumerFactory
        return factory
    }
}

@Configuration
class AvroConfiguration(
    private val kafkaProperties: KafkaProperties,
) {
    @Bean
    fun kafkaProducerFactory(): ProducerFactory<Any, Any> {
        val props: MutableMap<String, Any> = kafkaProperties.properties.toMutableMap()

        props += ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers
        props += ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
        props += ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaAvroSerializer::class.java
        return DefaultKafkaProducerFactory(props.toMap())
    }

    @Bean
    fun kafkaConsumerFactory(): ConsumerFactory<Any, Any> {
        val props: MutableMap<String, Any> = kafkaProperties.properties.toMutableMap()

        props += ConsumerConfig.GROUP_ID_CONFIG to "group"
        props += ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers
        props += ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
        props += ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to KafkaAvroDeserializer::class.java
        return DefaultKafkaConsumerFactory(props.toMap())
    }
}
