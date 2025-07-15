"use client"
import React, { useState } from "react";
import Image from "next/image";
import { cn } from "@/lib/utils";
import { UserAndSupervizorsDTO, } from "@/app/supervisor/superVizorDataTypes/types";
import DetailsComponentOfUser from "./DetailsComponentOfUser";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";

import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Separator } from "@/components/ui/separator";
import { format } from "date-fns";

const SingleUserComponent = ({
    user,
    session 
}: {
user :  UserAndSupervizorsDTO
session : RequestCookie | undefined
  //en son bu sayfadaki bu userların tek tek detaylarının sheetini yapacaktım onu da pdf formatındaki gibi yapıcam ufak metrickler falan silmek istersek de sheetden silecez vesaire bu sayfa akaldık
}) => {

    console.log(user);

    const [open,setOpen] = useState(false);

    const [openmaindialog,setOpenMainDialog] = useState(false);

  return (
    <>
      {

              <div className="space-y-2 mt-2">
                  <div  className="p-3  border rounded-lg flex flex-col bg-gray-50 w-fit ">
               <Dialog open = {openmaindialog} onOpenChange={setOpenMainDialog} >
  <DialogTrigger  className="flex flex-col justify-between items-start gap-[10px]">
    
        <div
          className={cn(
             `flex flex-col   w-full h-fit  justify-center items-center rounded-[30px]  p-[10px] gap-[10px] `
          )}
        >
          <div
            className={cn(` rounded-[30px] flex flex-col w-fit h-fit p-[10px]   justify-start items-start  shadow-lg  hover:scale-105 duration-300 cursor-pointer ;`)} 
            
          >
            <Image
              src={"/indir.jpg"}
              alt="232"
              className={cn(
                ` w-[200px] h-[100px]  object-fit  rounded-[30px] cursor-pointer  `
              )}
              width={100}
              height={100}
            />
            <div className=" h-full w-full justify-start items-start flex flex-col p-[5px] gap-[5px]  ">
        <div className=" flex flex-row gap-[5px] w-full">
                  <h2 className="text-[13px] font-normal w-fit   text-white ">
                Kullanıcı İsmi:
              </h2>
              <h2 className="w-full text-[16px] font-normal   ">
                {user.firstName}
              </h2>
        </div>

            </div>
            <div 
      /*       style={{ backgroundColor: sensors.color_code , opacity: 0.6}} */
            
            className=" bg-[#c0ccc9]  w-full p-[5px] rounded-[5px] mb-[5px]">
        <div className=" flex flex-row gap-[5px]">
              <h2 className="text-[13px] font-normal   text-white ">
                Kullanıcı İsmi:
              </h2>


              <h2 className="text-[13px] font-normal   text-white ">
                {user.firstName}{" "}
              </h2>
        </div>
        
        <div className=" flex flex-row  gap-[5px]">

              <h2 className="text-[13px] font-normal text-white   ">
              Kullanıcı rolü:
              </h2>
              <h2 className="text-[13px] font-normal text-white   ">
                {user.role}{" "}
              </h2>
        </div>
        


            
         
            </div>

          
          </div>
        </div>
                      
                      </DialogTrigger >
  <DialogContent  className="bg-white w-fit items-center justify-center flex">
   <DetailsComponentOfUser
    open = {open}
     setOpenMainDialog = {setOpenMainDialog}
    setOpen = {setOpen} user = {user} session = {session} sessionId = {session} />
  </DialogContent>
</Dialog>
                 

                    </div>
              </div>

 
     }
    </>
  );
};

export default SingleUserComponent;
