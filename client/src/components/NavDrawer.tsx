import { useUser } from "@/hooks/userContext";
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
    privileges?: number;
  }[];
}) {
  const [user] = useUser();

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
        {items
          .filter(
            (m) =>
              m.privileges === undefined ||
              (m.privileges === 0 && user.id === null) ||
              (user.id !== null &&
                m.privileges > 0 &&
                m.privileges <= user.roleId)
          )
          .map((item, i) => (
            <ListItem disablePadding key={i}>
              <ListItemButton
                component={Link}
                href={item.href}
                onClick={onClose}
              >
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
