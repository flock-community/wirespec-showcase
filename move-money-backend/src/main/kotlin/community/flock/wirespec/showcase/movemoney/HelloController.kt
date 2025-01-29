package community.flock.wirespec.showcase.movemoney

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/move-money")
class HelloController {

    @GetMapping("/hello")
    fun x(): String {
        return "Hello from move money"
    }
}
