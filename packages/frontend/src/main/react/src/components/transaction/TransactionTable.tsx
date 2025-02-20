import { DataGrid, GridColDef } from "@mui/x-data-grid";
import { Transaction } from "../../types";

export type TransactionTableProps = {
  transactions: Transaction[];
};

export const TransactionTable = ({ transactions }: TransactionTableProps) => {
  const columns: GridColDef[] = [
    { field: "status", flex: 1 },
    {
      field: "created",
      flex: 2,
      valueFormatter: (value: Date) => value.toISOString(),
    },
    { field: "completed", flex: 1 },
    { field: "senderAccount", flex: 2 },
    // { field: "senderAccountName", flex: 1 },
    { field: "recipientAccount", flex: 2 },
    // { field: "recipientAccountName", flex: 1 },
    { field: "amount", flex: 1 },
  ] as const;

  const rows = transactions.map((transaction) => ({
    id: transaction.id,
    status: transaction.status,
    created: transaction.transactionCreatedTimestamp,
    senderAccount: transaction.sender.name + transaction.sender.accountNumber,
    // senderAccountName: transaction.sender.name,
    recipientAccount:
      transaction.recipient.name + transaction.recipient.accountNumber,
    // recipientAccountName: transaction.recipient.name,
    amount: transaction.amount,
  }));

  return <DataGrid columns={columns} rows={rows} hideFooter />;
};
