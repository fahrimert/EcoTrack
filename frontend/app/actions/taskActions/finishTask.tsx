"use server";
import { TaskDetail } from "@/app/worker/dashboard/sensor-tasks/[id]/components/AssignedTaskFormForSolving";

import axios from "axios";
import { cookies,} from "next/headers";

export async function finishTask(formData: FormData,initialData : TaskDetail) {
 
  try {

        const session = cookies().get("session")?.value
        const response = await axios.put(
            `http://localhost:8080/sensor/finishTask/${initialData.id}`,
            formData,
            {
              headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${session}`,
    
              },
            }
          );
        const responseJson = await response.data 

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
