import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import SidebarProvider from "./components/SidebarComponents/SidebarProvider";
import UserActivityDetector from "./sensors/[id]/components/UserActivityDetector";
import { cookies } from "next/headers";
import AppProviders from "../../providers/AppProviders";

const inter = Inter({ 
  subsets: ["latin"],
  variable: "--font-inter",
  display: "swap",
});



export const metadata: Metadata = {
  title: "EcoTrack | Sensör Yönetim Platformu",
  description: "Gerçek zamanlı sensör takip ve arıza yönetim sistemi.",
};

export default async function DashboardLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
          const session = cookies().get("session")?.value

  return (
    <html lang="en">
      <body className={inter.className}>
              <AppProviders>
          <UserActivityDetector session = {session}/>
<div className="w-full min-h-screen bg-[#f1f0ee]">
              <SidebarProvider >{children}</SidebarProvider>
          </div>
        </AppProviders>
      </body>
    </html>
  );
}
