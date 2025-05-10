"use server";
import { SensorData } from "@/app/dashboard/sensors/[id]/components/AssignedSensorAndMap";
import { SignİnFormSchema, FormState } from "@/lib/definitions";
import axios from "axios";
import { cookies, headers } from "next/headers";
import { redirect } from "next/navigation";

export async function updateSensor(formData: FormData,initalData : SensorData) {
 
  try {

        const session = cookies().get("session")?.value
        const response = await axios.put(
            `http://localhost:8080/sensor/AllState/${initalData.data.currentSensorSession.sensor.id}`,
            formData,
            {
              headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${session}`,
    
              },
            }
          );
        const responseJson = await response.data.text() 
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
 
  finally{
    redirect(`/dashboard`)

  }

}
