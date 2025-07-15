import React from "react";
import { cookies } from "next/headers";
import SensorManagementSensorsWrapperComponent from "./SensorManagementSensorsWrapperComponent";

const page = async () => {
  const session = cookies().get("session");


  return (
    <div className=" w-full h-full bg-white rounded-[20px] p-[10px]  gap-[20px] mt-[10px]">
       <div className="relative w-[100%] h-fit flex flex-col justify-start items-start max-xl:w-full ">
     
     <SensorManagementSensorsWrapperComponent 

        session={session}/> 
    </div> 
  </div>
  );
}

export default page