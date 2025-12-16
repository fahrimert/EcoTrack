"use client"

import React, { useContext, useEffect, useState } from "react";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import axios from "axios";

import GoogleMapComponentOfAllUsers from "./GoogleMapComponentOfAllUsers";
import { TailSpin } from 'react-loader-spinner'
import { SourceContext } from "@/context/SourceContext";
import { DestinationContext } from "@/context/DestinationContext";
import { HoverContext } from "@/context/HoverContext";
import SensorListAndUsers from "./CrewJobTrackPageSensorListAndUsers";
import SensorList from "../../components/SensorComponents/SensorList";
import { CrewJobsUserAndSessionSensorDTO, WorkerDashboardSensorDto } from "@/app/worker/types/types";
import CrewJobTrackPageSensorListAndUsers from "./CrewJobTrackPageSensorListAndUsers";
import { UserProfileDTO } from "@/app/sharedTypes";


interface CrewPageProps {
  crewJobTrackPageSensorsAndTheirLocations: CrewJobsUserAndSessionSensorDTO[];
  workerSensors: WorkerDashboardSensorDto[];
  userProfile: UserProfileDTO;
}

const CrewJobTrackPageSensorsAndTheirLocations = ({ crewJobTrackPageSensorsAndTheirLocations,  workerSensors,  userProfile } : CrewPageProps) => {

  //sensör güncelleme websocketi 

//bizim almamız gereken herşey bir useeffectte var 

const [sensorListData,setSensorListData] = useState<typeof SensorList[]>()



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
<div className="w-full h-[calc(100vh-80px)] p-4 pt-6 grid grid-cols-1 xl:grid-cols-12 gap-6">
            
            <div className="order-2 xl:order-1 xl:col-span-4 h-[500px] xl:h-full">
               <CrewJobTrackPageSensorListAndUsers 
                  userProfile={userProfile}
                  crewJobTrackPageSensorsAndTheirLocations={crewJobTrackPageSensorsAndTheirLocations} 
               />
            </div>

            <div className="relative order-1 xl:order-2 xl:col-span-8 h-[500px] xl:h-full rounded-2xl overflow-hidden shadow-lg border border-slate-200 bg-white">
             

                <GoogleMapComponentOfAllUsers 
                   crewJobTrackPageSensorsAndTheirLocations={crewJobTrackPageSensorsAndTheirLocations} 
                   workerSensors={workerSensors}
                />
            </div>

          </div>
    </HoverContext.Provider>
</DestinationContext.Provider>

</SourceContext.Provider>
    </>
  );
};

export default CrewJobTrackPageSensorsAndTheirLocations;
