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
import { SensorSessionWithser } from './WorkerPastSensorWrapper'
interface GroupedSensorData {
  id: number;
  userİd:number
  sensorName: string;
  status: string;
  username:string
  installationDate: string;
  sessions: {
    id: number;
    startTime: string;
    completedTime: string;
    note: string;
  }[];
}

const WorkersPastSensorList = ({session , sensorListData }: {session:RequestCookie, sensorListData:SensorSessionWithser[]| undefined}) => {


  const groupedSensors = sensorListData?.reduce((acc, current) => {

    //hem userin idsinin eşit olması lazım usera göre sıralayıp ondaki sensorleri yapması lazım 
    const existingSensor = acc.find(s => s.userİd === current.userid);
    
    if (existingSensor) {
      existingSensor.sessions.push({
        id: current.sensorsessionsid,
        startTime: current.startTime,
        completedTime: current.completedTime,
        note: current.note
      });
    } else {
      acc.push({
        id: current.sensorsid,
        userİd:current.userid,
        sensorName: current.sensorName,
        username:current.firstName,
        status: current.finalStatus!,
        installationDate: current.startTime,
        sessions: [{
          id: current.sensorsessionsid,
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

export default WorkersPastSensorList