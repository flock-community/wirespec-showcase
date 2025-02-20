import { GetAllAuditEvents } from "./wirespec/Audit-api.ts";
import { auditClient } from "./AuditClient.ts";
import { ShowcaseError } from "./utils.ts";
import { AuditEvent, Transaction } from "./types";
import { moveMoneyClient } from "./MovemoneyClient.ts";
import {
  GetMoveMoneyTransactions,
  Login,
  Logout,
  SendMoney,
} from "./wirespec/Move-money-api.ts";
import { NewTransaction } from "./components/transaction/TransactionForm.tsx";
import { getLoggedInUser } from "./components/user/logged-in-user.ts";

let userToken: () => string = () => {
  let loggedInUser = getLoggedInUser();
  if (loggedInUser == undefined) {
    throw new ShowcaseError("User is not logged in");
  }
  return loggedInUser.token;
};

const CLIENT_IP = "12.123.41.255";

export const fetchAuditEvents: () => Promise<AuditEvent[]> = async () => {
  const response: GetAllAuditEvents.Response =
    await auditClient.getAllAuditEvents(
      GetAllAuditEvents.request({ token: userToken() }),
    );

  switch (response.status) {
    case 200:
      return consumeAuditEvents(response);
    case 500:
      throw new Error("Server error occurred while fetching audit events");
  }
};

const consumeAuditEvents = (response: GetAllAuditEvents.Response200) =>
  response.body.auditEvents.map((it) => ({
    externalIpAddress: it.externalIpAddress,
    timestamp: new Date(it.timestamp),
    details: it.details,
    eventType: it.eventType,
    username: it.username ?? "Unknown/Anonymous user",
  }));

export const fetchTransactions: () => Promise<Transaction[]> = async () => {
  const response = await moveMoneyClient.getMoveMoneyTransactions(
    GetMoveMoneyTransactions.request({
      token: userToken(),
      xforwardedfor: CLIENT_IP,
      limit: 10,
      offset: 0,
    }),
  );

  switch (response.status) {
    case 200: {
      return consumeMoveMoneyTransactions(response);
    }
    case 400:
    case 403:
    case 500:
      throw new ShowcaseError(
        "Server error occurred while fetching transactions",
        response.body,
      );
  }
};

const consumeMoveMoneyTransactions: (
  response: GetMoveMoneyTransactions.Response200,
) => Transaction[] = (response: GetMoveMoneyTransactions.Response200) => {
  const currencySymbol = (currencySymbol: string) => {
    switch (currencySymbol) {
      case "EUR":
        return "€ ";
      case "USD":
        return "$ ";
      default:
        throw new Error("Unknown currency symbol: " + currencySymbol);
    }
  };

  return response.body.map((it) => {
    return {
      amount: currencySymbol(it.currency) + it.amount,
      status:
        it.transactionCompletedTimestamp != undefined ? "Completed" : "Pending",
      transactionCompletedTimestamp:
        it.transactionCompletedTimestamp != undefined
          ? new Date(it.transactionCompletedTimestamp)
          : undefined,
      transactionCreatedTimestamp: new Date(it.transactionCreatedTimestamp),
      recipient: {
        name: it.recipientAccountName,
        accountNumber: it.recipientAccountNumber,
      },
      sender: {
        accountNumber: it.senderAccountNumber,
        name: it.senderAccountName,
      },
      description: "",
      id: it.id,
    } as const satisfies Transaction;
  });
};

export const createTransaction = async (transaction: NewTransaction) => {
  let responsePromise = await moveMoneyClient.sendMoney(
    SendMoney.request({
      token: userToken(),
      xforwardedfor: "12.123.41.255",
      body: transaction,
    }),
  );

  // TODO: complete
  switch (responsePromise.status) {
    case 201:
      console.log("Successful transaction creation");
      return;
    case 400:
    case 403:
    case 500:
      throw new ShowcaseError(
        "Could not create transaction",
        responsePromise.body,
      );
  }
};

export const login = async (username: string) => {
  const response = await moveMoneyClient.login(
    Login.request({
      xforwardedfor: CLIENT_IP,
      body: {
        username: username,
        password: "password",
      },
    }),
  );

  switch (response.status) {
    case 201:
      console.log("Successful login");
      return response.body.token;
    case 400:
    case 403:
    case 500:
      throw new ShowcaseError("Could not login user", response.body);
  }
};

export const logout = async () => {
  const response = await moveMoneyClient.logout(
    Logout.request({
      token: userToken(),
      xforwardedfor: CLIENT_IP,
    }),
  );

  switch (response.status) {
    case 204:
      console.log("Successful logout");
      return;
    case 400:
    case 403:
      throw new ShowcaseError("Could not logout user", response.body);
  }
};
