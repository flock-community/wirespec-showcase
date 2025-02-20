import { Stack, Typography } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import { AuditEvent } from "../../types";

export type AuditTableProps = {
  auditEvents: AuditEvent[];
};

export const AuditTable = ({ auditEvents }: AuditTableProps) => {
  const columns = [
    { field: "timestamp", flex: 2 },
    {
      field: "externalIpAddress",
      flex: 2,
    },
    { field: "details", flex: 5 },
    { field: "eventType", flex: 1 },
    { field: "username", flex: 1 },
  ];
  const rows = auditEvents.map((event) => ({
    ...event,
    id: event.timestamp.getTime(),
  }));

  return (
    <Stack spacing={3}>
      <Typography variant="h3">Audit </Typography>
      <DataGrid columns={columns} rows={rows} hideFooter />
    </Stack>
  );
};
