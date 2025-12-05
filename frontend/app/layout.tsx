import "./globals.css";
import type { Metadata } from "next";
import { Inter } from "next/font/google";
import AppProviders from "./providers/AppProviders";

const inter = Inter({ 
  subsets: ["latin"],
  variable: "--font-inter",
  display: "swap",
});

export const metadata: Metadata = {
  title: "EcoTrack | Sensör Yönetim Platformu",
  description: "Gerçek zamanlı sensör takip ve arıza yönetim sistemi.",
};
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className={`${inter.variable} font-sans antialiased w-full min-h-screen bg-gray-50`}>
        <AppProviders>
        {children}
        </AppProviders>
      </body>
    </html>
  );
}
