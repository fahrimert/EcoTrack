"use client";
import React, { useEffect, useState } from "react";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { ScrollArea } from "@/components/ui/scroll-area";
import { SensorListForManagerUse } from "./SensorManagementSensorsWrapperComponent";
import { PdfReport, PdfReportInduvual } from "../../types/types";
import ManagementSensor from "./PdfReportSensor";
import PdfReportSensor from "./PdfReportSensor";

const PdfReportSensorList = ({
  filter,
  sensorListData,
  session
}: {
  filter:number
  sensorListData: PdfReport[] | undefined;
  session:RequestCookie | undefined
}) => {
    const [sensorListDataFiltered,setSensorListDataFiltered] = useState<PdfReport[]>()
  

    
         useEffect(() => {
    if (filter) {
      setSensorListDataFiltered(sensorListData?.filter(a => a.supervizorId === filter));
    } 
  }, [filter]);



  return (<>
     
       <ScrollArea className="h-screen  w-full flex flex-col">
        <div className="w-full h-fit flex flex-col gap-[20px]">

   

      <div className=" w-full h-fit grid grid-cols-3  items-center    gap-[10px] rounded-[30px] ">
     {
        sensorListData?.map((sensor) => (
          <PdfReportSensor
            sensor={sensor}
            session = {session}
          />
        ))}
      </div>
        </div>

    </ScrollArea>
    </>
 
  );
};

export default PdfReportSensorList;
