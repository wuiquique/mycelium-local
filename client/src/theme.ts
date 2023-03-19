import { createTheme } from "@mui/material/styles";
import type {} from "@mui/x-date-pickers/themeAugmentation";
import { amber, red, rose } from "tailwindcss/colors";

// Create a theme instance.
const theme = createTheme({
  palette: {
    primary: {
      main: rose[600],
    },
    secondary: {
      main: amber[600],
    },
    error: {
      main: red[600],
    },
  },
});

export default theme;
