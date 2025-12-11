"use client";
import React, { useState } from "react";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { 
  MdOutlineWaterDrop, 
  MdGroups2, 
  MdDashboard, 
} from "react-icons/md";
import { BsListTask } from "react-icons/bs";
import { HiOutlineLogout } from "react-icons/hi";
import Link from "next/link";


interface SidebarProps {
  children: React.ReactNode;
  session: any;
}

const MENU_ITEMS = [
  {
    title: "Genel Bakış",
    href: "/dashboard",
    icon: MdDashboard,
  },
  {
    title: "Görev Geçmişi",
    href: "/worker/dashboard/past-sensors",
    icon: BsListTask,
  },
  {
    title: "Ekip Takibi",
    href: "/worker/dashboard/ekiptakibi",
    icon: MdGroups2,
  }
];

const SidebarContent = ({ 
  userProfile, 
  handleLogout, 
  currentPath, 
  collapsed = false 
}: { 
  userProfile: any, 
  handleLogout: () => void, 
  currentPath: string, 
  collapsed?: boolean 
}) => {
  return (
    <div className="flex flex-col h-full justify-between bg-[#f8f9fa] border-r border-gray-200 text-gray-700">
      
      <div className={cn("flex items-center p-6 h-[80px]", collapsed ? "justify-center" : "justify-start gap-3")}>
        <div className="bg-emerald-500 p-2 rounded-xl shadow-lg shadow-emerald-200">
             <MdOutlineWaterDrop className="text-white text-xl" />
        </div>
        {!collapsed && (
          <h1 className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-emerald-600 to-teal-600">
            EcoTrack
          </h1>
        )}
      </div>

      <nav className="flex-1 px-3 py-4 space-y-2 overflow-y-auto">
        {MENU_ITEMS.map((item, index) => {
          const isActive = currentPath === item.href;
          return (
            <Link
              key={index}
              href={item.href}
              className={cn(
                "flex items-center gap-3 p-3 rounded-xl transition-all duration-200 group relative",
                isActive 
                  ? "bg-white shadow-sm text-emerald-600 border border-gray-100" 
                  : "hover:bg-gray-100 text-gray-500 hover:text-gray-900",
                 collapsed && "justify-center"
              )}
            >
              <item.icon size={22} className={cn(isActive && "text-emerald-500")} />
              
              {!collapsed && <span className="font-medium text-sm">{item.title}</span>}
              
              {collapsed && (
                <div className="absolute left-14 bg-gray-900 text-white text-xs px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap z-50 pointer-events-none">
                  {item.title}
                </div>
              )}
            </Link>
          );
        })}
      </nav>

      <div className="p-4 border-t border-gray-200 bg-white/50">
        <div className={cn("flex items-center gap-3", collapsed ? "justify-center flex-col" : "justify-between")}>
          
          <div className="flex items-center gap-3 overflow-hidden">
            <div className="w-10 h-10 rounded-full bg-emerald-100 border-2 border-emerald-200 flex items-center justify-center shrink-0">
               <span className="text-emerald-700 font-bold">
                 {userProfile?.firstName?.[0] || "U"}
               </span>
            </div>
            {!collapsed && (
              <div className="flex flex-col">
                <span className="text-sm font-semibold text-gray-800 truncate max-w-[120px]">
                  {userProfile?.firstName || "Kullanıcı"}
                </span>
                <span className="text-xs text-gray-400">Aktif</span>
              </div>
            )}
          </div>

          <Button 
            variant="ghost" 
            size="icon"
            onClick={handleLogout}
            className="hover:bg-red-50 hover:text-red-500 transition-colors shrink-0"
            title="Çıkış Yap"
          >
             <HiOutlineLogout size={20} />
          </Button>
        </div>
      </div>
    </div>
  );
};

export default SidebarContent;
