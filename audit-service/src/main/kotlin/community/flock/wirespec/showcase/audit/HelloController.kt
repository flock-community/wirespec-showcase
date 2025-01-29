package community.flock.wirespec.showcase.audit

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/audit-service")
class HelloController {

    @GetMapping("/hello")
    fun x(): String {
        return "Hello from audit service"
    }
}
