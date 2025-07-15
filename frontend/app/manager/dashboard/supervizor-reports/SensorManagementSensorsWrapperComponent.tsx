"use client"
import React, { useEffect, useState } from "react";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import axios from "axios";
import { useFetchAllSuperVizors } from "@/hooks/useFetchAllSuperVizors";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import Heading from "./Heading";
import PdfReportSensorList from "./PdfReportSensorList";
import { PdfReport } from "../../types/types";

export interface SensorList {
  sensorName: string;
  status: string;
  colorCode: string;
  latitude: number;
  longitude: number;
  currentSensorSession:
    | {
        id: number;
        sensor: {
          id: number;
          sensorName: string;
          status: string;
          installationDate: string;
        };
        startTime: string;
        completedTime: null;
        note: null;
      }
    | undefined;
}

export interface SensorListForManagerUse {
  id: number;
  sensorName: string;
  status: string;
  color_code: string;
  latitude: number;
  longitude: number;
  installationDate: string; 
  lastUpdatedAt: string | null;
  currentSensorSession:
    | {
         id: number;
  sensorName: string;
  firstName: string;
  surName: string;
  online: boolean;
      }
    | undefined;
      imageResponseDTO:  {
  name: string;
  type: string;
  base64Image: string | undefined;
};
}




export interface TaskSensorWithTask {
  id: number;
  taskSensors: {
    id: number;
    sensorName: string;
    status:string;
    color_code: string;
    latitude: number;
    longitude: number;
    currentSensorSession: {
      id: number;
      sensorName: string;
      displayName: string;
      color_code: string;
      note: string | null;
      startTime: string;
      completedTime: string | null;
      latitude: number;
      longitude: number;
    };
  };
  superVizorDescription: string;
  superVizorDeadline: string; // ISO 8601 format
  assignedBy: {
    id: number;
    firstName: string;
    surName: string;
  };
  workerArriving: string | null;
  workerArrived: string | null;
    worker_on_road_note: string;

}
const SensorManagementSensorsWrapperComponent = ({session } : {session:RequestCookie | undefined }) => {
  

  const [sensorListData,setSensorListData] = useState<PdfReport[]>()

     const { superVizorList,  errorForSupervizor } = useFetchAllSuperVizors(session);

 const [sensorId,setSensorId] = useState(1)
 const [filter,setFilter] = useState  (3)



 useEffect(() => {
    axios.get(`http://localhost:8080/manager/getAllSupervizorPdfReport/${filter}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) =>setSensorListData(res.data))
    .catch((err) => {
  console.log("Sensör verisi alınamadı:", err);
  setSensorListData([]);
  })
  }, [filter]);
  console.log(filter);
  /* const [sensorDataInduvual,setSensorDataInduvual] = useState()
  useEffect(() => {
    axios.get(`http://localhost:8080/sensors/sensormanagement/${filter}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setSensorDataInduvual(res.data))
 .catch((err) => {
  console.log("Sensör verisi alınamadı:", err);
  setSensorDataInduvual(null); // fallback
  })
  }, [sensorId]); */

  return (
    <>
    <div className="relative w-full h-fit flex flex-row justify-start items-start gap-[20px]  pt-[20px]  max-xl:flex-col  ">
      <div className="relative w-[100%] h-fit flex flex-col justify-start items-start max-xl:w-full gap-[15px] ">
       { 
  <Heading
title={"Tüm Raporlar"}
description={"Müdür olarak Süpervizörlerin verdikleri raporlara tıklayarak bunlarla ilgili bilgileri bulabilir ve supervizor ismine göre pdf raporlarını görebilirsiniz."}
/> 
  }

        <Select onValueChange={(e) => setFilter(e)} value={filter} >
      <h2 className=" text-black mb-[10px]">Sensor Raporları İçin   Supervizor Seçiniz</h2>
      <SelectTrigger className="text-black">
        <SelectValue  placeholder={`Sensor Status Filtresi seçiniz`} />
      </SelectTrigger>
      <SelectContent className="bg-white">

        {superVizorList.map((role,c) => (
          <SelectItem key={c} value={role.id} className="text-black">
            {role.firstName}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
    {sensorListData?.length != 0 ? 
       <PdfReportSensorList
     filter ={ filter}
       sensorListData= {sensorListData } 
       session = {session}
     />  :
     <div className="w-full h-[400px] p-[10px] bg-gray-400 items-center justify-center flex">
      <h2>Supervizore Dair Sensor Raporu bulunmamaktadır</h2>
      
     </div>
     }
  
      </div>
    </div>
    </>
  );
};

export default SensorManagementSensorsWrapperComponent;
