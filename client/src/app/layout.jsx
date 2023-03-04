"use client";
import "./globals.css";

import React, { useState } from "react";
import { Container, CssBaseline } from "@mui/material";
import NavBar from "../components/NavBar";
import { ThemeProvider } from "@mui/material/styles";
import theme from "../theme";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterMoment } from "@mui/x-date-pickers/AdapterMoment";
import NavDrawer from "../components/NavDrawer";
import { MdLogin } from "react-icons/md";
import Footer from "../components/Footer";

const navItems = [
  {
    name: "Login",
    href: "/auth/login",
    icon: <MdLogin />,
  },
];

export default function Layout({ children }) {
  const [drawer, setDrawer] = useState(false);

  return (
    <html lang="en">
      {/*
        <head /> will contain the components returned by the nearest parent
        head.tsx. Find out more at https://beta.nextjs.org/docs/api-reference/file-conventions/head
        */}
      <head />
      <body>
        <ThemeProvider theme={theme}>
          <LocalizationProvider dateAdapter={AdapterMoment}>
            <CssBaseline />
            <NavBar onDrawer={() => setDrawer(true)} />
            <NavDrawer
              open={drawer}
              onClose={() => setDrawer(false)}
              items={navItems}
            />
            <Container className="mt-[6rem]" maxWidth="lg">
              {children}
            </Container>
            <Footer />
          </LocalizationProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}
