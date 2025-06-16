"use client"

import React, { useContext, useEffect, useState } from "react";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import axios from "axios";

import GoogleMapComponentOfAllUsers from "./GoogleMapComponentOfAllUsers";
import { TailSpin } from 'react-loader-spinner'
import { SourceContext } from "@/context/SourceContext";
import { DestinationContext } from "@/context/DestinationContext";
import { HoverContext } from "@/context/HoverContext";
import SensorListAndUsers from "./SensorListAndUsers";
import SensorList from "../../components/SensorComponents/SensorList";

const UserSensorsAndMapOFAllUsers = ({session , usersAndTheirSensors } : {session:RequestCookie | undefined, 
  usersAndTheirSensors : [{  id: number;
    name: string;
    latitude: number;
    longitude: number;
    sensorlatitude: number;
    sensorlongitude: number;
    sensor: {
      id: number;
      sensorName: string;
      status: string;
      installationDate: string;
    };}] }) => {


  //sensör güncelleme websocketi 

//bizim almamız gereken herşey bir useeffectte var 

const [sensorListData,setSensorListData] = useState<typeof SensorList[]>()

useEffect(() => {
  axios.get("http://localhost:8080/sensors", {
    headers: { Authorization: `Bearer ${session?.value}` },
    withCredentials: true,
  })
  .then((res) => setSensorListData(res.data))
  .catch((err) => console.log(err));
}, []); 


const [source,setSource] = useState({
  lat:39.9334,
  lng: 32.8597
})
const [destination,setDestination] = useState({
  lat:null,
  lng: null
})
  return (
    <>
       <SourceContext.Provider value={{source,setSource}}>
          <DestinationContext.Provider value={{destination,setDestination}}>
    
          <HoverContext.Provider value={false}>
    <div className="relative w-full h-screen flex flex-row justify-start items-start gap-[20px]  pt-[20px] ">
      {/* Sensör Listesi  */}
   
    <div className=" w-full h-fit flex flex-col bg-[#c2cecb] items-start justify-start     gap-[5px] rounded-[30px]">
      <SensorListAndUsers  sensorListData={usersAndTheirSensors}  />



  </div>
  

{/* Harita */}
       <div className="relative w-full h-full items-center"> 
       {usersAndTheirSensors.length > 0 && usersAndTheirSensors[0].id !== undefined ? (
      <GoogleMapComponentOfAllUsers usersAndTheirSensors = {usersAndTheirSensors}   session={session} sensorListData = {sensorListData} />

       ): (
        <div><TailSpin
        visible={true}
        height="80"
        width="80"
        color="#4fa94d"
        ariaLabel="tail-spin-loading"
        radius="1"
        wrapperStyle={{}}
        wrapperClass=""
        />
      </div> // veya boş bir <></>
      )}
    </div>
     
      
    </div>
    </HoverContext.Provider>
</DestinationContext.Provider>

</SourceContext.Provider>
    </>
  );
};

export default UserSensorsAndMapOFAllUsers;
