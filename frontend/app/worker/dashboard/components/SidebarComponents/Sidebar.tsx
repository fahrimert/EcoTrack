"use client";
import React, {  useState } from "react";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { HiOutlineLogout } from "react-icons/hi";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { BsListTask } from "react-icons/bs";
import {  MdChevronLeft, MdChevronRight, MdDashboard, MdMenu, MdOutlineWaterDrop, MdTask } from "react-icons/md";
import { RiDashboardHorizontalFill } from "react-icons/ri";
import Link from "next/link";
import { MdGroups2 } from "react-icons/md";
import { logOut } from "../../../../actions/authActions/signout";
import toast from "react-hot-toast";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { useUserProfile } from "@/hooks/useUserProfile";
import SidebarContent from "./SidebarContent";

 
interface NewSidebar  {
  children: React.ReactNode;
  session : RequestCookie | undefined
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
const Sidebar:React.FC<NewSidebar> = ({children,session} ) => {

  const path = usePathname();
  const [isCollapsed, setIsCollapsed] = useState(false);
  const { userProfile, loading, error } = useUserProfile(session);

  const [open, setOpen] = useState(false);
  const [sidebarw,setSidebarw] = useState(true)

const handleLogout = async () => {
    try {
      await logOut();
      toast.success("Başarıyla çıkış yapıldı.");
    } catch (error: any) {
      console.error(error);
      toast.error("Çıkış yapılırken hata oluştu.");
    }
  };



  return (
    /* bi sıkıntı olursa burdaki ilk divdeki h-screenda sıkıntı var  */
   
<div className="flex h-screen overflow-hidden bg-[#f1f0ee]">
  <aside 
        className={cn(
          "hidden md:flex flex-col h-screen sticky top-0 transition-all duration-300 ease-in-out z-40 bg-[#f8f9fa]",
          isCollapsed ? "w-[120px]" : "w-[280px]"
        )}
      >
        <SidebarContent
          userProfile={userProfile} 
          handleLogout={handleLogout} 
          currentPath={path}
          collapsed={isCollapsed}
        />
        
        <button 
          onClick={() => setIsCollapsed(!isCollapsed)}
          className="absolute -right-3 top-9 bg-white border border-gray-200 rounded-full p-1 shadow-md hover:bg-gray-50 text-gray-500 z-50"
        >
          {isCollapsed ? <MdChevronRight size={16}/> : <MdChevronLeft size={16}/>}
        </button>
      </aside>



<div className="flex-1 flex flex-col h-full overflow-hidden">
        
        <header className="md:hidden flex items-center p-4 bg-white border-b border-gray-200 h-16 shrink-0">
          <Sheet>
            <SheetTrigger asChild>
              <Button variant="ghost" size="icon">
                <MdMenu size={24} />
              </Button>
            </SheetTrigger>
            <SheetContent side="left" className="p-0 w-[280px]">
              <SidebarContent
                userProfile={userProfile}
                handleLogout={handleLogout}
                currentPath={path}
                collapsed={false} 
              />
            </SheetContent>
          </Sheet>
          <span className="ml-4 font-bold text-lg text-emerald-600">EcoTrack</span>
        </header>

        <main className="flex-1 overflow-auto p-4 md:p-6 relative">
          {children}
        </main>
      </div>
      </div>
  );
};

export default Sidebar;
