"use server";
import { TaskSensorWithTask } from "@/app/worker/dashboard/components/SensorComponents/SensorsAndMap";
import { TaskDetail } from "@/app/worker/dashboard/sensor-tasks/[id]/components/AssignedTaskFormForSolving";
import { SensorData } from "@/app/worker/dashboard/sensors/[id]/components/AssignedSensorAndMap";
import { SignİnFormSchema, FormState } from "@/lib/definitions";
import axios from "axios";
import { cookies, headers } from "next/headers";
import { redirect } from "next/navigation";

export async function updateTask(worker_on_road_note: string ,initialData : TaskSensorWithTask) {
 
  try {
      console.log(worker_on_road_note);
        const session = cookies().get("session")?.value
        const response = await axios.put(
            `http://localhost:8080/task/updateOnRoad/${initialData.id}`,
         worker_on_road_note
         ,
            {
              headers: {
      'Content-Type': 'application/json',
                'Authorization': `Bearer ${session}`,
    
              },
            }
          );
        const responseJson = await response.data 
            console.log(responseJson);
             return {
            serverData: responseJson,
          };
  
  

}
 catch (error) {
    console.log((error as Error).message)
    return {
      serverError: "Bir Sorun Oluştu   ",
    };
  }
 

}
