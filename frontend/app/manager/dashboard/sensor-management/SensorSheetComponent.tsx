  "use client"
  
import { managerDeactivateUser } from "@/app/actions/userActions/managerDeactivateUser";
import { managerDeleteUserAction } from "@/app/actions/userActions/managerDeleteUserAction";
import { UserAndSupervizorsDTO } from "@/app/supervisor/superVizorDataTypes/types";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogTrigger } from "@/components/ui/dialog";
import { Separator } from "@/components/ui/separator";
import { useUserProfile } from "@/hooks/useUserProfile";
import axios from "axios";
import { format } from "date-fns";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { useEffect, useState } from "react";
import { SensorListForManagerUse } from "./SensorManagementSensorsWrapperComponent";
import { managerDeleteSensorAction } from "@/app/actions/sensorActions/manageDeleteSensorAction";

  const SensorSheetComponent = ({
    sensors,
    open,
  setOpen,
  session,
  setOpenMainDialog
  } : {
open:boolean,
  sensors :   SensorListForManagerUse,
  session: RequestCookie | undefined,
    setOpen: (value: boolean) => void;
    setOpenMainDialog: (value: boolean) => void;

  } , ) => {

  const [address, setAddress] = useState("");
   const { userProfile, loading, error } = useUserProfile(session);
    
   console.log(sensors);

    useEffect(() => {
    const fetchAddress = async () => {
      const latitude = sensors.latitude;
      const longitude = sensors.longitude;
      try {
        const res = await fetch(
          `https://nominatim.openstreetmap.org/reverse?lat=${latitude}&lon=${longitude}&format=json`,
          {
            headers: {
              "User-Agent": "YourAppName/1.0 (your@email.com)",
            },
          }
        );

        const data = await res.json();
        setAddress(data.display_name);
      } catch (error) {
        console.error("Reverse geocoding failed:", error);
        setAddress("Konum alınamadı.");
      } 
    };

    fetchAddress();
  }, []); 

  const handleDelete = async (id:string) => {
        try {
          const user = await managerDeleteSensorAction(id);
          setOpen(false)
          setOpenMainDialog(false)
          console.log(user);
        } catch (error) {
          console.log(error);
        }
  }
  
  
  const [deactivateOpen,setDeactivateOpen] = useState(false)
  const handleDeactivateUser = async (id: string) => {
        try {
          const userDeactivate = await managerDeactivateUser(id);
          setDeactivateOpen(false)
          console.log(userDeactivate);
        } catch (error) {
          console.log(error);
        }
  }
  console.log(sensors);
    return (
      <div className='relative  w-full h-full flex flex-col justify-start items-start gap-[5px] p-[10px]  '>
                      <h2 className={`w-fit h-fit  text-black text-[24px] leading-[24px]`}>Sensör Raporu</h2>

    <div className="w-full  h-fit flex flex-col justify-start items-start">
     <div className=" w-full  px-[5px] flex flex-col py-[10px] gap-[10px] ">
          <div className=" w-full flex flex-row items-center  justify-between  gap-[10px]">
      
       <div  className=" w-full flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
  <h2 className="w-full text-[16px] text-black">Kullanıcı İsmi : </h2>
     {sensors.sensorName ? (
  <h2 className="w-full text-black text-[14px]">
        {sensors.sensorName}
    <Separator/>

    </h2>
) : (
  <div className="w-[100px] h-[100px] flex items-center justify-center bg-gray-100 text-sm text-gray-500">
İsim yok  </div>
)} 
       </div>

    
  </div>

   
    <div className=" w-full h-fit flex flex-row items-start  gap-[10px] border-b-[1px]">
  <h2 className="w-full  text-[14px] text-black">Sensör Son Kaydedilen Konumu: </h2>
         <h2 className="w-full text-black  text-[14px]">
        {address}
    <Separator/>

    </h2> 
  </div>
    <div className=" w-full h-fit flex flex-row items-start  gap-[10px] border-b-[1px]">
  <h2 className="w-full  text-[14px] text-black">Sensör Kurulma Tarihi: </h2>
         <h2 className="w-full text-black  text-[14px]">
       {
        format(new Date(sensors.installationDate), 'dd MMMM yyyy HH:mm')}
     
    <Separator/>
    </h2> 
  </div>

    <div className=" w-full h-fit flex flex-row items-start  gap-[10px] border-b-[1px]">
  <h2 className="w-full  text-[14px] text-black">Sensöre Ait Şuanki Task Harici Çözen Kişi: </h2>
         <h2 className="w-full text-black  text-[14px]">
       {sensors.currentSensorSession?.firstName ? sensors.currentSensorSession.firstName : "Anlık Çözüm Yok"}
     
    <Separator/>
    </h2> 
  </div>
    <div className=" w-full h-fit flex flex-row items-start  gap-[10px] border-b-[1px]">
  <h2 className="w-full  text-[14px] text-black">Sensörün Son Güncellenme Tarihi: </h2>
         <h2 className="w-full text-black  text-[14px]">
         {
        format(new Date(sensors.lastUpdatedAt), 'dd MMMM yyyy HH:mm')}
     
    <Separator/>
    </h2> 
  </div>


  

<div className="w-full justify-end items-end h-fit flex gap-[10px] ">

<Dialog open ={open} onOpenChange={setOpen}>
<DialogTrigger >
<div className=" flex justify-end items-center bg-red-400 w-fit p-[10px] rounded-[15px]">
  <h2 className="text-white">

Sensörü Sil
  </h2>
</div>

</DialogTrigger>

<DialogContent className="bg-white">
  <div className="gap-[5px]">

  <h2 className=" text-[24px]">Sensörü silmeye emin misiniz? </h2>
  <h2 className=" text-[16px]">Sensörü sildiğiniz zaman bu işlem bidaha geri alınamaz.</h2>
  </div>
  <Button variant={null} onClick={() => {handleDelete(sensors.id)} }  className=" flex justify-end items-center bg-red-400 w-fit p-[10px] rounded-[15px]">
  <h2 className="text-white">

Sensörü Sil
  </h2>
</Button>
</DialogContent>
</Dialog>
</div>
     </div>


 
    </div>

      </div>

    )
  }

  export default SensorSheetComponent