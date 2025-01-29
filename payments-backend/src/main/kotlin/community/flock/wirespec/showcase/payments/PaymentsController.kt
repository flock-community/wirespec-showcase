package community.flock.wirespec.showcase.payments

import community.flock.wirespec.showcase.payments.api.GetPaymentByIdEndpoint
import community.flock.wirespec.showcase.payments.api.GetPaymentsEndpoint
import community.flock.wirespec.showcase.payments.api.PostPaymentEndpoint
import org.springframework.web.bind.annotation.RestController

@RestController
internal class PaymentsController(
    private val service: PaymentsService
) : GetPaymentsEndpoint.Handler, PostPaymentEndpoint.Handler, GetPaymentByIdEndpoint.Handler {


    override suspend fun getPayments(request: GetPaymentsEndpoint.Request): GetPaymentsEndpoint.Response<*> {
        TODO("Not yet implemented")
    }

    override suspend fun postPayment(request: PostPaymentEndpoint.Request): PostPaymentEndpoint.Response<*> {
        TODO("Not yet implemented")
    }

    override suspend fun getPaymentById(request: GetPaymentByIdEndpoint.Request): GetPaymentByIdEndpoint.Response<*> {
        TODO("Not yet implemented")
    }
}
