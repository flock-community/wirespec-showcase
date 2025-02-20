package community.flock.wirespec.showcase.movemoney

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
internal class Configuration {
    @Bean("wirespecSpringWebClient")
    fun webclient(paymentsProperties: PaymentsProperties) = WebClient.create(paymentsProperties.baseUrl)
}

@ConfigurationProperties(prefix = "payments-service")
data class PaymentsProperties(
    val baseUrl: String,
)
