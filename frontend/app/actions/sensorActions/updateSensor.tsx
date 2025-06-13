"use server";

import { SensorDataDifferentOne } from "@/app/supervisor/superVizorDataTypes/types";
import axios from "axios";
import { cookies,  } from "next/headers";

export async function updateSensor(formData: FormData,initialData : SensorDataDifferentOne) {
 
  try {

        const session = cookies().get("session")?.value
        const response = await axios.put(
            `http://localhost:8080/sensor/AllState/${initialData.data.id}`,
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
