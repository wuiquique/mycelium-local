"use client";

import React from "react";
import { AppBar, Button, IconButton, Toolbar, Typography, TextField, Box } from "@mui/material";
import { red } from '@mui/material/colors';
import { MdMenu } from "react-icons/md";
import Link from "next/link";
import { AiOutlineSearch } from 'react-icons/ai'

export default function NavBar({ onDrawer }: { onDrawer: () => void }) {

  const styles = () => ({
    notchedOutline: {
      borderWidth: "1px",
      borderColor: "yellow !important"
    }
  });

  return (
    <AppBar>
      <Toolbar color="primary" sx={{ justifyContent: 'space-between' }}>
        <div className='flex'>
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
          <Typography variant="h6" component="div" className="mt-2">
            News
          </Typography>
        </div>
        <div>
          <Box sx={{ display: 'flex', alignItems: 'flex-end' }}>
              <AiOutlineSearch size='1.8rem'/>
              <TextField
                  sx={{ minWidth:"900px" }}
                  variant='standard'
              />
          </Box>
        </div>
        <div>
          <Button component={Link} href="/auth/login" color="inherit">
            Login
          </Button>
          <Button component={Link} href="/auth/register" color="inherit">
            register
          </Button>
        </div>
      </Toolbar>
    </AppBar>
  );
}
