"use client"

import axios from 'axios';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import React, { useEffect, useState } from 'react'
import PastSensorList from './PastSensorList';

const PastSensorWrapper = ({session} : {session : RequestCookie}) => {
  const [sensorListData,setSensorListData] = useState()
  useEffect(() => {
    axios.get("http://localhost:8080/worker/past-sensors",      {
        headers: {
          'Authorization': `Bearer ${session}`,
          'Content-Type': 'application/json',
        },
      })
    .then((res) => setSensorListData(res.data))
    .catch((err) => console.log(err));
  }, []);

  console.log(sensorListData);
  return (
    <PastSensorList session={session} sensorListData={sensorListData} />
  )
}

export default PastSensorWrapper