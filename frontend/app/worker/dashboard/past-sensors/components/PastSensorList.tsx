"use client"
import React from 'react'

import { columns } from './columns'
import { CustomDataTable } from '@/components/ui/CustomDataTable'
import { PastSensorsDto } from '@/app/worker/types/types'


//bunu kullanmıyorum
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
//bunu kullanmıyorum
export type SensorTaskDetail = {
  id: number;
  sensorName: string;
  status: 'ACTIVE' | 'INACTIVE' | string;
  installationDate: string;
  sessions: {
    id: number;
    startTime: string;
    completedTime: string;
    note: string;
  }[];
};
const PastSensorList = ({ pastSensors }: {pastSensors:PastSensorsDto[]| undefined}) => {
  console.log("PASTSENSORSSESSIONS",pastSensors[0].sessions);
  return (
    <div className=" w-full h-fit items-start justify-start   p-[10px]   gap-[5px] rounded-[30px]">
          <CustomDataTable
      
      searchKey="sensorName"
      columns={columns}
      data={pastSensors || []}

 />
  
  </div>

  )
}

export default PastSensorList