"use client"
import axios from 'axios';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import React, { useContext, useEffect, useState } from 'react'
import PastSensorList from './PastSensorsForReport';
import WorkersPastSensorList from './PastSensorsForReport';
import { DateContext } from '@/context/DateContext';
import { SourceContext } from '@/context/SourceContext';
import PastSensorsForReport from './PastSensorsForReport';
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
const PastSensorsForReportWrapper = ({session} : {session : string | undefined}) => {
  const [sensorListData,setSensorListData] = useState<SensorSessionWithser[]>([])
    const { date, setDate } = useContext(DateContext);

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
    <PastSensorsForReport session={session} sensorListData={sensorListData} />

  )
}

export default PastSensorsForReportWrapper