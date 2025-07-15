"use client"
import { averageTaskMinutesChartData } from '@/app/supervisor/superVizorDataTypes/types';
import axios from 'axios';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import React, {  useEffect, useState } from 'react';
import { ScatterChart, Scatter, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';


export const ScatterChartt = ({session} : {session : RequestCookie| undefined }) => {

      const [userName, setUsername] = useState(1);

  const [durations, setDurations] = useState<
    Record<string, { formatted: string; rawMinutes: number }>
  >({});

 const [dataForAverageTaskMinutesChart,setDataForAverageTaskMinutesChart] = useState<averageTaskMinutesChartData>()

    console.log(session);
  useEffect(() => {

    axios.get(`http://localhost:8080/manager/getAverageTaskMinsOfLastMonth`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) =>
    
    setDataForAverageTaskMinutesChart(res.data))
    .catch((err) => console.log(err));
  }, [session?.value]);
  console.log(session);
  useEffect(() => {
    if (!userName) return;
    axios
      .get(
        `http://localhost:8080/superVizorSensors/getSensorSessionsOfLastMonth/${userName}`,
        {
          headers: { Authorization: `Bearer ${session?.value}` },
          withCredentials: true,
        }
      )
      .then((res) => setDurations(res.data))
      .catch((err) => console.log(err));
  }, [userName,session?.value]);

  console.log(dataForAverageTaskMinutesChart);

  const transformedDataForScatterChart = dataForAverageTaskMinutesChart?.averageChartData &&
  Object.entries(dataForAverageTaskMinutesChart.averageChartData).map(([user,minutes]) => ({
    x: user,
    y: minutes
  }))
  
   return (
      <ResponsiveContainer width="100%" height={400}>
        <ScatterChart
          margin={{
            top: 20,
            right: 20,
            bottom: 20,
            left: 20,
          }}
        >
          <CartesianGrid />
          <XAxis type="category" dataKey="x" name="Worker"  />
          <YAxis type="number" dataKey="y" name="Avg Task Completion Time (min) "  />
          <Tooltip cursor={{ strokeDasharray: '3 3' }} />
          <Scatter name="Average Task Completion Time" data={transformedDataForScatterChart} fill="#8884d8" />
        </ScatterChart>
      </ResponsiveContainer>
    );
}

export default ScatterChart


