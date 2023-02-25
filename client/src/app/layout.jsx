"use client";
import "./globals.css";

import React from "react";
import { Container, CssBaseline } from "@mui/material";
import NavBar from "../components/NavBar";
import { ThemeProvider } from "@mui/material/styles";
import theme from "../theme";

export default function layout({ children }) {
  return (
    <html lang="en">
      {/*
        <head /> will contain the components returned by the nearest parent
        head.tsx. Find out more at https://beta.nextjs.org/docs/api-reference/file-conventions/head
        */}
      <head />
      <body>
        <ThemeProvider theme={theme}>
          <CssBaseline />
          <NavBar></NavBar>
          <Container className="mt-[6rem]" maxWidth="lg">
            <div className="text-center">{children}</div>
          </Container>
        </ThemeProvider>
      </body>
    </html>
  );
}
