import type { Metadata } from "next";
import "./globals.css";
import Providers from "./providers/Providers";




export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className="w-full "
        >
          <Providers>
        {children}
          </Providers>
      </body>
    </html>
  );
}
