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
import SensorList, { UserProfile } from "./SensorList";


export interface Notification {
  supervizorDescription: string;
  superVizorDeadline: string; 
  createdAt: string;          
  senderId: number;
  receiverId: number;
  taskId: number;
  isread: boolean,
  id: number;
}

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
  superVizorDeadline: string; // ISO 8601 format
  assignedBy: {
    id: number;
    firstName: string;
    surName: string;
  };
  workerArriving: string | null;
  workerArrived: string | null;
    worker_on_road_note: string;

}
const SensorsAndMap = ({session } : {session:RequestCookie | undefined}) => {
  
  //source ve destination stateleri map için 
  const [source,setSource] = useState({
    lat:39.9334,
    lng: 32.8597
  })
  const [destination,setDestination] = useState({
    lat:null,
    lng: null
  })
  const [sensorListData,setSensorListData] = useState<typeof SensorList[]>()
  const [taskSensorListData,setTaskSensorListData] = useState <TaskSensorWithTask[]>([])
  const [userProfile,setUserProfile] = useState<UserProfile>()

  const [notification,setNotification] = useState<Notification>()
  useEffect(() => {
    axios.get(`http://localhost:8080/user/profile/${session?.value}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setUserProfile(res.data))
    .catch((err) => console.log(err));
  }, []);

  let stompClient: Client;

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws'); 
    stompClient = over(socket);
    stompClient.connect({}, (frame) => {
      console.log("Connected: " + frame); 
      stompClient.subscribe('/topic/sensors', (message) => {
        const updatedSensor = JSON.parse(message.body);
        setSensorListData(updatedSensor)
      });
    }, (error) => {
      console.error("WebSocket bağlantı hatası:", error);
    });

   
  }, []);




  
      //burda tüm userları gösterecez sadece bunu eşleşenleri online diye gösterecez onu da backgroundu yeşil yaparız 
  
  

  //tüm userların locationlarını anlık almak için websocket için websocket 

  //dbdeki tüm sensor listesini almak için endpoint
  useEffect(() => {
    axios.get("http://localhost:8080/sensors", {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setSensorListData(res.data))
    .catch((err) => console.log(err));
  }, []);

    useEffect(() => {
        if (!userProfile?.id) return; // userProfile henüz gelmediyse çık

    axios.get(`http://localhost:8080/tasks/getTasksOfMe/${userProfile?.id}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setTaskSensorListData(res.data))
    .catch((err) => console.log(err));
  }, [userProfile?.id]);


  //mantık task varsa taskdaki sensör listesini task yoksa normal bunu dönder 
    
    
  //user için useeffect

  return (
    <>
    <div className="relative w-full h-fit flex flex-row justify-start items-start gap-[20px]  pt-[20px]  max-xl:flex-col  ">
      <SourceContext.Provider value={{source,setSource}}>
      <DestinationContext.Provider value={{destination,setDestination}}>
      <HoverContext.Provider value={false}>

       <div className="relative w-[60%] h-full  max-xl:w-full"> 

        {/* Search Component */}
      <div className="absolute left-40 z-10 p-6  ">
        <SearchUserLocation session={session} />
      </div>

      {/* Google Map Component */}
      <GoogleMapComponent taskSensorListData = {taskSensorListData}  userProfile = {userProfile} sensorListData= {sensorListData} session={session} />
    </div>

    {/* List Of */}
      <div className="relative w-[40%] h-fit flex flex-col justify-start items-start max-xl:w-full ">
     
      <SensorList taskSensorListData = {taskSensorListData}  sensorListData= {sensorListData} session = {session} userProfile ={userProfile}/>
      </div>
      </HoverContext.Provider>
      </DestinationContext.Provider>
      </SourceContext.Provider>
    </div>
    </>
  );
};

export default SensorsAndMap;
