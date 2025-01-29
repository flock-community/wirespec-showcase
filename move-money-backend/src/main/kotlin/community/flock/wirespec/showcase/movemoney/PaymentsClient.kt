package community.flock.wirespec.showcase.movemoney

import community.flock.wirespec.integration.spring.kotlin.client.WirespecWebClient
import community.flock.wirespec.showcase.movemoney.api.GetPaymentByIdEndpoint
import community.flock.wirespec.showcase.movemoney.api.GetPaymentsEndpoint
import community.flock.wirespec.showcase.movemoney.api.PostPaymentEndpoint
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
internal class PaymentsClient(
    @Qualifier("paymentsClient")
    private val wirespecWebClient: WirespecWebClient
) {

    suspend fun getPayments(req: GetPaymentsEndpoint.Request): GetPaymentsEndpoint.Response<*> {
        return wirespecWebClient.send(req)
    }

    suspend fun getPayment(req: GetPaymentByIdEndpoint.Request): GetPaymentByIdEndpoint.Response<*> {
        return wirespecWebClient.send(req)
    }

    suspend fun postPayment(req: PostPaymentEndpoint.Request): PostPaymentEndpoint.Response<*> {
        return wirespecWebClient.send(req)
    }


}
