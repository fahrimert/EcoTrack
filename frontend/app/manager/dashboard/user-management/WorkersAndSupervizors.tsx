"use client";
import React, { useEffect, useState } from "react";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { useAllWorkerAndSupervizor } from "@/hooks/useAllWorkerAndSupervizor";
import SingleUserComponent from "./SingleUserComponent";
import {
  Dialog,
  DialogContent,
  DialogTrigger,
} from "@/components/ui/dialog"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import LeaderTableForWorker from "./LeaderTableForWorker";
import LeaderTableForSupervizor from "./LeaderTableForSupervizor";

const WorkersAndSupervizors = ({session} : {session :  RequestCookie | undefined}) => {
         const [filter,setFilter] = useState("")


  //burda tüm userları gösterecez sadece bunu eşleşenleri online diye gösterecez onu da backgroundu yeşil yaparız

  const { userAndSupervizor,  error } = useAllWorkerAndSupervizor(session);
  

  const [usersAndSupervizors,setUsersAndSupervizors] = useState(userAndSupervizor)
  useEffect(() => {
    if (filter) {
      setUsersAndSupervizors(userAndSupervizor.filter((a) => a.role === filter));
    } if (!filter || filter == "Hepsi") {
      setUsersAndSupervizors(userAndSupervizor); 
    }
  }, [filter, userAndSupervizor]);



  const [openLeaderTableDialog,setOpenLeaderTableDialog] = useState(false)

  return (
    <div className="w-full h-fit flex flex-row gap-[10px] ">

<div className="flex flex-col w-[25%] h-screen bg-[#f1f0ee] p-[10px]">

   
         <div className=" bg-[#edecea] w-full h-fit flex flex-col my-[10px] gap-[10px]  text-black">
<div className="w-full h-full  ">

       <Dialog  >
  <DialogTrigger  className="h-full w-full items-start flex flex-col justify-start rounded-[10px] bg-white py-[10px] px-[3px] gap-[10px]">
    
     Kullanıcı Ekleyin
                      
                      </DialogTrigger >
  <DialogContent  className="bg-white w-fit items-center justify-center flex">
   <div>
    {/* kullanıcı ekleme yapıcaz */}
{/*     <LeaderTableForWorker session={session}/>
 */}   </div>
  </DialogContent>
</Dialog>
</div>
<div className="w-fit h-full  ">

       <Dialog open = {openLeaderTableDialog} onOpenChange={setOpenLeaderTableDialog} >
  <DialogTrigger  className="h-full w-full items-center flex flex-col justify-center rounded-[10px] bg-white py-[10px] px-[3px] gap-[10px]">
    
     İşçi Lider Tablosuna Bakınız 
                      
                      </DialogTrigger >
  <DialogContent  className="bg-white w-fit items-center justify-center flex">
   <div>
    <LeaderTableForWorker session={session}/>
   </div>
  </DialogContent>
</Dialog>
</div>
<div className="w-full h-full  ">

       <Dialog  >
  <DialogTrigger  className="h-full w-full items-center flex flex-col justify-center rounded-[10px] bg-white py-[10px] px-[3px]  gap-[10px]">
    
     Supervizor Lider Tablosuna Bakınız 
                      
                      </DialogTrigger >
  <DialogContent  className="bg-white w-fit items-center justify-center flex">
   <div>
    <LeaderTableForSupervizor session={session}/>

   </div>
  </DialogContent>
</Dialog>
</div>

         </div>
</div>
      <div className="w-full h-fit flex flex-col">
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
      <div className=" w-full h-fit grid grid-cols-3  items-center    gap-[10px] rounded-[30px] ">
        
          <>
            {usersAndSupervizors.map((c) => (
              <SingleUserComponent user={c} session = {session} />
            ))}
          </>
      </div>
        </div>   

    </div>
  );
};

export default WorkersAndSupervizors;
 