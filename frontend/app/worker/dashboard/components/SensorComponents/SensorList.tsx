"use client";
import React, { useEffect, useRef, useState } from "react";
import { ScrollArea } from "@/components/ui/scroll-area";
import Heading from "../../past-sensors/[id]/components/Heading";
import TaskSensor from "./TaskSensor";
import { Client, over } from "stompjs";
import SockJS from "sockjs-client";
import { DifferentUserProfileType, UserProfileDTO } from "@/app/sharedTypes";
import { WorkerDashboardSensorDto, WorkerDashboardTaskSensorWithTaskDto } from "@/app/worker/types/types";
import SensorCard from "./SensorCard";
import { Loader2, Inbox } from "lucide-react";


//typeyi şimdilik silmedim typeyi silecem diğer yerlerde de kullanmayı kesince 
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


interface SensorListProps {
  sensorListFromTasksOfSingleUser: WorkerDashboardTaskSensorWithTaskDto[];
  sensorListData: WorkerDashboardSensorDto[] | undefined;
  userProfile: UserProfileDTO | undefined;
}
const SensorList = ({
  sensorListFromTasksOfSingleUser,
  sensorListData,
  userProfile,
}:  SensorListProps) => {
  const [tasks, setTasks] = useState<WorkerDashboardTaskSensorWithTaskDto[]>(sensorListFromTasksOfSingleUser);
  const [sensors, setSensors] = useState<WorkerDashboardSensorDto[]>(sensorListData || []);
  const [hasLiveTasks, setHasLiveTasks] = useState(sensorListFromTasksOfSingleUser.length > 0);

  const stompClientRef = useRef<Client | null>(null);

  useEffect(() => {
    setTasks(sensorListFromTasksOfSingleUser);
  }, [sensorListFromTasksOfSingleUser]);

  useEffect(() => {
    if(sensorListData) setSensors(sensorListData);
  }, [sensorListData]);

  
  (sensorListData);


        let stompClient: Client;
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
      
              return [updatedTask, ...prevTasks]; 
               }
      )});

              setHasLiveTasks(true);

        }, (error) => {
          console.error("WebSocket bağlantı hatası:", error);
        });
    
       
      }, []);


        
            //burda tüm userları gösterecez sadece bunu eşleşenleri online diye gösterecez onu da backgroundu yeşil yaparız 
        
const hasActiveTasks = tasks.length > 0;
const showSensors = !hasActiveTasks && sensors.length > 0;
const isEmpty = !hasActiveTasks && sensors.length === 0;  
return (
<div className="h-full w-full bg-white xl:border-l border-gray-200 xl:shadow-xl flex flex-col rounded-2xl overflow-hidden border xl:border-0 shadow-md xl:shadow-none">      
      <div className="p-6 border-b border-gray-100 bg-gray-50/50 backdrop-blur-sm sticky top-0 z-10">
        <Heading
          title={hasActiveTasks ? "Aktif Görevler" : "Tüm Sensörler"}
          description={
            hasActiveTasks
              ? "Üzerinize atanmış görevleri öncelikli olarak tamamlayınız."
              : "Şu an aktif göreviniz yok. Sahadaki sensörleri inceleyebilirsiniz."
          }
        />
        
        <div className="mt-2 flex items-center gap-2">
            <span className={`h-2 w-2 rounded-full ${hasActiveTasks ? 'bg-amber-500 animate-pulse' : 'bg-emerald-500'}`} />
            <span className="text-xs text-gray-500 font-medium">
                {hasActiveTasks ? `${tasks.length} Görev Bekliyor` : "Saha Stabil"}
            </span>
        </div>
      </div>

      <ScrollArea className=" h-[700px] bg-gray-50/30">
        <div className="p-4 pb-20">
          
          {isEmpty && (
            <div className="flex flex-col items-center justify-center h-64 text-gray-400 gap-3">
               <div className="bg-gray-100 p-4 rounded-full">
                 <Inbox size={40} className="text-gray-300" />
               </div>
               <p className="text-sm font-medium">Görüntülenecek veri yok</p>
            </div>
          )}

          <div className="grid grid-cols-1 xl:grid-cols-2 gap-4 animate-in fade-in slide-in-from-bottom-4 duration-500">
            
            {hasActiveTasks && tasks.map((task) => (
              <TaskSensor key={task.id} sensors={task.taskSensors} />
            ))}

            {showSensors && sensors.map((sensor) => {
              const isMySensor = sensor.currentSensorSession?.userId === userProfile?.id;
              return (
                <SensorCard
                  key={sensor.id}
                  sensor={sensor}
                  isMySensor={isMySensor}
                />
              );
            })}
          </div>
        </div>
      </ScrollArea>
    </div>
  );
};

export default SensorList;
