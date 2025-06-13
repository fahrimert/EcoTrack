"use client"
import axios from 'axios';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import React, {  useEffect, useState } from 'react'
import PastSensorsForReport from './PastSensorsForReport';
import { SensorSessionWithser } from '@/app/supervisor/superVizorDataTypes/types';

const PastSensorsForReportWrapper = ({session} : {session : RequestCookie | undefined}) => {
  const [sensorListData,setSensorListData] = useState<SensorSessionWithser[]>([])
  
  useEffect(() => {
    axios.get("http://localhost:8080/superVizorSensors/getWorkersPastSensors",      {
        headers: {
          'Authorization': `Bearer ${session?.value}`,
          'Content-Type': 'application/json',
        },
      })
    .then((res) => setSensorListData(res.data))
    .catch((err) => console.log(err));
  }, []);

  return (
    <PastSensorsForReport session={session} sensorListData={sensorListData} />

  )
}

export default PastSensorsForReportWrapper