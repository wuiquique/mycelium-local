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
import { MdLogin, MdProductionQuantityLimits } from "react-icons/md";
import { TbReportAnalytics, TbPlugConnected } from 'react-icons/tb'
import { AiOutlineUserSwitch, AiOutlineAppstoreAdd } from 'react-icons/ai'
import { BiCategory } from 'react-icons/bi'
import Footer from "../components/Footer";

const navItems = [
  {
    name: "Login",
    href: "/auth/login",
    icon: <MdLogin size='1.5rem'/>,
    perm: 1
  },
  {
    name: "Users", 
    href: "/admin/users",
    icon: <AiOutlineUserSwitch size='1.5rem'/>,
    perm: 2
  },
  {
    name: 'New Product', 
    href: '/admin/product/create', 
    icon: <AiOutlineAppstoreAdd size='1.5rem'/>,
    perm: 2
  },
  {
    name: 'Categories',
    href: '/admin/category',
    icon: <BiCategory size='1.5rem'/>, 
    perm: 2
  },
  {
    name: "Orders", 
    href: '/admin/orders', 
    icon: <MdProductionQuantityLimits size='1.5rem'/>,
    perm: 2
  },
  {
    name: 'Reportes', 
    href: '/admin/reports', 
    icon: <TbReportAnalytics size='1.5rem'/>,
    perm: 2
  },
  {
    name: 'Integrations', 
    href: '/admin/integrations', 
    icon: <TbPlugConnected size='1.5rem'/>,
    perm: 2
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
