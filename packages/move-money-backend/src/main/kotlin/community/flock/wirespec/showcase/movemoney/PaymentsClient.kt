package community.flock.wirespec.showcase.movemoney

import community.flock.wirespec.integration.spring.kotlin.client.WirespecWebClient
import community.flock.wirespec.showcase.movemoney.payments.api.GetPaymentByIdEndpoint
import community.flock.wirespec.showcase.movemoney.payments.api.GetPaymentsEndpoint
import community.flock.wirespec.showcase.movemoney.payments.api.PostPaymentEndpoint
import org.springframework.stereotype.Component

/**
 * PaymentsClient is a component responsible for interacting with payment-related
 * endpoints through a WirespecWebClient. This client facilitates communication
 * with external APIs for payment operations such as retrieving payments, fetching
 * a specific payment by ID, and posting a new payment.
 *
 * @property proxy A WirespecWebClient instance configured for payment-specific interactions.
 */
@Component
internal class PaymentsClient(
    private val proxy: WirespecWebClient,
) {
    suspend fun getPayments(req: GetPaymentsEndpoint.Request): GetPaymentsEndpoint.Response<*> = proxy.send(req)

    suspend fun getPayment(req: GetPaymentByIdEndpoint.Request): GetPaymentByIdEndpoint.Response<*> = proxy.send(req)

    suspend fun postPayment(req: PostPaymentEndpoint.Request): PostPaymentEndpoint.Response<*> = proxy.send(req)
}
