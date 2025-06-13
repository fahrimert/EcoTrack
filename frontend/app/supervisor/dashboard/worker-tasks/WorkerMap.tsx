"use client"
import React from "react";

import GoogleMapComponentOfOrder from "./GoogleMapComponentOfOrder";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";


const WorkerMap = ({session} : {session : RequestCookie | undefined}) => {



//ilk başta sadece harita ve merkez göstericem 



  return (
    <div className="relative w-full h-fit flex flex-row justify-start items-start gap-[20px]  pt-[20px]  max-xl:flex-col  ">

       <div className="relative w-full h-full  max-xl:w-full"> 

      <GoogleMapComponentOfOrder  session = {session} />
    </div>

    {/* List Of */}
    </div>
  )
}

export default WorkerMap