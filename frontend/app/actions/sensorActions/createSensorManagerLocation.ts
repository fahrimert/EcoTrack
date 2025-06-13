"use server";

import { AddSensorFormValues } from "@/app/manager/dashboard/sensor-management/ManagerSensorAddLocationComponent";
import axios from "axios";
import { cookies} from "next/headers";

export async function createSensorManagerLocation(data: AddSensorFormValues) {
  try {
    console.log(data);
 const session = cookies().get("session")?.value
        const response = await axios.post(
            `http://localhost:8080/manager/updateSensorLocations`,
            
              {
                  id:data.sensorId,
                latitude: data.lat,
                longitude : data.lng,
            
            },
            {
              headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${session}`,
              },
            }
          );
        const responseJson = await response.data 

   return {
            serverData: "Başarıyla Sensor Location Güncellendi",
          };
  

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
