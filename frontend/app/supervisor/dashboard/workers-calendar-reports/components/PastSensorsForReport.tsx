"use client"
import React, { useContext } from 'react'

import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'

import { DataTable } from '@/components/ui/DataTable'
import { SensorSessionWithser } from './PastSensorsForReportWrapper'
import { SourceContext } from '@/context/SourceContext'
import { DateContext } from '@/context/DateContext'
import { columns } from './columns'
interface GroupedSensorData {
  id: number;
  userİd:number
  sensorName: string;
  status: string;
  sessionkey:string
  
  username:string
  installationDate: string;
  sessions: {
    
    id: number;
        sessionkey:string,
    
    startTime: string;
    completedTime: string;
    note: string;
  }[];
}

const PastSensorsForReport = ({session , sensorListData }: {session:string, sensorListData:SensorSessionWithser[]| undefined}) => {
    const { date, setDate } = useContext(DateContext);
     const defaultList = sensorListData.filter((d) => new Date(d.startTime).getDate() == date )

  const defaultListt = defaultList?.reduce((acc, current) => {
    const existingSensor = acc.find(s => s.id === current.sensorsid);
    //genel bi data statesi oluşturayım ona göre sortlamayı o tarihdeekileri göster diye yapayım
    if (existingSensor) {
      existingSensor.sessions.push({
        id: current.sensorsessionsid,
        sessionkey:session,
        startTime: current.startTime,
        completedTime: current.completedTime,
        note: current.note
      });
    } else {
      acc.push({
        id: current.sensorsid,
        sensorName: current.sensorName,
        sessionkey:session,

        userİd:current.userid,
        username:current.firstName,
        status: current.finalStatus,
        installationDate: current.startTime,
        sessions: [{
          id: current.sensorsessionsid,
          sessionkey:session,
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
             data={defaultListt || []}
       
        />  

  </div>

  )
}

export default PastSensorsForReport