import { Box, Stack, TextField } from "@mui/material";
import Button from "@mui/material/Button";
import { FormEvent, useState } from "react";
import {
  randomAmount,
  randomDescription,
  randomIban,
  randomName,
} from "../dummy-data.ts"; // export type NewTransaction = Omit<Transaction, "id">

// TODO
// export type NewTransaction = Omit<Transaction, "id">
export type NewTransaction = {
  amount: number;
  currency: "EUR";
  recipientAccountName: string;
  recipientAccountNumber: string;
};

export type TransactionFormProps = {
  onSubmit: (transaction: NewTransaction) => void;
};

export const TransactionForm = ({ onSubmit }: TransactionFormProps) => {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const [formData, setFormData] = useState({
    amount: 0,
    description: "",
    recipientAccountName: "",
    recipientAccountNumber: "",
  });

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();

    onSubmit({
      amount: formData.amount,
      currency: "EUR",
      recipientAccountName: formData.recipientAccountName,
      recipientAccountNumber: formData.recipientAccountNumber,
    });
  };

  return (
    <Stack spacing={3}>
      <Button
        onClick={() =>
          setFormData({
            recipientAccountNumber: randomIban(),
            recipientAccountName: randomName(),
            amount: randomAmount(),
            description: randomDescription(),
          })
        }
        variant="outlined"
        color="secondary"
      >
        Fill with dummy data
      </Button>
      <form onSubmit={handleSubmit}>
        <Box display="flex" flexDirection="column" gap={2}>
          <TextField
            label="Recipient Account Name"
            name="recipientAccountName"
            value={formData.recipientAccountName}
            onChange={handleChange}
            required
            fullWidth
          />
          <TextField
            label="Recipient Account Number"
            name="recipientAccountNumber"
            value={formData.recipientAccountNumber}
            onChange={handleChange}
            required
            fullWidth
          />
          <TextField
            label="Amount"
            type="number"
            name="amount"
            value={formData.amount}
            onChange={handleChange}
            required
            fullWidth
          />
          <TextField
            label="Description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            required
            fullWidth
          />
          <Button type="submit" variant="contained" color="primary">
            Create Transaction
          </Button>
        </Box>
      </form>
    </Stack>
  );
};
