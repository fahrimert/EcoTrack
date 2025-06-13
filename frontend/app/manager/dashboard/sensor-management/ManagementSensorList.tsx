"use client";
import React, { useEffect, useState } from "react";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Client, over } from "stompjs";
import { UserProfilea } from "@/app/supervisor/superVizorDataTypes/types";
import Heading from "@/app/supervisor/dashboard/workers-past-sensors/components/Heading";
import ManagementSensor from "./ManagementSensor";
import { SensorListForManagerUse } from "./SensorManagementSensorsWrapperComponent";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

const ManagementSensorList = ({
  stasusesData,
  session,
  sensorListData,
  userProfile,
}: {
  stasusesData   :[ 'ACTIVE', 'FAULTY', 'IN_REPAIR', 'SOLVED' ]
  session: RequestCookie;
  sensorListData: SensorListForManagerUse[] | undefined;
  userProfile: UserProfilea | undefined;
}) => {
    const [sensorListDataFiltered,setSensorListDataFiltered] = useState<SensorListForManagerUse[]>()
  
const userBasedsensor = sensorListData?.map((g) => {
  if (!g.currentSensorSession || !userProfile?.sensorSessions || userProfile.sensorSessions.length === 0) {
    return false;
  }
  return g.currentSensorSession.id === userProfile.sensorSessions[0].id;
});
  const customSensorListData = sensorListData?.map((sensor) => {
    const isUserSensor =   userProfile?.sensorSessions ? userProfile?.sensorSessions.some(
      (session) => session.id === sensor.currentSensorSession?.id
    ) : null;

    return {
      ...sensor,
      userBasedSensor: isUserSensor, 
    };
  });


        let stompClient: Client;
   

         const [filter,setFilter] = useState  ("")

         useEffect(() => {
    if (filter) {
      setSensorListDataFiltered(sensorListData?.filter(a => a.status === filter));
    } if (!filter || filter == "Hepsi") {
      setSensorListDataFiltered(sensorListData);
    }
  }, [filter, sensorListData?.map((g) => g.imageResponseDTO)]);

  return (<>
     
       <ScrollArea className="h-screen  w-full flex flex-col">
        <div className="w-full h-fit flex flex-col gap-[20px]">

   { 
  <Heading
title={"Tüm Sensörler"}
description={"Müdür olarak sensörler arasında güncelleme , yeni sensör ekleme, Sensör silme ve Sensörü arşive yükleme işlemlerini yapabiliriz."}
/> 
  }

        <Select onValueChange={(e) => setFilter(e)} value={filter} >
      <h2 className=" text-black mb-[10px]">Status Seçiniz</h2>
      <SelectTrigger className="text-black">
        <SelectValue  placeholder={`Sensor Status Filtresi seçiniz`} />
      </SelectTrigger>
      <SelectContent className="bg-white">
        {stasusesData.map((role,c) => (
          <SelectItem key={c} value={role} className="text-black">
            {role}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>

      <div className=" w-full h-fit grid grid-cols-3  items-center    gap-[10px] rounded-[30px] ">
        {
        sensorListDataFiltered?.map((sensors) => (
          <ManagementSensor
            sensors={sensors}
            session={ session}
          />
        ))}
      </div>
        </div>

    </ScrollArea>
    </>
 
  );
};

export default ManagementSensorList;
