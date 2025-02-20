// src/pages/AuditOverview.tsx
import { useAuditEvents } from "../adapter-hooks.ts";
import { AuditTable } from "../components/audit/AuditTable.tsx";
import { Loading } from "../components/common/Loading.tsx";
import { ErrorState } from "../components/common/ErrorState.tsx";

const AuditOverview = () => {
  const { auditEvents, status, error } = useAuditEvents();

  if (status == "LOADING") return <Loading />;

  if (status == "ERROR") return <ErrorState error={error} />;

  return <AuditTable auditEvents={auditEvents} />;
};

export default AuditOverview;
