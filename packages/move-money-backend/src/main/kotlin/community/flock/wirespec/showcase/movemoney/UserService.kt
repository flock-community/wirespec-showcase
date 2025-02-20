package community.flock.wirespec.showcase.movemoney

import community.flock.wirespec.showcase.movemoney.Users.JANE
import community.flock.wirespec.showcase.movemoney.Users.JOHN
import community.flock.wirespec.showcase.movemoney.Users.PETER
import org.springframework.stereotype.Service

internal data class UserInformation(
    val user: User,
    val accountInfo: AccountInfo,
    val token: String,
) {
    val userAccountInfo = UserAccountInfo(accountInfo, user)
}

private object Users {
    val JOHN =
        UserInformation(
            User(id = 42L, username = "johnmdoe", firstName = "John", lastName = "Doe"),
            AccountInfo(
                accountNumber = "NL12INGB000123456789",
                accountName = "John M. Doe",
            ),
            "2523A1DE-3ABA-4D33-9732-EFC340E0664F",
        )

    val PETER =
        UserInformation(
            User(id = 101L, username = "peter1_green", firstName = "Peter", lastName = "Green"),
            AccountInfo(
                accountNumber = "NL79ABNA9455762838",
                accountName = "Peter Green",
            ),
            "E0D29BF6-28FF-49C8-AE07-36CD6575DE16",
        )

    val JANE =
        UserInformation(
            User(id = 279L, username = "janeDoe_12", firstName = "Jane", lastName = "Doe"),
            AccountInfo(
                accountNumber = "NL11RABO9391947572",
                accountName = "Jane Doe",
            ),
            "FE70AB70-7404-4952-AA59-57C7073CFA60",
        )
}

/**
 * Dummy implementation to get the user object
 * In a production like context this would be resolved either from a JWT, or from a session cookie
 */
internal fun getUser(token: String): User =
    when (token) {
        JOHN.token -> JOHN.user
        JANE.token -> JANE.user
        PETER.token -> PETER.user
        else -> error("this is an illegal user")
    }

@Service
internal class UserService {
    fun getUserInformation(username: String): UserInformation =
        when (username) {
            JOHN.user.username -> JOHN
            JANE.user.username -> JANE
            PETER.user.username -> PETER
            else -> throw IllegalArgumentException("User with username $username not found")
        }

    fun getUserAccountInfo(userId: Long): UserAccountInfo =
        when (userId) {
            JOHN.user.id -> JOHN.userAccountInfo
            PETER.user.id -> PETER.userAccountInfo
            JANE.user.id -> JANE.userAccountInfo
            else -> throw IllegalArgumentException("something went wrong getting user account info for $userId")
        }
}
