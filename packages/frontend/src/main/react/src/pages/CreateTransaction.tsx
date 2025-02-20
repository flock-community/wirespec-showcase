// src/pages/CreateTransaction.tsx
import { useNavigate } from "react-router-dom";
import { Stack, Typography } from "@mui/material";
import {
  NewTransaction,
  TransactionForm,
} from "../components/transaction/TransactionForm.tsx";
import { createTransaction } from "../adapter.ts";
import { useLoggedInUser } from "../components/user/logged-in-user.ts";

export const CreateTransaction = () => {
  useLoggedInUser(true);
  const navigate = useNavigate();

  const handleSubmit = async (transaction: NewTransaction) => {
    createTransaction(transaction)
      .then(() => navigate("/transactions"))
      .catch((e) => console.error("Error creating transaction:", e));
  };

  return (
    <Stack spacing={3}>
      <Typography variant="h3">Create Transaction</Typography>
      <TransactionForm onSubmit={handleSubmit} />
    </Stack>
  );
};
