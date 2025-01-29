package community.flock.wirespec.showcase.movemoney

import org.springframework.stereotype.Service

private val JOHN_DOE = User(id = 42L, firstName = "John", lastName = "Doe")

private val ACCOUNT_INFO_JOHN = AccountInfo(
    accountNumber = "NL79ABNA9455762838",
    accountName = "John M. Doe",
)
private val USER_ACCOUNT_INFO = UserAccountInfo(ACCOUNT_INFO_JOHN, JOHN_DOE)

/**
 * Dummy implementation to get the user object
 * In a production like context this would be resolved either from a JWT, or from a session cookie
 */
internal fun getUser(token: String): User {
    if (token !== "2523A1DE-3ABA-4D33-9732-EFC340E0664F") {
        error("This is an illegal user")
    }

    return JOHN_DOE

}

@Service
internal class UserService {
    fun getUserAccountInfo(userId: Long): UserAccountInfo {
        if (userId != JOHN_DOE.id) {
            error("something went wrong getting user account info for $userId")
        }

        return USER_ACCOUNT_INFO
    }
}


