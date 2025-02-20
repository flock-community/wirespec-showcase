package community.flock.wirespec.showcase.payments

import org.springframework.stereotype.Service

@Service
internal class PaymentsService(
    private val adapter: PaymentsAdapter,
) {
    fun getAllPayments(): List<Payment> = adapter.getAllPayments()

    fun getPaymentById(id: String): Payment? = adapter.getPaymentById(id)

    fun createPayment(payment: CreatePaymentRequest): Payment = adapter.createPayment(payment)
}
