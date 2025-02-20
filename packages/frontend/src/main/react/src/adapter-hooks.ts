import { fetchAuditEvents, fetchTransactions } from "./adapter.ts";
import { useEffect, useState } from "react";
import { AuditEvent, Transaction } from "./types";
import { ShowcaseError } from "./utils.ts";

export type Status = "DONE" | "LOADING" | "ERROR";

export type UseAuditEventsReturn =
  | {
      auditEvents: AuditEvent[];
      error: null;
      status: "DONE";
    }
  | {
      auditEvents: null;
      error: ShowcaseError;
      status: "ERROR";
    }
  | {
      auditEvents: null;
      error: null;
      status: "LOADING";
    };

export const useAuditEvents: () => UseAuditEventsReturn = () => {
  const [state, setState] = useState<UseAuditEventsReturn>({
    auditEvents: null,
    error: null,
    status: "LOADING",
  });

  useEffect(() => {
    fetchAuditEvents()
      .then((events) => {
        setState({ auditEvents: events, status: "DONE", error: null });
      })
      .catch((e) => {
        setState({ auditEvents: null, status: "ERROR", error: e });
      });
  }, []);

  return state;
};

export type UseTransactionsReturn =
  | {
      transactions: Transaction[];
      error: null;
      status: "DONE";
    }
  | {
      transactions: null;
      error: ShowcaseError;
      status: "ERROR";
    }
  | {
      transactions: null;
      error: null;
      status: "LOADING";
    };

export const useTransactions: () => UseTransactionsReturn = () => {
  const [state, setState] = useState<UseTransactionsReturn>({
    transactions: null,
    error: null,
    status: "LOADING",
  });

  useEffect(() => {
    fetchTransactions()
      .then((transactions) => {
        setState({ transactions: transactions, status: "DONE", error: null });
      })
      .catch((e) => {
        setState({ transactions: null, status: "ERROR", error: e });
      });
  }, []);

  return state;
};
