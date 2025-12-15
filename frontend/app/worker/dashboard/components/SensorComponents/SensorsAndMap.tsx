"use client"


import React, { useEffect, useState } from "react";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { SourceContext } from "@/context/SourceContext";
import { DestinationContext } from "@/context/DestinationContext";
import axios from "axios";
import { HoverContext } from "@/context/HoverContext";
import SockJS from 'sockjs-client';
import { Client, over } from 'stompjs';
import GoogleMapComponent from "./GoogleMapComponent";
import SearchUserLocation from "../SearchComponents/SearchUserLocation";
import { useUserProfile } from "@/hooks/useUserProfile";
import { UserLocationDTO, UserProfileDTO } from "@/app/sharedTypes";
import { WorkerDashboardSensorDto, WorkerDashboardTaskSensorWithTaskDto } from "@/app/worker/types/types";
import SensorList from "./SensorList";


//başka yerde bunu kullanıyor olablirim worker dashboardda task sensorlerini alırken kullanmıyorum artık
export interface TaskSensorWithTask {
  id: number;
  taskSensors: {
    id: number;
    sensorName: string;
    status:string;
    color_code: string;
    latitude: number;
    longitude: number;
    currentSensorSession: {
      id: number;
      sensorName: string;
      displayName: string;
      color_code: string;
      note: string | null;
      startTime: string;
      completedTime: string | null;
      latitude: number;
      longitude: number;
    };
  };
  superVizorDescription: string;
  superVizorDeadline: string; 
  assignedBy: {
    id: number;
    firstName: string;
    surName: string;
  };
  workerArriving: string | null;
  workerArrived: string | null;
    worker_on_road_note: string;

}
const SensorsAndMap = ({userProfile ,sensorListFromtTaskOfSingleUser, workerDashboardSensors,userLocation} : {
   userProfile :UserProfileDTO,

   sensorListFromtTaskOfSingleUser: WorkerDashboardTaskSensorWithTaskDto[],
workerDashboardSensors :WorkerDashboardSensorDto[],
userLocation:UserLocationDTO
}) => {
  const [sensors, setSensors] = useState<WorkerDashboardSensorDto[]>(workerDashboardSensors);
  
  const [source,setSource] = useState({
    lat:39.9334,
    lng: 32.8597
  })
  const [destination,setDestination] = useState({
    lat:null,
    lng: null
  })
 
  let stompClient: Client;

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws'); 
    stompClient = over(socket);
    stompClient.connect({}, (frame) => {
      console.log("Connected: " + frame); 
      stompClient.subscribe('/topic/sensors', (message) => {
        const updatedSensorsList = JSON.parse(message.body);
                console.log("WebSocket Update Geldi:", updatedSensorsList);
      setSensors(updatedSensorsList);
      });
    }, (error) => {
      console.error("WebSocket bağlantı hatası:", error);
    });

   
  }, []);



  return (
    <>
    <div className="relative w-full h-fit flex flex-row justify-start items-start gap-[20px]  pt-[20px]  max-xl:flex-col  ">
      <SourceContext.Provider value={{source,setSource}}>
      <DestinationContext.Provider value={{destination,setDestination}}>
      <HoverContext.Provider value={false}>
<div className="w-full flex flex-col xl:grid xl:grid-cols-12 gap-6 p-4 h-full min-h-screen xl:min-h-[calc(100vh-100px)] xl:max-h-[calc(100vh-50px)]">

            <div className="relative w-full h-[500px] xl:h-full xl:col-span-8 rounded-2xl overflow-hidden shadow-lg border border-slate-200 bg-white order-1">
              
              <div className="absolute top-4 left-4 right-4 z-10 sm:w-[400px] sm:right-auto">
                <SearchUserLocation />
              </div>

              <div className="w-full h-full">
                <GoogleMapComponent 
                  sensorListFromTasksOfSingleUser={sensorListFromtTaskOfSingleUser} 
                  userProfile={userProfile} 
sensorListData={sensors}                  userLocation={userLocation} 
                />
              </div>
            </div>

            <div className="w-full h-fit xl:h-full xl:col-span-4 order-2 xl:overflow-hidden rounded-2xl">
              <SensorList
                sensorListFromTasksOfSingleUser={sensorListFromtTaskOfSingleUser}
                sensorListData={sensors} 
                userProfile={userProfile}
              /> 
            </div>

          </div>

      </HoverContext.Provider>
      </DestinationContext.Provider>
      </SourceContext.Provider>
    </div>
    </>
  );
};

export default SensorsAndMap;
