type AuditEvent {
  timestamp: Integer,
  eventType: String,
  details: String,
  externalIpAddress: String,
  username: String?
}

type AuditEvents {
    auditEvents: AuditEvent[]
}

endpoint GetAllAuditEvents GET /api/audits#{ token: String } -> {
    200 -> AuditEvents
    500 -> AuditError
}

/**
  * Following the RFC-7807 standard for problem details
  * https://www.rfc-editor.org/rfc/rfc7807
  */
type AuditError {
    `type`: String,
    title: String,
    status: Integer,
    detail: String,
    instance: String
}


channel AuditEventChannel -> AuditEvent
