package community.flock.wirespec.showcase.movemoney

import org.springframework.stereotype.Service

@Service
internal class TransactionService(
    private val paymentsAdapter: PaymentsAdapter,
    private val userService: UserService,
) {
    suspend fun getTransactions(
        userId: Long,
        limit: Int,
        offset: Int,
    ): List<Transaction> {
        val (accountInfo, _) = userService.getUserAccountInfo(userId)
        return paymentsAdapter.getTransactions(accountInfo, limit, offset)
    }

    suspend fun getTransaction(
        transactionId: String,
        userId: Long,
    ): Transaction {
        val (_, user) = userService.getUserAccountInfo(userId)
        return paymentsAdapter.getTransactionById(transactionId, user)
    }

    suspend fun createTransaction(
        userId: Long,
        createTransactionRequest: CreateTransactionRequest,
    ): Transaction {
        val (accountInfo, _) = userService.getUserAccountInfo(userId)
        return paymentsAdapter.createTransaction(accountInfo, createTransactionRequest)
    }
}
