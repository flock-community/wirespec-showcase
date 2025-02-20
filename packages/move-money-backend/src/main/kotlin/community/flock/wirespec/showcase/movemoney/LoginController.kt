package community.flock.wirespec.showcase.movemoney

import community.flock.wirespec.showcase.movemoney.api.LoginEndpoint
import community.flock.wirespec.showcase.movemoney.api.LoginResponse
import community.flock.wirespec.showcase.movemoney.api.LogoutEndpoint
import community.flock.wirespec.showcase.movemoney.api.MoveMoneyError
import community.flock.wirespec.showcase.movemoney.audit.AuditEvent
import community.flock.wirespec.showcase.movemoney.audit.AuditEventType
import community.flock.wirespec.showcase.movemoney.audit.AuditService
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
internal class LoginController(
    private val userService: UserService,
    private val auditService: AuditService,
) : LoginEndpoint.Handler,
    LogoutEndpoint.Handler {
    override suspend fun login(request: LoginEndpoint.Request): LoginEndpoint.Response<*> {
        val userInformation =
            try {
                userService.getUserInformation(request.body.username)
            } catch (e: IllegalArgumentException) {
                return LoginEndpoint
                    .Response400(
                        body =
                            MoveMoneyError(
                                type = "Invalid Input",
                                title = "User Not Found",
                                status = 400,
                                detail = "The provided username does not match any existing user.",
                                instance = "/api/login",
                            ),
                    ).also {
                        auditService.audit(
                            AuditEvent(
                                timestamp = Instant.now(),
                                eventType = AuditEventType.LOGIN_FAILED,
                                externalIpAddress = request.headers.xforwardedfor,
                                details = "Failed login attempt for username: ${request.body.username}",
                                user = null,
                            ),
                        )
                    }
            }
        return LoginEndpoint
            .Response201(
                body =
                    LoginResponse(
                        token = userInformation.token,
                    ),
            ).also {
                auditService.audit(
                    AuditEvent(
                        eventType = AuditEventType.LOGIN_SUCCESS,
                        timestamp = Instant.now(),
                        externalIpAddress = request.headers.xforwardedfor,
                        details = "User logged in successfully: ${userInformation.user.username}",
                        user = userInformation.user,
                    ),
                )
            }
    }

    override suspend fun logout(request: LogoutEndpoint.Request): LogoutEndpoint.Response<*> {
        val user: User
        try {
            user = getUser(request.headers.token)
        } catch (e: IllegalStateException) {
            return LogoutEndpoint
                .Response400(
                    body =
                        MoveMoneyError(
                            type = "MoveMoneyError",
                            title = "Bad Request",
                            status = 400,
                            detail = e.message ?: "no detail",
                            instance = "move-money-api",
                        ),
                ).also {
                    auditService.audit(
                        AuditEvent(
                            timestamp = Instant.now(),
                            eventType = AuditEventType.LOGOUT_FAILED,
                            details = "User logout failed: ${e.message}",
                            externalIpAddress = request.headers.xforwardedfor,
                            user = null,
                        ),
                    )
                }
        }

        return LogoutEndpoint
            .Response204(body = Unit)
            .also {
                auditService.audit(
                    AuditEvent(
                        eventType = AuditEventType.LOGOUT_SUCCESS,
                        timestamp = Instant.now(),
                        externalIpAddress = request.headers.xforwardedfor,
                        details = "User logged out successfully.",
                        user = user,
                    ),
                )
            }
    }
}
