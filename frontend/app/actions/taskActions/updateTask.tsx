"use server";
import { TaskSensorWithTask } from "@/app/worker/dashboard/components/SensorComponents/SensorsAndMap";
import axios from "axios";
import { cookies } from "next/headers";

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
