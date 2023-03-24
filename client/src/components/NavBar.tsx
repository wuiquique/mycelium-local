"use client";

import React from "react";
import {
  AppBar,
  Button,
  IconButton,
  Toolbar,
  Typography,
  TextField,
  Box,
} from "@mui/material";
import { red } from "@mui/material/colors";
import { MdMenu } from "react-icons/md";
import Link from "next/link";
import { AiOutlineSearch } from "react-icons/ai";
import { useUser } from "@/hooks/userContext";

export default function NavBar({ onDrawer }: { onDrawer: () => void }) {
  const [user] = useUser();

  const styles = () => ({
    notchedOutline: {
      borderWidth: "1px",
      borderColor: "yellow !important",
    },
  });

  return (
    <AppBar>
      <Toolbar color="primary" sx={{ justifyContent: "space-between" }}>
        <div className="flex">
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
            onClick={onDrawer}
          >
            <MdMenu />
          </IconButton>
          <Button 
            variant='contained' 
            disableElevation
            component={Link}
            href='/'
          >
            <img
              src="/redwhite.png"
              width='20px'
            />
            &nbsp;
            mycellium :)
          </Button>
        </div>
        <div>
          <Box sx={{ display: 'flex', alignItems: 'flex-end' }}>
              <AiOutlineSearch size='1.8rem'/>
              <TextField
                sx={{ minWidth:"900px" }}
                variant='standard'
              />
              <Button
                variant='contained'
                disableElevation
                component={Link}
                href="/search"
              >
                Search
              </Button>
          </Box>
        </div>
        <div>
          {user.id === null ? (
            <>
              <Button component={Link} href="/login" color="inherit">
                Login
              </Button>
              <Button component={Link} href="/register" color="inherit">
                register
              </Button>
            </>
          ) : (
            <>
              Welcome, {user.name} {user.lastname}
            </>
          )}
        </div>
      </Toolbar>
    </AppBar>
  );
}
