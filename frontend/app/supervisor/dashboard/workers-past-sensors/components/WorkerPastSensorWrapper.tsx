"use client"
import axios from 'axios';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import React, { useEffect, useState } from 'react'
import PastSensorList from './WorkersPastSensorList';
import WorkersPastSensorList from './WorkersPastSensorList';
export interface SensorSessionWithser {
  sensorsid:number
  sensorsessionsid: number;
  sensorName: string;
  note: string;
  finalStatus: string | null;
  startTime: string; // ISO string formatı
  completedTime: string; // ISO string formatı
  userid: number;
  firstName: string;
  surName: string;
  role: 'WORKER' | 'SUPERVIZOR' | string; // ihtiyaca göre genişletilebilir
}
const WorkerPastSensorWrapper = ({session} : {session : RequestCookie}) => {
  const [sensorListData,setSensorListData] = useState<SensorSessionWithser[]>([])
  useEffect(() => {
    axios.get("http://localhost:8080/superVizorSensors/getWorkersPastSensors",      {
        headers: {
          'Authorization': `Bearer ${session}`,
          'Content-Type': 'application/json',
        },
      })
    .then((res) => setSensorListData(res.data))
    .catch((err) => console.log(err));
  }, []);
  
  return (
    <WorkersPastSensorList session={session} sensorListData={sensorListData} />
  )
}

export default WorkerPastSensorWrapper