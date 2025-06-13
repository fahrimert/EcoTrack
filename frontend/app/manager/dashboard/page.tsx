import MainPageChartComponentOfManager from "./components/MainPageChartComponentOfManager";
import { cookies } from "next/headers";
import { SensorTypeCount } from "@/app/supervisor/superVizorDataTypes/types";
import axios from "axios";

const page = async () => { 
       const session = cookies().get("session");
      console.log(session);
   const responsegetWorkerStats = await fetch(`http://localhost:8080/manager/getSuperVizorTasks`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${session?.value}`,
        "Content-Type": "application/json",
      },
    });

    const getWorkerStats = await responsegetWorkerStats.json()


    
    return (
    <>
      <div className=" h-fit w-full items-center justify-center flex pt-[10px]">
        <MainPageChartComponentOfManager 
        session = {session}
        getWorkerStats = {getWorkerStats}
       />
      </div>
    </>
  );
};

export default page;
