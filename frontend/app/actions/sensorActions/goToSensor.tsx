"use server";
import { SignİnFormSchema, FormState } from "@/lib/definitions";
import axios from "axios";
import { cookies, headers } from "next/headers";
import { redirect } from "next/navigation";

export async function goToSensor(sensorId: string) {
 
  try {

        const session = cookies().get("session")?.value
        const responseData = await fetch(`http://localhost:8080/sensor/repair/${sensorId}`, {
          method: 'PUT',
          headers: {
            'Authorization': `Bearer ${session}`,
            'Content-Type': 'application/json'
          }
        });
         const responseJson = await responseData.text() 

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
