import { useEffect, useState } from "react";
import { User } from "../dummy-data.ts";
import { useNavigate } from "react-router-dom";

const LOGGED_IN_USER_LOCAL_STORAGE_KEY = "WIRESPEC_USER";

type AuthenticatedUser = User & { token: string };

// Get initial global state from something saved (and check if valid)
const getLoggedInUserFromLocalStorage = () => {
  const item = localStorage.getItem(LOGGED_IN_USER_LOCAL_STORAGE_KEY);
  if (item == null) return null;

  // TODO: use zod or typeguards for validation?
  try {
    const parsed = JSON.parse(item);
    if (
      parsed &&
      typeof parsed.username === "string" &&
      typeof parsed.name === "string" &&
      typeof parsed.iban === "string" &&
      typeof parsed.token === "string"
    ) {
      return parsed;
    }
  } catch (e) {
    console.error("Invalid user data in localStorage:", e);
  }

  return null;
};

/**
 * @deprecated Hack to get credentials in ts files
 */
export const getLoggedInUser = () => {
  return loggedInUser;
};

// Global state
let loggedInUser: AuthenticatedUser | null = getLoggedInUserFromLocalStorage();

// Sync state between various entries
let setStates: React.Dispatch<React.SetStateAction<User | null>>[] = [];

export const useLoggedInUser: (redirectIfNotLoggedIn?: boolean) => {
  user: User | null;
  setUser: (user: User | null, token?: string | null) => void;
} = (redirectIfNotLoggedIn: boolean = false) => {
  const [internalUser, setInternalUser] = useState<User | null>(loggedInUser);
  // TODO: Dit mag niet van Tom
  const navigate = useNavigate();

  // register and unregister
  useEffect(() => {
    setStates.push(setInternalUser);

    maybeRedirect();
    return () => {
      const index = setStates.indexOf(setInternalUser);
      setStates.splice(index, 1);
    };
  }, []);

  const maybeRedirect = () => {
    if (redirectIfNotLoggedIn && !internalUser) {
      navigate("/select-user");
    }
  };

  const setUser = (user: User | null, token: string | null = null) => {
    if (user) {
      loggedInUser = { ...user, token: token! };
      localStorage.setItem(
        LOGGED_IN_USER_LOCAL_STORAGE_KEY,
        JSON.stringify(loggedInUser),
      );
    } else {
      loggedInUser = null;
      localStorage.removeItem(LOGGED_IN_USER_LOCAL_STORAGE_KEY);
    }

    setStates.forEach((it) => it(user));
  };

  return {
    user: loggedInUser && {
      username: loggedInUser.username,
      name: loggedInUser.name,
      iban: loggedInUser.iban,
    },
    setUser,
  };
};
