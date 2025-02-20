import "./App.css";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import AuditOverview from "./pages/AuditOverview.tsx";
import TransactionsOverview from "./pages/TransactionsOverview.tsx";
import { Box, Stack } from "@mui/material";
import ProjectOverview from "./pages/ProjectOverview.tsx";
import Container from "@mui/material/Container";
import { CreateTransaction } from "./pages/CreateTransaction.tsx";
import { SelectUserPage } from "./pages/SelectUserPage.tsx";
import { AppAppBar } from "./components/layout/AppAppBar.tsx";

const App = () => (
  <Container maxWidth="lg">
    <Stack spacing={5}>
      <BrowserRouter>
        <AppAppBar />
        <Box>
          <Routes>
            <Route path="/audit" element={<AuditOverview />} />
            <Route path="/transactions" element={<TransactionsOverview />} />
            <Route
              path="/transactions/create"
              element={<CreateTransaction />}
            />
            <Route path="/select-user" element={<SelectUserPage />} />
            <Route path="*" element={<ProjectOverview />} />
          </Routes>
        </Box>
      </BrowserRouter>
    </Stack>
  </Container>
);

export default App;
