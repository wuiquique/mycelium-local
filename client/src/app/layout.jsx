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
import {
  MdCreate,
  MdLogin,
  MdShoppingCart,
  MdTag,
  MdVerifiedUser,
} from "react-icons/md";
import Footer from "../components/Footer";
import { UserProvider } from "../hooks/userContext";

const navItems = [
  {
    name: "Categorías",
    href: "/categories",
    icon: <MdTag />,
  },
  {
    name: "Carrito",
    href: "/user/cart",
    icon: <MdShoppingCart />,
    privileges: 1,
  },
  {
    name: "Usuarios",
    href: "/admin/users",
    icon: <MdVerifiedUser />,
    privileges: 2,
  },
  {
    name: "Categorías",
    href: "/admin/category",
    icon: <MdTag />,
    privileges: 2,
  },
  {
    name: "Reportes",
    href: "/admin/reports",
    icon: <MdTag />,
    privileges: 2,
  },
  {
    name: "Login",
    href: "/auth/login",
    icon: <MdLogin />,
    privileges: 0,
  },
  {
    name: "Register",
    href: "/auth/register",
    icon: <MdCreate />,
    privileges: 0,
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
            <UserProvider>
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
            </UserProvider>
          </LocalizationProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}
