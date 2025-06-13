import React from "react";
import { cookies } from "next/headers";
import SensorManagementSensorsWrapperComponent from "./SensorManagementSensorsWrapperComponent";


const page = async () => {
  const session = cookies().get("session");

      const statuses = await fetch(`http://localhost:8080/sensors/getSensorStatuses`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${session?.value}`,
          'Content-Type': 'application/json'
        }
      });
      
      const stasusesData = await statuses.json() as [ 'ACTIVE', 'FAULTY', 'IN_REPAIR', 'SOLVED' ]
  return (
    <div className=" w-full h-full bg-white rounded-[20px] p-[10px]  gap-[10px] mt-[10px]">
      <h2 className="text-[24px]">Sensör  Listesi</h2>
       <div className="relative w-[100%] h-fit flex flex-col justify-start items-start max-xl:w-full ">
     
     <SensorManagementSensorsWrapperComponent 
     stasusesData = {stasusesData}
        session={session}/> 
    </div> 
  </div>
  );
};

export default page;
