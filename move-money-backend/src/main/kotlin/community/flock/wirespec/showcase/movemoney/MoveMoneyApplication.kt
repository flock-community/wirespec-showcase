package community.flock.wirespec.showcase.movemoney

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class MoveMoneyApplication

fun main(args: Array<String>) {
	runApplication<MoveMoneyApplication>(*args)
}
