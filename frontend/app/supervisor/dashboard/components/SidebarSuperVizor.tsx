"use client";
import React, { useEffect, useState } from "react";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { HiOutlineLogout } from "react-icons/hi";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { BsListTask } from "react-icons/bs";
import {  MdOutlineWaterDrop } from "react-icons/md";
import { RiDashboardHorizontalFill } from "react-icons/ri";
import Link from "next/link";
import { MdGroups2 } from "react-icons/md";
import toast from "react-hot-toast";
import axios from "axios";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { logOut } from "@/app/actions/authActions/signout";
import { UserProfile } from "./SensorList";

interface NewSidebar  {
  children: React.ReactNode;
  session : RequestCookie | undefined
}

const SidebarSupervizor:React.FC<NewSidebar> = ({children,session} ) => {

  const path = usePathname();
  const [open, setOpen] = useState(false);
  const [sidebarw,setSidebarw] = useState(true)
    const [userProfile,setUserProfile] = useState<UserProfile>()
  
  useEffect(() => {
    axios.get(`http://localhost:8080/user/profile/${session}`, {
      headers: { Authorization: `Bearer ${session}` },
      withCredentials: true,
    })
    .then((res) => setUserProfile(res.data))
    .catch((err) => console.log(err));
  }, []);
  const handleLogout = async () => {
   
     try {  
     const returnData = await logOut()
     toast.success(returnData.serverData!)
     } catch (error) {
    console.log(error.message);
      
     }
    }
  return (
    /* bi sıkıntı olursa burdaki ilk divdeki h-screenda sıkıntı var  */
    <div className=" w-full h-full flex flex-col justify-start items-center bg-[#f1f0ee] ">
      <div className=" w-full h-fit flex flex-row justify-center items-start max-xl:flex-col  bg-[#f1f0ee]  ">
        <div className={cn(` sticky  top-0 ${sidebarw ? "w-[23%]" : "w-fit"}   ${sidebarw ? "h-screen" : "h-fit"}  flex flex-row justify-center items-start    max-xl:hidden  max-xl:h-fit  gap-[]  `)} >
    
        <div className={cn(`w-full h-full flex flex-col justify-between items-center  gap-[30px]  bg-[#f1f0ee] mt-[5px] mb-[5px] 
  bg-gradient-to-b from-[#f1f0ee] to-[#e6e5e3]
  shadow-[inset_-1px_0_0_rgba(0,0,0,0.1),inset_1px_0_0_rgba(255,255,255,0.3)]
  border-r border-[#d0d7de]/50  border-slate-400  pt-[10px] pl-[5px] gap-[15px] ${!sidebarw ? "hidden" : "visible"} `)}>
        <div className=" w-full h-[200px] pr-[5px] pl-[5px] gap-[20px]">

            <div className="relative w-full h-fit flex flex-row justify-between items-center gap-[10px]  ">
              <Link href={"/dashboard"}>
                <h2 className=" w-full h-fit  text-[24px] items-end justify-center text-[#7f9f9a]">
                <MdOutlineWaterDrop/>
                {/* logo daha iyi gözüküyor */}
                </h2>
              </Link>
 
            </div>
            <div className=" relative w-full h-full flex flex-col justify-center items-start gap-[10px]   ">
                       <Link
                href={"/supervisor/dashboard/worker-tasks"}
                className={cn(
                  "relative w-full h-[30px] flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200 ",

                  path === "/supervisor/dashboard/worker-tasks" &&
                    " text-white dark:text-black  bg-[#6c6f8542] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <MdGroups2 size={20} color="black" />
                     <h2 >Görevler Sayfası</h2>
               </div>
                </div>
              </Link>
              <Link
                href={"/supervisor/dashboard/workers-past-sensors"}
                className={cn(
                  "relative w-full h-[30px] flex flex-row justify-start items-center hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200" ,

                  path === "/supervisor/dashboard/workers-past-sensors" &&
                    " text-black dark:text-black  bg-[#6c6f8542] rounded-[5px] "
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 h ">
                  <BsListTask size={20} color="black" />

                     <h2>İşçilerin Görev Geçmişi</h2>
                  </div>
                </div>
              </Link>

              <Link
                href={"/supervisor/dashboard/workers-calendar-reports"}
                className={cn(
                  "relative w-full h-[30px] flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200 ",

                  path === "/supervisor/dashboard/workers-calendar-reports" &&
                    " text-white dark:text-black  bg-[#6c6f8542] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <MdGroups2 size={20} color="black" />
                     <h2 >Rapor Sayfası</h2>
               </div>
                </div>
              </Link>
              <Link
                href={"/supervisor/dashboard/workers-performance-analysis-charts"}
                className={cn(
                  "relative w-full h-[30px] flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200 ",

                  path === "/supervisor/dashboard/workers-performance-analysis-charts" &&
                    " text-white dark:text-black  bg-[#6c6f8542] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <BsListTask size={20} color="black" />
                     <h2 >İşçi  Analiz  Sayfası</h2>
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
    
    <div className={cn(`w-full h-full flex flex-col justify-between items-center  gap-[30px]  bg-[#f1f0ee] 
bg-gradient-to-b from-[#f1f0ee] to-[#e6e5e3]
shadow-[inset_-1px_0_0_rgba(0,0,0,0.1),inset_1px_0_0_rgba(255,255,255,0.3)]
border-r border-[#d0d7de]/50  border-slate-400  pt-[10px] pl-[5px]  `)}>
    <div className=" w-full h-[200px] pr-[5px] pl-[5px] gap-[20px]">

        <div className="relative w-full h-fit flex flex-row justify-between items-center gap-[10px]  ">
          <Link href={"/dashboard"}>
            <h2 className=" w-full h-fit  text-[24px] items-end justify-center text-[#7f9f9a]">
            <MdOutlineWaterDrop/>
            {/* logo daha iyi gözüküyor */}
            </h2>
          </Link>

        </div>
        <div className=" relative w-full h-full flex flex-col justify-center items-start gap-[10px]   ">
     
          <Link
            href={"/dashboard/past-sensors"}
            className={cn(
              "relative w-full h-[30px] flex flex-row justify-start items-center hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200" ,

              path === "/dashboard/past-sensors" &&
                " text-black dark:text-black  bg-[#6c6f8542] rounded-[5px] "
            )}
          >
            <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
              <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 h ">
              <BsListTask size={20} color="black" />

                <h2>Görev Geçmişi</h2>
              </div>
            </div>
          </Link>

          <Link
            href={"/dashboard/ekiptakibi"}
            className={cn(
              "relative w-full h-[30px] flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200 ",

              path === "/dashboard/ekiptakibi" &&
                " text-white dark:text-black  bg-[#6c6f8542] rounded-[5px]"
            )}
          >
            <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
              <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                <MdGroups2 size={20} color="black" />
                <h2 >Ekip Takibi</h2>
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

export default SidebarSupervizor;
