"use client"
import React, { useEffect, useState } from "react";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { SourceContext } from "@/context/SourceContext";

import MapOfSingleSensor from "./MapOfSingleSensor";
import SearchUserLocation from "../../../components/SearchComponents/SearchUserLocation";
import AssignedSensorFormForOnRoad from "./AssignedTaskFormForOnRoad";
import AssignedTaskFormForSolving, { TaskDetail } from "./AssignedTaskFormForSolving";

import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Client, over } from "stompjs";
import SockJS from "sockjs-client";
import { TaskSensorWithTask } from "../../../components/SensorComponents/SensorsAndMap";


const AssignedTaskAndMap = ({initialData , session ,stasusesData  } : {initialData: TaskSensorWithTask
  
  , session:RequestCookie | undefined , 
stasusesData  : [ 'ACTIVE', 'FAULTY', 'IN_REPAIR', 'SOLVED' ] }) => {
  const [source,setSource] = useState({
    lat:39.9334,
    lng: 32.8597
  })

  const [task, setTask] = useState<TaskSensorWithTask>(initialData);
  

  let stompClient: Client;
  
      //burda tüm userları gösterecez sadece bunu eşleşenleri online diye gösterecez onu da backgroundu yeşil yaparız 
  
    useEffect(() => {
      const socket = new SockJS('http://localhost:8080/ws'); 
      stompClient = over(socket);
      stompClient.connect({}, (frame) => {
        console.log("Connected: " + frame); 
        stompClient.subscribe('/topic/tasks', (message) =>  
          {const task = JSON.parse(message.body) as  TaskSensorWithTask;
            setTask(task)});
      }, (error) => {
        console.error("WebSocket bağlantı hatası:", error);
      });
    }, []);
console.log(stasusesData);
  return (
    <>
    <div className="relative w-full h-fit flex flex-row justify-start items-start gap-[20px]  pt-[20px] ">


      <SourceContext.Provider value={{source,setSource}}>
   
   <Tabs defaultValue="onroad" className="relative w-[70%] h-fit flex flex-col justify-start items-start">
  <TabsList className=" w-full h-fit items-center justify-center flex ">
    <TabsTrigger value="onroad">Yoldayım Notu</TabsTrigger>
    <TabsTrigger value="solving">Görev Notu</TabsTrigger>
  </TabsList>
   
  <TabsContent value="onroad">
    

        <AssignedSensorFormForOnRoad initialData = {initialData}  />
  </TabsContent>
  <TabsContent value="solving">
        <AssignedTaskFormForSolving initialData = {initialData}  stasusesData = {stasusesData}  />

  </TabsContent>

</Tabs>
    
       <div className="relative w-[50%] h-full items-center justify-center"> 
      <div className="absolute left-40 z-10 p-6  ">
        <SearchUserLocation session={session} />
      </div>
      <MapOfSingleSensor session={session}  initialData = {initialData} />
</div>
      
      </SourceContext.Provider>
    </div>
    </>
  );
};

export default AssignedTaskAndMap;
