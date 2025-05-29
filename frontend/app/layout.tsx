import { Provider } from "@/components/ui/provider";
import "./globals.css";
import Providers from "./providers/Providers";
import { AppRouterCacheProvider } from '@mui/material-nextjs/v15-appRouter';

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className="w-full ">
  <AppRouterCacheProvider>

        <Provider>
        <Providers>{children}</Providers>
        </Provider>
  </AppRouterCacheProvider>

      </body>
    </html>
  );
}
