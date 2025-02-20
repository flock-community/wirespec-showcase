import {
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Stack,
  Typography,
} from "@mui/material";
import { User, users } from "../components/dummy-data.ts";
import { useLoggedInUser } from "../components/user/logged-in-user.ts";
import { useNavigate } from "react-router-dom";
import AccountCircle from "@mui/icons-material/AccountCircle";
import { LogoutRounded } from "@mui/icons-material";
import { login, logout } from "../adapter.ts";

export const SelectUserPage = () => {
  const { user, setUser } = useLoggedInUser();
  const navigate = useNavigate();

  const handleSelectUser = (newUser: User | null) => {
    if (newUser == null) {
      logout()
        .then(() => {
          console.log("Logged out ", user);
          setUser(null);
        })
        .catch((e) => console.error("Error logging out:", e));
    } else {
      login(newUser?.username)
        .then((token) => {
          console.log("Logged in as", newUser);
          setUser(newUser, token);
          navigate("/");
        })
        .catch((e) => console.error("Error logging in:", e));
    }
  };

  return (
    <Stack spacing={3}>
      <Typography variant="h3">Select user</Typography>
      <List>
        {users.map((user) => (
          <ListItem key={user.username}>
            <ListItemButton
              key={user.username}
              onClick={() => handleSelectUser(user)}
            >
              <ListItemIcon>
                <AccountCircle />
              </ListItemIcon>
              <ListItemText primary={user.name} />
            </ListItemButton>
          </ListItem>
        ))}
        {user != null && (
          <ListItem>
            <ListItemButton key="logout" onClick={() => handleSelectUser(null)}>
              <ListItemIcon>
                <LogoutRounded />
              </ListItemIcon>
              <ListItemText primary="Log out" />
            </ListItemButton>
          </ListItem>
        )}
      </List>
    </Stack>
  );
};
