// src/types/index.ts

export interface AuditEvent {
  timestamp: Date;
  eventType: string;
  details: string;
  externalIpAddress: string;
  username: string;
}

export interface Transaction {
  id: string;
  /* Prettified string like € 12,05 */
  amount: string;
  sender: AccountHolder;
  recipient: AccountHolder;
  status: string;
  transactionCreatedTimestamp: Date;
  transactionCompletedTimestamp: Date | undefined;
  description: string;
}

export interface AccountHolder {
  accountNumber: string;
  name: string;
}

// export type Transaction = {
//   id: string;
//   transactionCreatedTimestamp: ISO8601DateTime;
//   transactionCompletedTimestamp: ISO8601DateTime | undefined;
//   senderAccountNumber: string;
//   senderAccountName: string;
//   recipientAccountNumber: string;
//   recipientAccountName: string;
//   amount: number;
//   currency: string;
// };
