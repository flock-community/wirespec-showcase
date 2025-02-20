package community.flock.wirespec.showcase.movemoney

import community.flock.wirespec.integration.spring.kotlin.configuration.EnableWirespec
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@ConfigurationPropertiesScan
@EnableKafka
@EnableWirespec
@SpringBootApplication
class MoveMoneyApplication

fun main(args: Array<String>) {
    runApplication<MoveMoneyApplication>(*args)
}
