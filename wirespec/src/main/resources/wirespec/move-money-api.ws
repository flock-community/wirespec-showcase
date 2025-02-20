/**
  * An ISO-8601 compliant regex for a date time string
  * https://en.wikipedia.org/wiki/ISO_8601
  */
type ISO8601DateTime /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})(.\d+)?(Z|[\+\-]\d{2}:\d{2})$/

type Transaction {
    id: String,
    transactionCreatedTimestamp: ISO8601DateTime,
    transactionCompletedTimestamp: ISO8601DateTime?,
    senderAccountNumber: String,
    senderAccountName: String,
    recipientAccountNumber: String,
    recipientAccountName: String,
    amount: Number,
    currency: String
}

type TransactionInput {
    recipientAccountNumber: String,
    recipientAccountName: String,
    amount: Number,
    currency: String
}

/**
  * Following the RFC-7807 standard for problem details
  * https://www.rfc-editor.org/rfc/rfc7807
  */
type MoveMoneyError {
    `type`: String,
    title: String,
    status: Integer,
    detail: String,
    instance: String
}

endpoint GetMoveMoneyTransactions GET /api/move-money/transactions?{limit: Integer, offset: Integer}#{token: String , xforwardedfor: String } -> {
    200 -> Transaction[]
    400 -> MoveMoneyError
    403 -> MoveMoneyError
    500 -> MoveMoneyError
}

endpoint GetMoveMoneyTransactionById GET /api/move-money/transactions/{ id: String}#{token: String, xforwardedfor: String } -> {
    200 -> Transaction
    400 -> MoveMoneyError
    403 -> MoveMoneyError
    404 -> MoveMoneyError
    500 -> MoveMoneyError
}

endpoint SendMoney POST TransactionInput /api/move-money/transactions#{ token: String, xforwardedfor: String  } -> {
    201 -> Transaction
    400 -> MoveMoneyError
    403 -> MoveMoneyError
    500 -> MoveMoneyError
}


type LoginInput {
    username: String,
    password: String
}

type LoginResponse {
    token: String
}

endpoint Login POST LoginInput /api/move-money/login # { xforwardedfor: String } -> {
    201 -> LoginResponse
    400 -> MoveMoneyError
    403 -> MoveMoneyError
    500 -> MoveMoneyError
}

endpoint Logout POST /api/move-money/logout # { token: String, xforwardedfor: String } -> {
   204 -> Unit
   400 -> MoveMoneyError
   403 -> MoveMoneyError
   500 -> MoveMoneyError
}
