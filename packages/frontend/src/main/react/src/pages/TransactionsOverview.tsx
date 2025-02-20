// src/pages/TransactionsOverview.tsx
import { useTransactions } from "../adapter-hooks.ts";
import { Loading } from "../components/common/Loading.tsx";
import { ErrorState } from "../components/common/ErrorState.tsx";
import { TransactionTable } from "../components/transaction/TransactionTable.tsx";
import { Stack, Typography } from "@mui/material";
import Button from "@mui/material/Button";
import { Link } from "react-router-dom";

const TransactionsOverview = () => {
  const { transactions, status, error } = useTransactions();

  if (status === "LOADING") {
    return <Loading />;
  }

  if (status === "ERROR") {
    return <ErrorState error={error} />;
  }

  return (
    <Stack spacing={3}>
      <Typography variant="h3">Transactions</Typography>
      <Button variant="contained" component={Link} to="/transactions/create">
        Create transaction
      </Button>
      <TransactionTable transactions={transactions} />
    </Stack>
  );
};

export default TransactionsOverview;
