"use client";
import React, { useEffect, useState } from "react";
import OnlineUser from "./OnlineUser";
import Image from "next/image";

import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { useAllWorkerAndSupervizor } from "@/hooks/useAllWorkerAndSupervizor";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";


const OnlineUsers = ({session } :  {session : RequestCookie | undefined}) => {

  const { userAndSupervizor,  error } = useAllWorkerAndSupervizor(session);

           const [filter,setFilter] = useState("")
  
  
             const [usersAndSupervizors,setUsersAndSupervizors] = useState(userAndSupervizor)
             useEffect(() => {
               if (filter) {
                 setUsersAndSupervizors(userAndSupervizor.filter((a) => a.role === filter));
               } if (!filter || filter == "Hepsi") {
                 setUsersAndSupervizors(userAndSupervizor); 
               }
             }, [filter, userAndSupervizor]);
  return (
    <div className="w-full h-fit bg-[#c2cecb]">
      <div className="w-fit h-fit flex flex-col p-[10px] bg-white ronded-[10px] m-[10px] ">

              <Select onValueChange={(e) => setFilter(e)} value={filter} >
                         <h2 className=" text-black mb-[10px]">Rol Seçiniz</h2>
                         <SelectTrigger className="text-black">
                           <SelectValue  placeholder={`Rol Filtresi seçiniz`} />
                         </SelectTrigger>
                         <SelectContent className="bg-white">
                           {["SUPERVISOR","WORKER","Hepsi"].map((role,c) => (
                             <SelectItem key={c} value={role} className="text-black">
                               {role}
                             </SelectItem>
                           ))}
                         </SelectContent>
                       </Select>
      </div>
      <div className=" w-full h-fit  grid grid-cols-2  min-h-screen   bg-[#c2cecb]   gap-[10px] rounded-[30px] ">
           <>

            {usersAndSupervizors.map((c) => (
              <OnlineUser user={c} />
            ))}
          </>
      </div>
    </div>
  );
};

export default OnlineUsers;
