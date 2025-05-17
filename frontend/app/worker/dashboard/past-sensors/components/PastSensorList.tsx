"use client"
import React from 'react'

import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion"
import PastSensor from './PastSensor'
import { columns } from './columns'
import { DataTable } from '@/components/ui/DataTable'

export interface SensorData {
  id: number;
  sensor: {
    id: number;
    sensorName: string;
    status: "ACTIVE" | "IN_REPAIR" | "FAULTY" | "SOLVED"; // or string for more flexibility
    installationDate: string;
  };
  startTime: string;
  completedTime: string;
  note: string;
}

interface GroupedSensorData {
  id: number;
  sensorName: string;
  status: string;
  installationDate: string;
  sessions: {
    id: number;
    startTime: string;
    completedTime: string;
    note: string;
  }[];
}
const PastSensorList = ({session , sensorListData }: {session:RequestCookie, sensorListData:SensorData[]| undefined}) => {

  const groupedSensors = sensorListData?.reduce((acc, current) => {
    const existingSensor = acc.find(s => s.id === current.sensor.id);
    
    if (existingSensor) {
      existingSensor.sessions.push({
        id: current.id,
        startTime: current.startTime,
        completedTime: current.completedTime,
        note: current.note
      });
    } else {
      acc.push({
        id: current.sensor.id,
        sensorName: current.sensor.sensorName,
        status: current.sensor.status,
        installationDate: current.sensor.installationDate,
        sessions: [{
          id: current.id,
          startTime: current.startTime,
          completedTime: current.completedTime,
          note: current.note
        }]
      });
    }
    return acc;
  }, [] as GroupedSensorData[]);
  return (
    <div className=" w-full h-fit items-start justify-start   p-[10px]   gap-[5px] rounded-[30px]">
          <DataTable
      
      searchKey="sensorName"
      columns={columns}
      data={groupedSensors || []}

 />
  
  </div>

  )
}

export default PastSensorList