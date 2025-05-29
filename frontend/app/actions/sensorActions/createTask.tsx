"use server";
import { AttachTaskFormValues } from "@/app/supervisor/dashboard/worker-tasks/AttachTask";
import { SignİnFormSchema, FormState } from "@/lib/definitions";
import axios from "axios";
import { cookies, headers } from "next/headers";
import { redirect } from "next/navigation";

export async function createTask(data: AttachTaskFormValues) {
 
  try {

        const session = cookies().get("session")?.value
             const response = await axios.post(
            `http://localhost:8080/tasks/createTask`,
            {superVizorDescription : data.description,
                superVizorDeadline : data.deadline,
                assignedTo:{
    id:data.userId 
                },
                sensor:{
    id: data.sensorId
  },
            },
            {
              headers: {
      'Content-Type': 'application/json',
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
