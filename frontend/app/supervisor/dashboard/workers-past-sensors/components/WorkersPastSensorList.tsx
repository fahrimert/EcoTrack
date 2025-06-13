"use client"
import React from 'react'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import { columns } from './columns'
import { GroupedSensorData, SensorSessionWithser } from '@/app/supervisor/superVizorDataTypes/types'
import { CustomDataTable } from '@/components/ui/CustomDataTable'


const WorkersPastSensorList = ({session , sensorListData }: {session:RequestCookie | undefined, sensorListData:SensorSessionWithser[]| undefined}) => {


  const groupedSensors = sensorListData?.reduce((acc, current) => {

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
         <CustomDataTable
             
             searchKey="sensorName"
             columns={columns}
             data={groupedSensors || []}
       
        />  

  </div>

  )
}

export default WorkersPastSensorList