import AccountCircle from "@mui/icons-material/AccountCircle";
import { Stack } from "@mui/material";
import { User } from "../dummy-data.ts";
import { Link } from "react-router-dom";
import Button from "@mui/material/Button";

export type AppBarUserProps = {
  user: User | null;
};

export const AppBarUser = ({ user }: AppBarUserProps) => {
  const buttonText = user ? `${user.name} (${user.iban})` : "Log in";

  return (
    <Stack marginLeft={3} direction="row" alignItems="center" spacing={1}>
      <Button
        startIcon={<AccountCircle />}
        color="inherit"
        component={Link}
        to="/select-user"
      >
        {buttonText}
      </Button>
    </Stack>
  );
};
