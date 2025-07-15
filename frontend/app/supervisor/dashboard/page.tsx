import { cookies } from "next/headers";
import React from "react";
import MainPageChartsComponent from "./components/MainPageChartsComponent";

 
const page = async () => {
  const session = cookies().get("session");

  
     const response = await fetch(`http://localhost:8080/superVizor/getAllSensorStatusMetricValuesForDoughnutComponent`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${session?.value}`,
        "Content-Type": "application/json",
      },
    });

    const responseOfDoughbut = await response.json()

    console.log(responseOfDoughbut);
     const responsegetWorkerStats = await fetch(`http://localhost:8080/superVizor/getTimeBasedSessionWorkerStatsForBarChartData`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${session?.value}`,
        "Content-Type": "application/json",
      },
    });

    const responseOfBarChart = await responsegetWorkerStats.json()
         const getFaultyLocationsForSupervizorDashboardHeatmapComponent = await fetch(`http://localhost:8080/superVizor/getFaultyLocationsForSupervizorDashboardHeatmapComponent`, {
           method: "GET",
           headers: {
             Authorization: `Bearer ${session?.value}`,
             "Content-Type": "application/json",
           },
         });

     
         const responseOHeatmapComponent= await getFaultyLocationsForSupervizorDashboardHeatmapComponent.json() as 	[{
     id: string;
     latitude: number;
     longitude: number;
 }]
   
    return (
    <>
      <div className=" h-fit w-full items-center justify-center flex pt-[10px]">
        <MainPageChartsComponent 
        responseOfDoughbut = {responseOfDoughbut}
        responseOHeatmapComponent = {responseOHeatmapComponent}
        responseOfBarChart = {responseOfBarChart} />
      </div>
    </>
  );
};

export default page;
