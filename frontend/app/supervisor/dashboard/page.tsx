import { cookies } from "next/headers";
import React from "react";
import { ChartComponent } from "./components/ChartComponent";
import MainPageChartsComponent from "./components/MainPageChartsComponent";
 interface UserStats {
    [key: string]: number; // kullanıcı adı -> sayı eşleşmesi
  }

  interface TimeBasedUserStats {
    last_month?: UserStats;
    last_week?: UserStats;
    last_day?: UserStats;
  }

  export type FullUserStats = TimeBasedUserStats[]
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
 
    return (
    <>
      <div className=" h-fit w-full items-center justify-center flex">
        <MainPageChartsComponent sensorfilterBasedStat = {sensorfilterBasedStat} getWorkerStats = {getWorkerStats} />
      </div>
    </>
  );
};

export default page;
