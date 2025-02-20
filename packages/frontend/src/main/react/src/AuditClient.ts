import { GetAllAuditEvents } from "./wirespec/Audit-api.ts";
import { serialization, wirespecWebClient } from "./WirespecWebClient.ts";

class AuditClient implements GetAllAuditEvents.Handler {
  private client = GetAllAuditEvents.client(serialization);

  getAllAuditEvents: (
    request: GetAllAuditEvents.Request,
  ) => Promise<GetAllAuditEvents.Response> = async (request) => {
    let rawRequest = this.client.to(request);
    let rawResponse = await wirespecWebClient(rawRequest);
    return this.client.from(rawResponse);
  };
}

export const auditClient = new AuditClient();
