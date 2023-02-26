import theme from "@/theme";
import {
  Box,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from "@mui/material";
import Link from "next/link";
import React from "react";
import { IconContext } from "react-icons";

export default function NavDrawer({
  open,
  onClose,
  items,
}: {
  open: boolean;
  onClose: () => void;
  items: {
    name: string;
    href: string;
    icon: JSX.Element;
  }[];
}) {
  return (
    <Drawer
      anchor="left"
      open={open}
      onClose={onClose}
      PaperProps={{
        sx: { backgroundColor: "primary.main", color: "primary.contrastText" },
      }}
    >
      <List className="max-w-xs w-[50vw]">
        {items.map((item, i) => (
          <ListItem disablePadding key={i}>
            <ListItemButton component={Link} href={item.href} onClick={onClose}>
              <ListItemIcon>
                <IconContext.Provider
                  value={{ color: theme.palette.primary.contrastText }}
                >
                  {item.icon}
                </IconContext.Provider>
              </ListItemIcon>
              <ListItemText primary={item.name} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Drawer>
  );
}
