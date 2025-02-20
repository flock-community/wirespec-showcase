type AuditEvent {
  timestamp: Integer,
  eventType: AuditEventType,
  details: String,
  externalIpAddress: String,
  username: String?
}

/**
 * Comment on type
 */
enum AuditEventType {
  LOGIN_SUCCES,
  LOGIN_FAIL,
  LOGOUT_SUCCESS,
  LOGOUT_FAIL,
  LIST_PAYMENTS,
  GET_PAYMENT_BY_ID,
  CREATE_PAYMENT,
  PAYMENT_INITIATED,
  PAYMENT_COMPLETED
}


channel AuditEventChannel -> AuditEvent
