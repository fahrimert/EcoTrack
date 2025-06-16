"use client"
import axios from 'axios';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import React, { useEffect, useState } from 'react'
import WorkersPastSensorList from './WorkersPastSensorList';
import { SensorSessionWithser } from '@/app/supervisor/superVizorDataTypes/types';

const WorkerPastSensorWrapper = ({session} : {session : RequestCookie | undefined}) => {
  const [sensorListData,setSensorListData] = useState<SensorSessionWithser[]>([])
  useEffect(() => {
    axios.get("http://localhost:8080/superVizor/getWorkersPastSensors",{
        headers: {
          'Authorization': `Bearer ${session?.value}`,
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