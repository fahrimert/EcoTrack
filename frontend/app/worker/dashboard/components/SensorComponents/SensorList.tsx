"use client";
import React, { useEffect, useState } from "react";
import Sensor from "./Sensor";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { ScrollArea } from "@/components/ui/scroll-area";
import { TaskSensorWithTask } from "./SensorsAndMap";
import Heading from "../../past-sensors/[id]/components/Heading";
import TaskSensor from "./TaskSensor";
import { Client, over } from "stompjs";
import SockJS from "sockjs-client";
import { UserProfilea } from "@/app/supervisor/superVizorDataTypes/types";

//interface for sensorlist
export interface SensorList {
  sensorName: string;
  status: string;
  colorCode: string;
  latitude: number;
  longitude: number;
  currentSensorSession:
    | {
        id: number;
        sensor: {
          id: number;
          sensorName: string;
          status: string;
          installationDate: string;
        };
        startTime: string;
        completedTime: null;
        note: null;
      }
    | undefined;
}
/* interface for user profile data */
export interface UserProfile {
  id: number;
  email: string;
  firstName: string;
  surName: string;
  password: string;
  refreshToken: {
    token: string;
    expiresAt: string;
    id: number;
  };
  role: string;
  twoFactorCode: null;
  sensorSessions: [
    {
      id: number;
      sensor: {
        id: number;
        sensorName: string;
        status: string;
        installationDate: number;
      };
      startTime: string;
      completedTime: string;
      note: null;
    }
  ];
  twoFactorAuthbeenverified: boolean;
}

const SensorList = ({
  taskSensorListData,
  session,
  sensorListData,
  userProfile,
}: {
  session: RequestCookie;
  taskSensorListData : TaskSensorWithTask[]
  sensorListData: SensorList[] | undefined;
  userProfile: UserProfilea | undefined;
}) => {
const userBasedsensor = sensorListData?.map((g) => {
  if (!g.currentSensorSession || !userProfile?.sensorSessions || userProfile.sensorSessions.length === 0) {
    return false;
  }
  return g.currentSensorSession.id === userProfile.sensorSessions[0].id;
});
  const customSensorListData = sensorListData?.map((sensor) => {
    const isUserSensor =   userProfile?.sensorSessions ? userProfile?.sensorSessions.some(
      (session) => session.id === sensor.currentSensorSession?.id
    ) : null;

    return {
      ...sensor,
      userBasedSensor: isUserSensor, 
    };
  });

  const [tasks, setTasks] = useState<TaskSensorWithTask[]>(taskSensorListData);
  const [hasLiveTasks, setHasLiveTasks] = useState(taskSensorListData.length > 0);

        let stompClient: Client;
    useEffect(() => {
  setTasks(taskSensorListData);
      setHasLiveTasks(taskSensorListData.length > 0);

}, [taskSensorListData]);
      useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws'); 
        stompClient = over(socket);
        stompClient.connect({}, (frame) => {
          console.log("Connected: " + frame); 
          stompClient.subscribe('/topic/tasks', (message) => {
            const updatedTask = JSON.parse(message.body);

        setTasks(prevTasks =>{
                  const exists = prevTasks.some(notif => notif.id === updatedTask.id);
              if (exists) return prevTasks;
      
              return [updatedTask, ...prevTasks]; // En başa eklemek için
               }
      )});

              setHasLiveTasks(true); // Yeni görev geldi, görevli moda geç

        }, (error) => {
          console.error("WebSocket bağlantı hatası:", error);
        });
    
       
      }, []);


        
      console.log(taskSensorListData);
      console.log(tasks);
        
            //burda tüm userları gösterecez sadece bunu eşleşenleri online diye gösterecez onu da backgroundu yeşil yaparız 
        

  return (<>
     
       <ScrollArea className="h-screen  w-full flex flex-col">
        <div className="w-full h-fit flex flex-col gap-[20px]">

   { tasks.length == 0 ? 
  <Heading
title={"Tüm Sensörler"}
description={"Herhangi bir göreviniz olmadığı için tüm sensörlerin arasından müsait olanları seçebilirsiniz"}
/> : 
<Heading
title={"Görevler"}
description={"Görevleriniz olduğu için tüm sensörler yerine sadece görevinizdeki sensörleri görürsünüz"}

/>
  }



      <div className=" w-full h-fit grid grid-cols-2 bg-[#c2cecb] items-start justify-start     gap-[5px] rounded-[30px] max-xl:grid max-xl:grid-cols-3 max-md:grid max-md:grid-cols-1">
        {
        tasks.length !== 0  && hasLiveTasks ? 
            tasks?.map((sensors) => (
          <TaskSensor
            sensors={sensors.taskSensors}
          />
        ))
        : 
        
        customSensorListData?.map((sensors) => (
          <Sensor
            session={session}
            sensors={sensors}
            userProfile={userProfile}
            userBasedsensor={userBasedsensor}
          />
        ))}
      </div>
        </div>

    </ScrollArea>
    </>
 
  );
};

export default SensorList;
