import Footer from "@/components/Footer";
import NavBar from "@/components/NavBar";
import NavDrawer from "@/components/NavDrawer";
import { TextProvider } from "@/hooks/textContext";
import { UserProvider } from "@/hooks/userContext";
import theme from "@/theme";
import { Container, CssBaseline } from "@mui/material";
import { ThemeProvider } from "@mui/material/styles";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterMoment } from "@mui/x-date-pickers/AdapterMoment";
import { useState } from "react";
import "./index.css";

export default function Layout({ children, pageContext }) {
  const [drawer, setDrawer] = useState(false);

  return (
    <ThemeProvider theme={theme}>
      <LocalizationProvider dateAdapter={AdapterMoment}>
        <UserProvider>
          <TextProvider>
            <div className="bg-[#edebed] min-h-screen">
              <CssBaseline />
              <NavBar
                onDrawer={() => setDrawer(true)}
                defaultSearchQuery={pageContext.urlParsed?.search?.q ?? ""}
              />
              <NavDrawer open={drawer} onClose={() => setDrawer(false)} />
              <div className="bg-white pt-[6rem] pb-12">
                <Container maxWidth="lg">{children}</Container>
              </div>
              <Footer />
            </div>
          </TextProvider>
        </UserProvider>
      </LocalizationProvider>
    </ThemeProvider>
  );
}
