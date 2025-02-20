
/**
  * Following the RFC-7807 standard for problem details
  * https://www.rfc-editor.org/rfc/rfc7807
  */
type PaymentsApiError {
    `type`: String,
    title: String,
    status: Integer,
    detail: String,
    instance: String
}

type Currency /^EUR|USD$/
type Payment {
    id: String,
    transactionCreatedTimestamp: Integer,
    transactionCompletedTimestamp: Integer?,
    senderAccountNumber: String,
    senderAccountName: String,
    recipientAccountNumber: String,
    recipientAccountName: String,
    amount: MonetaryValue,
    currency: Currency
}

/**
 * Monetary value is any decimal number with exactly two digits, e.g. 1.00, 12.34 or 0.90
 */
type MonetaryValue /^\d+(\.\d{2})?$/


type PaymentInput {
    senderAccountNumber: String,
    senderAccountName: String,
    recipientAccountNumber: String,
    recipientAccountName: String,
    amount: MonetaryValue,
    currency: Currency
}

endpoint GetPayments GET /api/payments?{accountNumber: String, limit: Integer, offset: Integer} -> {
    200 -> Payment[]
    400 -> PaymentsApiError
    500 -> PaymentsApiError
}

endpoint GetPaymentById GET /api/payments/{ id: String} -> {
    200 -> Payment
    400 -> PaymentsApiError
    404 -> PaymentsApiError
    500 -> PaymentsApiError
}

endpoint PostPayment POST PaymentInput /api/payments -> {
    201 -> Payment
    400 -> PaymentsApiError
    500 -> PaymentsApiError
}

