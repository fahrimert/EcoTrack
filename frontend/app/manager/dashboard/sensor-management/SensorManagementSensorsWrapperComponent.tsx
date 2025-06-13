"use client"
import React, { useEffect, useState } from "react";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import axios from "axios";
import SockJS from 'sockjs-client';
import { Client, over } from 'stompjs';
import { useUserProfile } from "@/hooks/useUserProfile";
import ManagementSensorList from "./ManagementSensorList";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from "@/components/ui/tabs"
import {
  Dialog,
  DialogContent,
  DialogTrigger,
} from "@/components/ui/dialog"
import ManagerSensorAddComponent from "./ManagerSensorAddComponent";
import ManagerSensorAddComponentWrapper from "./ManagerSensorAddComponentWrapper";
import { Button } from "@/components/ui/button";
import ManagerSensorAddLocationComponent from "./ManagerSensorAddLocationComponent";
import ManagerSensorUpdateComponent from "./ManagerSensorUpdateComponent";

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

export interface SensorListForManagerUse {
  id: number;
  sensorName: string;
  status: string;
  color_code: string;
  latitude: number;
  longitude: number;
  installationDate: string; 
  lastUpdatedAt: string | null;
  currentSensorSession:
    | {
         id: number;
  sensorName: string;
  firstName: string;
  surName: string;
  online: boolean;
      }
    | undefined;
      imageResponseDTO:  {
  name: string;
  type: string;
  base64Image: string | undefined;
};
}


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
const SensorManagementSensorsWrapperComponent = ({session ,stasusesData} : {session:RequestCookie | undefined , stasusesData  :[ 'ACTIVE', 'FAULTY', 'IN_REPAIR', 'SOLVED' ]}) => {
  

  const [sensorListData,setSensorListData] = useState<SensorListForManagerUse[]>()
/*   const [taskSensorListData,setTaskSensorListData] = useState <TaskSensorWithTask[]>([])
 */
   const { userProfile, loading, error } = useUserProfile(session);

 /*  let stompClient: Client;

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
 */


  useEffect(() => {
    axios.get("http://localhost:8080/manager/getAllSensorForManagerUse", {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setSensorListData(res.data))
    .catch((err) => {
  console.log("Sensör verisi alınamadı:", err);
  setSensorListData([]); // fallback
  })
  }, []);

  
 const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
 const [isUpdateDialogOpen, setIsUpdateDialogOpen] = useState(false);


 const [sensorId,setSensorId] = useState("")

  const [sensorDataInduvual,setSensorDataInduvual] = useState< {
  success: boolean;
  message: string;
  data: {
      sensorName: string;
     imageResponseDTO :{ name: string;
      type: string;
      base64Image: string}
  }
  errors: any; // daha detaylı bir yapı varsa ona göre değiştirebilirsin
  status: number;
}>()
  console.log(session);
  useEffect(() => {
    axios.get(`http://localhost:8080/sensors/sensormanagement/${sensorId}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setSensorDataInduvual(res.data))
    .catch((err) => {
  console.log("Sensör verisi alınamadı:", err);
  setSensorDataInduvual(null); // fallback
  })
  }, [sensorId]);

  console.log(sensorDataInduvual,sensorId);

  return (
    <>
    <div className="relative w-full h-fit flex flex-row justify-start items-start gap-[20px]  pt-[20px]  max-xl:flex-col  ">
          <div className="w-fit h-fit flex flex-col gap-[20px]">


         <Dialog 
           open={isAddDialogOpen} 
    onOpenChange={setIsAddDialogOpen}
         modal={false} >
  <DialogTrigger    className="h-full w-full items-start flex flex-col justify-start rounded-[10px] bg-[#edecea] py-[10px] px-[3px] gap-[10px]">
    <h2>

     Sensör Ekleyin
    </h2>
                      
                      </DialogTrigger >
  <DialogContent  
    onInteractOutside={(event) => {
    event.preventDefault(); // dış tıklamayı engelle
  }}
  onEscapeKeyDown={(event) => {
    event.preventDefault(); // ESC tuşuyla kapanmayı da engelle
  }}

  className="bg-white w-fit items-center justify-center flex ">
   <div>
       <Tabs defaultValue="addsensor">
        <TabsList>
          <TabsTrigger value="withoutlocationcomponent">Sensör Ekle</TabsTrigger>
          <TabsTrigger value="withlocationcomponent">Sensörün Konumunu Güncelle</TabsTrigger>
        </TabsList>
        <TabsContent value="withoutlocationcomponent">
        <ManagerSensorAddComponent onSuccess={() => setIsAddDialogOpen(false)} />
        </TabsContent>
        <TabsContent value="withlocationcomponent">
        <ManagerSensorAddLocationComponent sensorListData = {sensorListData}  onSuccess={() => setIsAddDialogOpen(false)}/>
        </TabsContent>
      </Tabs>
     


   </div>


  </DialogContent>
</Dialog>

         <Dialog 
           open={isUpdateDialogOpen} 
    onOpenChange={setIsUpdateDialogOpen}
         modal={false} >
  <DialogTrigger    className="h-full w-full items-start flex flex-col justify-start rounded-[10px] bg-[#edecea] py-[10px] px-[3px] gap-[10px]">
    <h2>

     Sensör Güncelleyin
    </h2>
                      
                      </DialogTrigger >
  <DialogContent  
    onInteractOutside={(event) => {
    event.preventDefault(); // dış tıklamayı engelle
  }}
  onEscapeKeyDown={(event) => {
    event.preventDefault(); // ESC tuşuyla kapanmayı da engelle
  }}

  className="bg-white w-fit items-center justify-center flex">
   <div>
         <ManagerSensorUpdateComponent  sensorDataInduvual = {sensorDataInduvual?.data} onSetId={(e) =>setSensorId(e)} sensorListData = {sensorListData} onSuccess={() => setIsUpdateDialogOpen(false)} />
 </div>


  </DialogContent>
</Dialog>
        </div>   

      <div className="relative w-[100%] h-fit flex flex-col justify-start items-start max-xl:w-full ">
     
     <ManagementSensorList 
     stasusesData = {stasusesData}
       sensorListData= {sensorListData } 
       session = {session}
        userProfile ={userProfile}/> 
      </div>
    </div>
    </>
  );
};

export default SensorManagementSensorsWrapperComponent;
