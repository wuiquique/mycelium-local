"use client";

import { useTexts } from "@/hooks/textContext";
import { useUser } from "@/hooks/userContext";
import {
  AppBar,
  Box,
  Button,
  IconButton,
  TextField,
  Toolbar,
} from "@mui/material";
import Link from "next/link";
import { AiOutlineSearch } from "react-icons/ai";
import { MdMenu } from "react-icons/md";

export default function NavBar({ onDrawer }: { onDrawer: () => void }) {
  const [user] = useUser();
  const texts = useTexts();

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
            variant="contained"
            disableElevation
            component={Link}
            href="/"
          >
            <img src="/redwhite.png" width="20px" />
            &nbsp;
            {texts.global.shopname}
          </Button>
        </div>
        <div>
          <Box sx={{ display: "flex", alignItems: "flex-end" }}>
            <AiOutlineSearch size="1.8rem" />
            <TextField sx={{ minWidth: "900px" }} variant="standard" />
            <Button
              variant="contained"
              disableElevation
              component={Link}
              href="/search"
            >
              {texts.header.searchbutton}
            </Button>
          </Box>
        </div>
        <div>
          {user.id === null ? (
            <>
              <Button component={Link} href="/login" color="inherit">
                {texts.header.loginbutton}
              </Button>
              <Button component={Link} href="/register" color="inherit">
                {texts.header.registerbutton}
              </Button>
            </>
          ) : (
            <>
              {texts.header.welcometext}
              {user.name} {user.lastname}
            </>
          )}
        </div>
      </Toolbar>
    </AppBar>
  );
}
