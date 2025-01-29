package community.flock.wirespec.showcase.movemoney

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import community.flock.wirespec.integration.spring.kotlin.client.WirespecWebClient
import community.flock.wirespec.kotlin.Wirespec
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.web.reactive.function.client.WebClient
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType


@ConfigurationProperties(prefix = "payments")
data class PaymentsProperties(
    val baseUrl: String
)


@Configuration
internal class Configuration {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
        .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)

    @Bean
    fun wirespecSerialization(objectMapper: ObjectMapper) = object : Wirespec.Serialization<String> {
        override fun <T> deserialize(raw: String, kType: KType): T =
            objectMapper.readValue(raw, objectMapper.constructType(kType.javaType))

        override fun <T> deserializeParam(values: List<String>, kType: KType): T =
            objectMapper.convertValue(values, objectMapper.constructType(kType.javaType))

        override fun <T> serialize(t: T, kType: KType): String = objectMapper.writeValueAsString(t)

        override fun <T> serializeParam(value: T, kType: KType): List<String> =
            objectMapper.convertValue(value, objectMapper.constructType(kType.javaType))

    }

    @Bean
    @Qualifier("paymentsClient")
    fun wirespecPaymentsClient(
        paymentsProperties: PaymentsProperties,
        wirespecSerialization: Wirespec.Serialization<String>
    ): WirespecWebClient =
        WirespecWebClient(
            client = WebClient.create(paymentsProperties.baseUrl),
            wirespecSerde = wirespecSerialization
        )
}
