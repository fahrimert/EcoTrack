"use client"
import React, { useContext } from 'react'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import { DataTable } from '@/components/ui/DataTable'
import { DateContext } from '@/context/DateContext'
import { GroupedSensorDataOnPDFReport, SensorSessionWithser } from '@/app/supervisor/superVizorDataTypes/types'
import { columns } from './detailComponentsForPdfSheet/columns'


const PastSensorsForReport = ({session , sensorListData }: {session:RequestCookie | undefined, sensorListData:SensorSessionWithser[]| undefined}) => {
    const { date, setDate } = useContext(DateContext);
     const defaultList = sensorListData?.filter((d) => new Date(d.startTime).getDate() == date )

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
  }, [] as GroupedSensorDataOnPDFReport[]);

  console.log(defaultListt);
  return (
    <div className=" w-full h-fit items-center justify-center   p-[10px]   gap-[5px] rounded-[30px]">

                                
         <DataTable
             
             searchKey="sensorName"
             columns={columns}
             data={defaultListt || []}
       
        />  

  </div>

  )
}

export default PastSensorsForReport