"use client"
import { FullUserStats, RadarData, RadarDataForWorker, SensorTypeCount } from '@/app/supervisor/superVizorDataTypes/types';
import React, { useEffect, useState } from 'react'
import BarChart from './BarChart';
import { ScatterChartt } from './ScatterChartt';
import axios from 'axios';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import { ChartComponentForWorker } from './ChartComponent';
import { RadarChartt } from './RadarChart';
import WorkerSelect from '@/app/supervisor/dashboard/workers-performance-analysis-charts/WorkerSelect';
import { useFetchAllSuperVizors } from '@/hooks/useFetchAllSuperVizors';
import { RadarChartForWorker } from './RadarChartForWorker';
import { useFetchAllWorkers } from '@/hooks/useFetchAllWorkers';

const MainPageChartComponentOfManager =  ({session,getSupervizorTasks} : {
  session : RequestCookie | undefined
  getSupervizorTasks : FullUserStats 
  
}) => {
 
     const [dataForPie,setdataForPie] = useState<SensorTypeCount[]>([])
     
     const { superVizor,  errorForSupervizor } = useFetchAllSuperVizors(session);
     const { users,  error } = useFetchAllWorkers(session);
      useEffect(() => {
        axios.get(`http://localhost:8080/manager/getAllAssignedTaskStatusValuesForDoughnutComponent`, {
          headers: { Authorization: `Bearer ${session?.value}` },
          withCredentials: true,
        })
        .then((res) => setdataForPie(res.data))
        .catch((err) => console.log(err));
      }, []);  
    console.log(dataForPie);
const transformed = getSupervizorTasks.map((periodObj) => {
  const [periodKey, periodValue] = Object.entries(periodObj)[0];
  return {
    period: periodKey,
    labels: Object.keys(periodValue),
    data: Object.values(periodValue),
  };
});


const lastweekdata = transformed.filter((c) => c.period == 'last_week')
const lastmonthdata = transformed.filter((c) => c.period == 'last_month')
const lastdaydata = transformed.filter((c) => c.period == 'last_day')


const [userss,setUserss] = useState(1)
const [superVizorForRadar,setSuperVizorRadar] = useState(1)

   const [radarData,setRadarData] = useState<RadarData>()

    console.log(superVizorForRadar);
  useEffect(() => {

    axios.get(`http://localhost:8080/manager/getSuperVizorPropertiesForRadarChart/${superVizorForRadar}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) =>setRadarData(res.data))
    .catch((err) => console.log(err));
  }, [superVizorForRadar]);



   const [radarDataForWorker,setRadarDataForWorker] = useState<RadarDataForWorker>()

  useEffect(() => {

    axios.get(`http://localhost:8080/manager/getWorkerPropertiesForRadarChart/${userss}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) =>setRadarDataForWorker(res.data))
    .catch((err) => console.log(err));
  }, [users]);



  return (
    <div className='gap-[10px] flex flex-col w-fit '>

        
   <div  className=" bg-white w-full h-full flex justify-center items-center rounded-[20px] p-[20px] ">
         <BarChart 
         lastmonthdata = {lastmonthdata}
         lastdaydata = {lastdaydata}
        lastweekdata = {lastweekdata}
        />
     </div>
     
   <div className=" bg-white w-full h-full flex flex-col justify-start items-start rounded-[20px] p-[20px] ">
      <h1 className="text-black text-[24px]">İşçilerinizin Verilen Görevlerdeki Ortalama Çözme Süreleri</h1>

     <ScatterChartt session = {session}/>
     </div>
   <div className=" bg-white w-full  h-fit flex flex-col justify-center items-center rounded-[20px] p-[30px] ">
      <div className='w-full h-[400px] flex flex-col '>
        <WorkerSelect 
        users = {superVizor}
          onChange={e => setSuperVizorRadar(e)}
        value={superVizorForRadar}
        label="Supervizor Seçiniz"
        />
<RadarChartt 
radarData={radarData}
/> 
      </div>   
      
    <div className='w-full h-[400px] '>
     <WorkerSelect 
        users = {users}
          onChange={e => setUserss(e)}
        value={userss}
        label="Worker Seçiniz"
        />
<RadarChartForWorker 
radarData={radarDataForWorker}
/> 
      </div>
      {/* <BarChart/> */}
     </div>
   <div className=" bg-white w-full  h-full flex flex-col justify-center items-center rounded-[20px] p-[20px] ">
      <h1 className="text-black text-[24px]">Süpervizorlerinizin Verdikleri Taskların Çözülmelerine  Göre Statüs Grafiği</h1>
      <div className='w-fit h-fit '>

<ChartComponentForWorker dataForPie = {dataForPie}/> 
      </div>
      {/* <BarChart/> */}
     </div>

    </div>
  )
}

export default MainPageChartComponentOfManager