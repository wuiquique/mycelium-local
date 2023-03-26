"use client";

import { useTexts } from "@/hooks/textContext";
import { useUser } from "@/hooks/userContext";
import {
  AppBar,
  Box,
  Button,
  IconButton,
  InputAdornment,
  TextField,
  Toolbar,
} from "@mui/material";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { AiOutlineSearch } from "react-icons/ai";
import { MdMenu } from "react-icons/md";

export default function NavBar({
  onDrawer,
  defaultSearchQuery,
}: {
  onDrawer: () => void;
  defaultSearchQuery: string;
}) {
  const [user] = useUser();
  const texts = useTexts();
  const router = useRouter();

  const [searchQuery, setSearchQuery] = useState(defaultSearchQuery);

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
          <form
            onSubmit={(e) => {
              e.preventDefault();
              router.push(`/search?q=${searchQuery}`);
            }}
          >
            <Box
              sx={{
                display: "flex",
              }}
            >
              <Box
                sx={{
                  backgroundColor: "white",
                  borderRadius: "0.25rem",
                }}
              >
                <TextField
                  sx={{ width: "50vw", maxWidth: "900px" }}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <AiOutlineSearch size="1.5rem" />
                      </InputAdornment>
                    ),
                  }}
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.currentTarget.value)}
                  size="small"
                />
              </Box>
              <Button
                variant="contained"
                disableElevation
                component={Link}
                href={`/search?q=${searchQuery}`}
              >
                {texts.header.searchbutton}
              </Button>
              <button hidden type="submit"></button>
            </Box>
          </form>
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
