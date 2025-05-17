import React, { useContext, useEffect, useState } from "react";
import Image from "next/image";
import { cn } from "@/lib/utils";
import { UserOnlineStatusDTO } from "./OnlineUsers";

const OnlineUser = ({
    user
}: {
user :  UserOnlineStatusDTO

  
}) => {




  return (
    <>
      {
        <div
          className={cn(
             `flex flex-col   w-full h-fit  justify-center items-center rounded-[30px]  p-[10px] gap-[10px] `
          )}
        >
          <div
            className={cn(` ${user.userOnlineStatus?.isOnline  ? "bg-green-400" :  "bg-gray-50"  } rounded-[30px] flex flex-col w-fit h-fit p-[10px]   justify-start items-start  shadow-lg  hover:scale-105 duration-300 cursor-pointer ;`)} 
            
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
                Kullanıcı Soyismi:
              </h2>


              <h2 className="text-[13px] font-normal   text-white ">
                {user.surName}{" "}
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

           <div className=" flex flex-row  gap-[5px]">

              <h2 className="text-[13px] font-normal text-white   ">
              Kullanıcı Aktiflik Durumu: 
              </h2>
              <h2 className="text-[13px] font-normal text-white   ">
                {user.userOnlineStatus?.isOnline == true ? "Online"  : "Offline"}
              </h2>
        </div>
            
         
            </div>

          
          </div>
        </div>
     }
    </>
  );
};

export default OnlineUser;
