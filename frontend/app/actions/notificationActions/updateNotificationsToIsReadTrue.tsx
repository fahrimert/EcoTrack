"use server";
import { TaskSensorWithTask } from "@/app/worker/dashboard/components/SensorComponents/SensorsAndMap";
import axios from "axios";
import { cookies } from "next/headers";

export async function updateNotificationsToIsReadTrue(session:string,userId:string) {
  console.log(userId);
  console.log(session );
  try {
        const response = await axios.put(
            `http://localhost:8080/task/updateMarkIsRead/${userId}`,
            {},
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
    console.log(error);
    return {
    
      serverError: "Bir Sorun Oluştu   ",
    };
  }
 

}
