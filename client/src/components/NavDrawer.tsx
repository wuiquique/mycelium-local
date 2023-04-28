import { useTexts } from "@/hooks/textContext";
import { useUser } from "@/hooks/userContext";
import theme from "@/theme";
import {
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from "@mui/material";
import { useMemo } from "react";
import { IconContext } from "react-icons";
import { AiOutlineAppstoreAdd, AiOutlineUserSwitch } from "react-icons/ai";
import { BiCategory } from "react-icons/bi";
import {
  MdLogin,
  MdProductionQuantityLimits,
  MdShoppingCart,
  MdTag,
  MdFactCheck,
} from "react-icons/md";
import { TbPlugConnected, TbReportAnalytics } from "react-icons/tb";
import { VscSymbolString } from "react-icons/vsc";

export default function NavDrawer({
  open,
  onClose,
}: {
  open: boolean;
  onClose: () => void;
}) {
  const texts = useTexts();

  const items: {
    name: string;
    href: string;
    icon: JSX.Element;
    privileges?: number;
  }[] = useMemo(
    () => [
      {
        name: texts.navdrawer.category,
        href: "/categories",
        icon: <MdTag />,
      },
      {
        name: texts.navdrawer.usercart,
        href: "/user/cart",
        icon: <MdShoppingCart />,
        privileges: 1,
      },
      {
        name: texts.navdrawer.orders,
        href: "/user/orders",
        icon: <MdFactCheck />,
        privileges: 1,
      },
      {
        name: texts.navdrawer.adminuser,
        href: "/admin/users",
        icon: <AiOutlineUserSwitch />,
        privileges: 2,
      },
      {
        name: texts.navdrawer.newproduct,
        href: "/admin/product/create",
        icon: <AiOutlineAppstoreAdd />,
        privileges: 2,
      },
      {
        name: texts.navdrawer.adminproduct,
        href: "/admin/product",
        icon: <BiCategory />,
        privileges: 2,
      },
      {
        name: texts.navdrawer.admincategory,
        href: "/admin/category",
        icon: <BiCategory />,
        privileges: 2,
      },
      {
        name: texts.navdrawer.adminorder,
        href: "/admin/orders",
        icon: <MdProductionQuantityLimits />,
        privileges: 2,
      },
      {
        name: texts.navdrawer.reports,
        href: "/admin/reports",
        icon: <TbReportAnalytics />,
        privileges: 2,
      },
      {
        name: texts.navdrawer.integrations,
        href: "/admin/integrations",
        icon: <TbPlugConnected />,
        privileges: 2,
      },
      {
        name: texts.navdrawer.admintext,
        href: "/admin/text",
        icon: <VscSymbolString />,
        privileges: 2,
      },
      {
        name: texts.navdrawer.login,
        href: "/login",
        icon: <MdLogin />,
        privileges: 0,
      },
      {
        name: texts.navdrawer.register,
        href: "/register",
        icon: <MdLogin />,
        privileges: 0,
      },
    ],
    [
      texts.navdrawer.admincategory,
      texts.navdrawer.adminorder,
      texts.navdrawer.adminproduct,
      texts.navdrawer.admintext,
      texts.navdrawer.adminuser,
      texts.navdrawer.category,
      texts.navdrawer.integrations,
      texts.navdrawer.login,
      texts.navdrawer.newproduct,
      texts.navdrawer.register,
      texts.navdrawer.reports,
      texts.navdrawer.usercart,
    ]
  );
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
              <ListItemButton component="a" href={item.href} onClick={onClose}>
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
