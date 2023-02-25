"use client";

import React from 'react'
import {
  AppBar,
  Button,
  IconButton,
  Toolbar,
  Typography,
} from "@mui/material";
import { MdMenu } from "react-icons/md";
import Link from 'next/link';

export default function NavBar() {
  return (
    <AppBar>
      <Toolbar color="primary">
        <IconButton
          size="large"
          edge="start"
          color="inherit"
          aria-label="menu"
          sx={{ mr: 2 }}
        >
          <MdMenu />
        </IconButton>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          News
        </Typography>
        <Button component={Link} href="/auth/login" color="inherit">Login</Button>
        <Button component={Link} href="/auth/register" color="inherit">register</Button>
      </Toolbar>
    </AppBar>
  )
}
