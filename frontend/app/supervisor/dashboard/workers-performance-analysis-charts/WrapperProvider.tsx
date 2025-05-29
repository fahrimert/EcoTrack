"use client"
import { ChartDataProvider } from "@mui/x-charts";
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { ReactNode } from "react";
interface ProvidersProps {
  children: ReactNode;
}

const WrapperProvider = ({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) => {
    const theme = createTheme({
  palette: {
    mode: 'light', // veya 'dark'
  },
});
  return (
        <ThemeProvider theme={theme}>
      <CssBaseline />
      {children}

            </ThemeProvider>

  )
}

export default WrapperProvider