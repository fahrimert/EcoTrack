"use client"
import Image from "next/image";
import Link from "next/link";
import React, { useState } from "react";
import SensorList from "./SensorList";
import GoogleMapComponent from "./GoogleMapComponent";
import SearchUserLocation from "./SearchUserLocation";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { LoadScript } from "@react-google-maps/api";
import { SourceContext } from "@/context/SourceContext";
import { DestinationContext } from "@/context/DestinationContext";
import {APIProvider} from '@vis.gl/react-google-maps';
import { Wrapper } from "@googlemaps/react-wrapper";


const SensorsAndMap = ({session,dataa } : {session:RequestCookie | undefined,dataa : {
  lat:number,
  long:number
}}) => {
  
  const [source,setSource] = useState({
    lat:31.321,
    lng:32.321
  })

  const [destination,setDestination] = useState({
    lat:28.321,
    lng:38.321
  })

  
  
  
  
  
  return (
    <>
    <div className="relative w-full h-screen flex flex-row justify-start items-start gap-[20px]  pt-[40px] ">


      <SourceContext.Provider value={{source,setSource}}>
      <DestinationContext.Provider value={{destination,setDestination}}>
      <div className="relative w-[40%] h-full flex flex-col justify-start items-start  border-[1px]   ">
      <SearchUserLocation session = {session}/>
 <SensorList/>
      </div>
      <div className="w-[200px]">

      <GoogleMapComponent session ={session}/>
      
      </div>

      </DestinationContext.Provider>
      
      </SourceContext.Provider>

    </div>
    </>
  );
};

export default SensorsAndMap;
