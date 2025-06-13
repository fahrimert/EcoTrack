import { cookies } from "next/headers";
import React from "react";
import MainPageChartsComponent from "./components/MainPageChartsComponent";


 

const page = async () => {
  const session = cookies().get("session");

  
     const response = await fetch(`http://localhost:8080/superVizorSensors/getSensorsFiltersBasedStat`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${session?.value}`,
        "Content-Type": "application/json",
      },
    });

    const sensorfilterBasedStat = await response.json()


     const responsegetWorkerStats = await fetch(`http://localhost:8080/superVizorSensors/getWorkerStats`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${session?.value}`,
        "Content-Type": "application/json",
      },
    });

    const getWorkerStats = await responsegetWorkerStats.json()
         const getFaultyLocationss = await fetch(`http://localhost:8080/superVizorSensors/getFaultyLocations`, {
           method: "GET",
           headers: {
             Authorization: `Bearer ${session?.value}`,
             "Content-Type": "application/json",
           },
         });
     
         const responseOFaulty = await getFaultyLocationss.json() as 	[{
     id: string;
     latitude: number;
     longitude: number;
 }]
   
    return (
    <>
      <div className=" h-fit w-full items-center justify-center flex pt-[10px]">
        <MainPageChartsComponent 
        sensorfilterBasedStat = {sensorfilterBasedStat}
        responseOFaulty = {responseOFaulty}
        getWorkerStats = {getWorkerStats} />
      </div>
    </>
  );
};

export default page;
