"use server";

import axios from "axios";
import { cookies} from "next/headers";

export async function updateSensorManager(formData: FormData) {
  try {
console.log(formData);
         const session = cookies().get("session")?.value
        const response = await axios.post(
            `http://localhost:8080/manager/managerUpdateSensor`,
            formData,
            {
              headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${session}`,
    
              },
            }
          );
        const responseJson = await response.data 
        const responseJsonStatus =  response.status 

             return {
            serverData: "Sensor Başarıyla Kuruldu",
          }; 
  console.log(responseJsonStatus);
  

}
 catch (error) {
      if (axios.isAxiosError(error) && error.response) {
    console.log("Backend Hatası:", error.response.data); 
    return {
      serverError: error.response.data || "Bir hata oluştu.",
    };
  } else {
    console.log("Beklenmeyen Hata:", (error as Error).message);
    return {
      serverError: "Beklenmeyen bir hata oluştu.",
    };
  }
  }
 

}
