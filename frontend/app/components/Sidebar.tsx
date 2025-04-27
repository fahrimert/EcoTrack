"use client";
import React, { useState } from "react";
import {FaUserFriends } from "react-icons/fa";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { FaMessage, FaUserGroup } from "react-icons/fa6";
import { Button } from "@/components/ui/button";
import { HiOutlineLogout } from "react-icons/hi";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { BsListTask } from "react-icons/bs";
import { MdOutlineWaterDrop } from "react-icons/md";

import { RiDashboardHorizontalFill } from "react-icons/ri";
import Link from "next/link";
import { MdGroups2 } from "react-icons/md";

interface NewSidebar  {
  children: React.ReactNode;
  friendss: {
    id: string;
    email: string;
    name: string;
    password: string;
    refreshToken: string;
    isActive: boolean;
    university: string;
    major: string;
    emailToken: string;
    createdAt: Date;
    updatedAt: Date;
}[]
}

const NewSidebar:React.FC<NewSidebar> = ({children}) => {
  const path = usePathname();
  const [open, setOpen] = useState(false);

  return (
    /* bi sıkıntı olursa burdaki ilk divdeki h-screenda sıkıntı var  */
    <div className=" w-full h-screen flex flex-col justify-start items-center bg-[#f1f0ee] ">
      <div className=" w-full h-fit flex flex-row justify-center items-start max-xl:flex-col  bg-[#f1f0ee]  ">
        <div className=" sticky  top-0  w-[20%] h-screen flex flex-col justify-center items-center   max-xl:hidden max-xl:w-fit  max-xl:h-fit   ">
        <div className="w-full h-full flex flex-col justify-between items-center   bg-[#f1f0ee]
  bg-gradient-to-b from-[#f1f0ee] to-[#e6e5e3]
  shadow-[inset_-1px_0_0_rgba(0,0,0,0.1),inset_1px_0_0_rgba(255,255,255,0.3)]
  border-r border-[#d0d7de]/50  border-slate-400  pt-[20px] pl-[10px] ">
        <div className=" w-full pr-[5px] pl-[5px]">

            <div className="relative w-full h-fit flex flex-row justify-start items-center gap-[10px]  ">
              <Link href={"/dashboard"}>
                <h2 className=" w-full h-[60px]   text-[24px] items-center justify-center text-[#7f9f9a]">
                <MdOutlineWaterDrop/>
                {/* logo daha iyi gözüküyor */}
                </h2>
              </Link>
            </div>
            <div className=" relative w-full h-fit flex flex-col justify-center items-start gap-[10px]   ">
         
              <Link
                href={"/dashboard/sensors"}
                className={cn(
                  "relative w-full h-[30px] flex flex-row justify-start items-center hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px]  duration-200" ,

                  path === "/dashboard/sensors" &&
                    " text-black dark:text-black  bg-[#16171c] rounded-[5px] "
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
                    " text-white dark:text-black  bg-[#16171c] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <MdGroups2 size={20} color="black" />
                    <h2 >Ekip Takibi</h2>
                  </div>
                </div>
              </Link>

              <Link
                href={"/dashboard/profile"}
                className={cn(
                  "relative w-full h-[30px] flex flex-row justify-start items-center  hover:bg-[#6c6f8542] hover:text-white hover:rounded-[5px] duration-200 ",

                  path === "/dashboard/profile" &&
                    " text-white dark:text-black  bg-[#16171c] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <MdGroups2 size={20} color="black" />
                    <h2 className="text-18px" >Profil Bilgileri</h2>
                  </div>
                </div>
              </Link>
            
  
            </div>
        </div>

              <div className="w-full h-fit gap-[10px] flex flex-col px-[5px]">
              <div className="relative h-[40px] w-full flex flex-row justify-start items-center  border-l-[2px] text-wrap flex-wrap  px-[5px] gap-[5px] bg-[#6c6f8542]">
              <div className=" w-[20px] h-[20px] rounded-[15px]  border-[1px] border-slate-400 flex items-center justify-center bg-[#6c6f8542] ">
<h2 className=" text-white">
a

</h2>
              </div>
                <h2 className=" w-fit relative  text-white text-[16px] flex justify-start items-start text-wrap flex-wrap">
             a
                </h2>
                
              </div>
              <Button
           
                className="w-full  h-fit justify-start items-center flex flex-row px-[5px] gap-[10px]"
              >
                <h2 className="w-fit h-fit">Çıkış Yapın</h2>

                <HiOutlineLogout />
              </Button>

              </div>
          </div>
        </div>
        <div className="relative w-full h-fit flex flex-col justify-center items-center gap-[10px] pl-[40px] pr-[40px]  ">
          <div className=" relative w-full h-fit flex flex-col justify-start items-center gap-[20px]  bg-[#05050a]  visible xl:hidden ">
            <div className=" relative w-full h-fit flex flex-row justify-center items-center gap-[10px] bg-[#05050a]  border-[#E2E9E8] pb-[5px]  max-xl:justify-between ">
              <Sheet >
                <SheetTrigger className=" w-fit h-fit px-[20px] pb-[20px] pt-[40px] visible  xl:hidden ">
                  <RiDashboardHorizontalFill size={40} color="white" />
                </SheetTrigger>
                <SheetContent side={"left"} className="bg-[#05050a]">
                <div className="w-full h-full flex flex-col justify-between items-center   bg-[#05050a]    pb-[20px] pt-[40px] ">
        <div className=" w-full pr-[5px] pl-[5px]">

            <div className="relative w-full h-fit flex flex-row justify-start items-center gap-[10px] border-b-[2px]   ">
              <Link href={"/dashboard"}>
                <h2 className=" w-full h-[65px]   text-[24px] items-center justify-center text-[#7f9f9a]">
                  Gıybet
                </h2>
              </Link>
            </div>
            <div className=" relative w-full h-fit flex flex-col justify-center items-start gap-[20px] pt-[20px] pb-[20px] ">
         
              <Link
                href={"/dashboard/messages"}
                className={cn(
                  "relative w-full h-[40px] flex flex-row justify-start items-center hover:bg-[#292a33]",

                  path === "/dashboard/messages" &&
                    " text-black dark:text-black  bg-[#16171c] rounded-[5px] "
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 h ">
                  <FaMessage size={20} color="white" />

                    <h2 className="text-black">Mesajlar</h2>
                  </div>
                </div>
              </Link>

              <Link
                href={"/dashboard/chatGroups"}
                className={cn(
                  "relative w-full h-[40px] flex flex-row justify-start items-center  hover:bg-[#292a33]",

                  path === "/dashboard/chatGroups" &&
                    " text-black dark:text-black  bg-[#16171c] rounded-[5px]"
                )}
              >
                <div className="relative w-fit h-fit flex flex-row justify-center items-center gap-[10px] px-[5px]">
                  <div className="relative  w-fit h-fit flex flex-row justify-center items-center gap-[10px] p-0 ">
                    <MdGroups2 size={20} color="white" />
                    <h2 className="text-black">Gruplar</h2>
                  </div>
                </div>
              </Link>

              <Button
        
                className="w-full  h-fit justify-start items-center flex flex-row px-[5px] gap-[10px]"
              >
                <h2 className="w-fit h-fit">Çıkış Yapın</h2>

                <HiOutlineLogout />
              </Button>
            </div>
        </div>

              <div className="relative h-[40px] w-full flex flex-row justify-start items-center  border-l-[2px] text-wrap flex-wrap  px-[5px] gap-[5px] bg-[#6c6f8542] hover:bg-[#6c6f8542]">
              <div className=" w-[20px] h-[20px] rounded-[15px]   border-[1px] border-slate-400 flex items-center justify-center ">
<h2 className=" text-white">
a

</h2>
              </div>
                <h2 className=" w-fit relative  text-white text-[16px] flex justify-start items-start text-wrap flex-wrap">
               a
                </h2>
              </div>
          </div>
                </SheetContent>
              </Sheet>
           
          
            </div>
          
          </div>  
      {children}  
        </div>
      </div>
    </div>
  );
};

export default NewSidebar;
