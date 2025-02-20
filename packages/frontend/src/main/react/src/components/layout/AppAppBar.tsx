import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Button from "@mui/material/Button";
import Container from "@mui/material/Container";
import { Link } from "react-router-dom";
import { Stack, styled, Typography } from "@mui/material";
import { useLoggedInUser } from "../user/logged-in-user.ts";
import { AppBarUser } from "./AppBarUser.tsx";

const Offset = styled("div")(({ theme }) => theme.mixins.toolbar);

export const AppAppBar = () => {
  const { user } = useLoggedInUser(false);

  return (
    <>
      <AppBar position="fixed" enableColorOnDark>
        <Container maxWidth="lg">
          <Toolbar disableGutters>
            <Stack direction="row" spacing={3} sx={{ flexGrow: 1 }}>
              <Button
                component={Link}
                to="/"
                sx={{
                  flexGrow: 1,
                  padding: 0,
                  minWidth: "auto",
                  justifyContent: "flex-start",
                }}
              >
                <img
                  src="/wirespec.svg"
                  alt="Wirespec Logo"
                  style={{ height: 60 }}
                />
                <Typography color="white" variant="h6" sx={{ marginLeft: 2 }}>
                  Wirespec showcase
                </Typography>
              </Button>
              <Button
                component={Link}
                to="/audit"
                sx={{ color: "inherit" }} // Ensure visibility
              >
                Audit overview
              </Button>
              <Button
                component={Link}
                to="/transactions/create"
                sx={{ color: "inherit" }} // Ensure visibility
              >
                Create Transaction
              </Button>
              <Button
                component={Link}
                to="/transactions"
                sx={{ color: "inherit" }} // Ensure visibility
              >
                Transactions
              </Button>
            </Stack>
            <div>
              <AppBarUser user={user} />
            </div>
          </Toolbar>
        </Container>
      </AppBar>
      <Offset />
    </>
  );
};
