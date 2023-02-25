import { createTheme } from '@mui/material/styles';
import * as colors from 'tailwindcss/colors'

// Create a theme instance.
const theme = createTheme({
  palette: {
    primary: {
      main: colors.rose[600],
    },
    secondary: {
      main: colors.amber[500],
    },
    error: {
      main: colors.red[600],
    },
  },
});

export default theme;
