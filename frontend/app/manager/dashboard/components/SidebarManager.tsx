"use client";
import React, { useEffect, useState } from "react";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { HiOutlineLogout } from "react-icons/hi";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { BsListTask } from "react-icons/bs";
import {  MdOutlineWaterDrop } from "react-icons/md";
import { RiDashboardHorizontalFill, RiTaskLine } from "react-icons/ri";
import Link from "next/link";
import { MdGroups2 } from "react-icons/md";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { logOut } from "@/app/actions/authActions/signout";
import { IoAnalyticsSharp } from "react-icons/io5";
import { useUserProfile } from "@/hooks/useUserProfile";
import toast from "react-hot-toast";

interface NewSidebar  {
  children: React.ReactNode;
  session : RequestCookie | undefined
}

const SidebarManager:React.FC<NewSidebar> = ({children,session} ) => {

  const path = usePathname();
  const [sidebarw,setSidebarw] = useState(true)
  const { userProfile, loading, error } = useUserProfile(session);

  const handleLogout = async () => {
   
     try {  
          
    const logout =   await logOut()
    if (logout?.serverError) {
      toast.error(logout.serverError);
      console.log("Toast Error Triggered:",logout.serverError);
      
    }
     } catch (error) {
   if (error instanceof Error) {
  console.log(error.message);
} else {
  console.log(error);
}
      
     }
    }
  return (
    /* bi sıkıntı olursa burdaki ilk divdeki h-screenda sıkıntı var  */
    <div className=" w-full h-full flex flex-col justify-start items-center bg-[#f1f0ee] ">
      <div className=" w-full h-fit flex flex-row justify-center items-start max-xl:flex-col  bg-[#f1f0ee]  ">
        <div className={cn(` sticky  top-0 ${sidebarw ? "w-[23%]" : "w-fit"}   ${sidebarw ? "h-screen" : "h-fit"}  flex flex-row justify-center items-start    max-xl:hidden  max-xl:h-fit   `)} >
    
        <div className={cn(`w-full h-full flex flex-col justify-between items-center  gap-[30px]  bg-[#f1f0ee] mt-[5px] mb-[5px] 
  bg-gradient-to-b from-[#f1f0ee] to-[#e6e5e3]
  shadow-[inset_-1px_0_0_rgba(0,0,0,0.1),inset_1px_0_0_rgba(255,255,255,0.3)]
  border-r border-[#d0d7de]/50  border-slate-400  pt-[10px] pl-[5px] gap-[15px] ${!sidebarw ? "hidden" : "visible"} `)}>
        <div className=" w-full h-[200px] pr-[5px] pl-[5px] gap-[20px]">

            <div className="relative w-full h-fit flex flex-row justify-between items-center gap-[10px]  ">
              <Link href={"/dashboard"}>
                <h2 className=" w-full h-fit  text-[24px] items-end justify-center text-[#7f9f9a]">
                <MdOutlineWaterDrop/>
                </h2>
              </Link>
 
            </div>
            <div className=" relative w-full h-full flex flex-col justify-center items-start gap-[10px]   ">
               
              <Link
                href={"/manager/dashboard/user-management"}
                className={cn(
                  "relative w-full h-fit flex flex-row justify-start items-center hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200" ,

                  path === "/manager/dashboard/user-management" &&
                    " text-black dark:text-black  bg-[#6c6f8542] rounded-[5px] "
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 h ">
                  <BsListTask size={20} color="black" />

                     <h2>Kullanıcı Yönetimi</h2>
                  </div>
                </div>
              </Link>

              <Link
                href={"/manager/dashboard/sensor-management"}
                className={cn(
                  "relative w-full h-fit flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200 ",

                  path === "/manager/dashboard/sensor-management" &&
                    " text-white dark:text-black  bg-[#6c6f8542] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <MdGroups2 size={20} color="black" />
                     <h2 >Sensor Yönetimi</h2>
               </div>
                </div>
              </Link>
              <Link
                href={"/manager/dashboard/supervizor-reports"}
                className={cn(
                  "relative w-full h-fit flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200 ",

                  path === "/manager/dashboard/supervizor-reports" &&
                    " text-white dark:text-black  bg-[#6c6f8542] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <IoAnalyticsSharp size={20} color="black" />
                     <h2 >Rapor Sayfası</h2>
               </div>
                </div>
              </Link>

        <Link
                href={"/management/dashboard/annonucements"}
                className={cn(
                  "relative w-full h-fit flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200 ",

                  path === "/management/dashboard/annonucements" &&
                    " text-white dark:text-black  bg-[#6c6f8542] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <RiTaskLine size={20} color="black" />
                     <h2 >Duyuru Sayfası</h2>
               </div>
                </div>
              </Link>
      
            
  
            </div>
        </div>

              <div className="w-full h-fit gap-[10px] flex flex-col pr-[10px]">
              <div className="relative h-[40px] w-full flex flex-row justify-start items-center  border-l-[2px] text-wrap flex-wrap  px-[5px] gap-[5px] bg-[#6c6f8542]">
              <div className=" w-[20px] h-[20px] rounded-[15px]  border-[1px] border-slate-400 flex items-center justify-center bg-[#6c6f8542] ">
<h2 className=" text-white">
{userProfile?.firstName[0]}


</h2>
              </div>
                <h2 className=" w-fit relative  text-white text-[16px] flex justify-start items-start text-wrap flex-wrap">
                {userProfile?.firstName}

                </h2>
                
              </div>
              <Button
                onClick={() => {handleLogout()}}
                className="w-full  h-fit justify-start items-center flex flex-row px-[5px] gap-[10px] bg-[#6c6f8542]"
              >
                <h2 className="w-fit h-fit text-white">Çıkış Yapın</h2>

                <HiOutlineLogout color="white" />
              </Button>

              </div>
          </div>
          <Button onClick={() => {setSidebarw(!sidebarw)}} className="w-fit h-fit pt-[20px] pl-[10px]">
      <RiDashboardHorizontalFill size={40} color="black"  />

      </Button>

        </div>
        <div className=" relative w-fit h-fit flex flex-col justify-start items-start gap-[20px]    ">
  <div className=" relative w-full h-fit flex flex-row justify-start items-start gap-[10px]   border-[#E2E9E8] pb-[5px]   ">
    <Sheet  >
      <SheetTrigger className=" w-full h-fit  visible  xl:hidden  justify-start  ">
        <RiDashboardHorizontalFill size={40} color="black"   />
      </SheetTrigger>
      <SheetContent className="w-full" side={"left"}  >
      <div className={cn(`   top-0 w-full h-screen  flex flex-row justify-center items-start   `)} >
    
     <div className={cn(`w-full h-full flex flex-col justify-between items-center  gap-[30px]  bg-[#f1f0ee] mt-[5px] mb-[5px] 
  bg-gradient-to-b from-[#f1f0ee] to-[#e6e5e3]
  shadow-[inset_-1px_0_0_rgba(0,0,0,0.1),inset_1px_0_0_rgba(255,255,255,0.3)]
  border-r border-[#d0d7de]/50  border-slate-400  pt-[10px] pl-[5px] gap-[15px] ${!sidebarw ? "hidden" : "visible"} `)}>
        <div className=" w-full h-[200px] pr-[5px] pl-[5px] gap-[20px]">

            <div className="relative w-full h-fit flex flex-row justify-between items-center gap-[10px]  ">
              <Link href={"/dashboard"}>
                <h2 className=" w-full h-fit  text-[24px] items-end justify-center text-[#7f9f9a]">
                <MdOutlineWaterDrop/>
                </h2>
              </Link>
 
            </div>
           <div className=" relative w-full h-full flex flex-col justify-center items-start gap-[10px]   ">
               
              <Link
                href={"/manager/dashboard/user-management"}
                className={cn(
                  "relative w-full h-fit flex flex-row justify-start items-center hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200" ,

                  path === "/manager/dashboard/user-management" &&
                    " text-black dark:text-black  bg-[#6c6f8542] rounded-[5px] "
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 h ">
                  <BsListTask size={20} color="black" />

                     <h2>Kullanıcı Yönetimi</h2>
                  </div>
                </div>
              </Link>

              <Link
                href={"/manager/dashboard/sensor-management"}
                className={cn(
                  "relative w-full h-fit flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200 ",

                  path === "/manager/dashboard/sensor-management" &&
                    " text-white dark:text-black  bg-[#6c6f8542] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <MdGroups2 size={20} color="black" />
                     <h2 >Sensor Yönetimi</h2>
               </div>
                </div>
              </Link>
              <Link
                href={"/manager/dashboard/supervizor-reports"}
                className={cn(
                  "relative w-full h-fit flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200 ",

                  path === "/manager/dashboard/supervizor-reports" &&
                    " text-white dark:text-black  bg-[#6c6f8542] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <IoAnalyticsSharp size={20} color="black" />
                     <h2 >Rapor Sayfası</h2>
               </div>
                </div>
              </Link>

        <Link
                href={"/management/dashboard/annonucements"}
                className={cn(
                  "relative w-full h-fit flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200 ",

                  path === "/management/dashboard/annonucements" &&
                    " text-white dark:text-black  bg-[#6c6f8542] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <RiTaskLine size={20} color="black" />
                     <h2 >Duyuru Sayfası</h2>
               </div>
                </div>
              </Link>
      
            
  
            </div>
        </div>

              <div className="w-full h-fit gap-[10px] flex flex-col pr-[10px]">
              <div className="relative h-[40px] w-full flex flex-row justify-start items-center  border-l-[2px] text-wrap flex-wrap  px-[5px] gap-[5px] bg-[#6c6f8542]">
              <div className=" w-[20px] h-[20px] rounded-[15px]  border-[1px] border-slate-400 flex items-center justify-center bg-[#6c6f8542] ">
<h2 className=" text-white">
{userProfile?.firstName[0]}


</h2>
              </div>
                <h2 className=" w-fit relative  text-white text-[16px] flex justify-start items-start text-wrap flex-wrap">
                {userProfile?.firstName}

                </h2>
                
              </div>
              <Button
                onClick={() => {handleLogout()}}
                className="w-full  h-fit justify-start items-center flex flex-row px-[5px] gap-[10px] bg-[#6c6f8542]"
              >
                <h2 className="w-fit h-fit text-white">Çıkış Yapın</h2>

                <HiOutlineLogout color="white" />
              </Button>

              </div>
          </div>
      <Button onClick={() => {setSidebarw(!sidebarw)}} className="w-fit h-fit pt-[20px] pl-[10px]">
  <RiDashboardHorizontalFill size={40} color="black"  />

  </Button>

    </div>
      </SheetContent>
    </Sheet>
 

  </div>

</div>  
        <div className="relative w-full h-fit flex flex-col justify-center items-center gap-[10px]  ">
    
      {children}  
        </div>
      </div>
    </div>
  );
};

export default SidebarManager;
