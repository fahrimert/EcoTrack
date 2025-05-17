"use client"
import React, { useState } from "react";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { SourceContext } from "@/context/SourceContext";

import AssignedSensorForm from "./AssignedSensorForm";
import MapOfSingleSensor from "./MapOfSingleSensor";
import SearchUserLocation from "@/app/components/SearchComponents/SearchUserLocation";


export interface SensorData {
  data : {
  id: number;
  sensorName: string;
  status: string;
  color_code: string;
  latitude: number;
  longitude: number;
  currentSensorSession: {  id: number;
  sensorName: string;
  displayName: string;
  color_code: string;
  note: string | null;
  startTime: string; // ISO formatta tarih
  completedTime: string | null;
  latitude: number;
  longitude: number;};
  
}

}

const AssignedSensorAndMap = ({initialData , session ,stasusesData  } : {initialData: SensorData, session:RequestCookie | undefined,stasusesData : [ 'ACTIVE', 'FAULTY', 'IN_REPAIR', 'SOLVED' ]}) => {
  const [source,setSource] = useState({
    lat:39.9334,
    lng: 32.8597
  })

  
  return (
    <>
    <div className="relative w-full h-fit flex flex-row justify-start items-start gap-[20px]  pt-[20px] ">


      <SourceContext.Provider value={{source,setSource}}>
   
      <div className="relative w-[70%] h-fit flex flex-col justify-start items-start    ">
      <AssignedSensorForm initialData = {initialData} session = {session}  statuses = {stasusesData}/>
      </div>
       <div className="relative w-[50%] h-full items-center justify-center"> 
      <div className="absolute left-40 z-10 p-6  ">
        <SearchUserLocation session={session} />
      </div>
      <MapOfSingleSensor session={session}  initialData = {initialData} />
</div>
      
      </SourceContext.Provider>
    </div>
    </>
  );
};

export default AssignedSensorAndMap;
