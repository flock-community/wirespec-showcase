package community.flock.wirespec.showcase.payments

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payments")
class HelloController {

    @GetMapping("/hello")
    fun x(): String {
        return "Hello from payments"
    }
}
