"use server";
import axios from "axios";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export async function logOut() {
 
  try {
        const session = cookies().get("session")?.value
        
        ;
        const response = axios.post(
            'http://localhost:8080/customLogout',
            {}, 
            {
              headers: {
                'Authorization': `Bearer ${session}`,
                'Content-Type': 'application/json',
              },
            }
          )
          .then(response => {
            console.log("Logout başarılı:", response.data);
          })
          .catch(error => {
            console.error("Logout hatası:", error);
          });
        
        
          cookies().delete("session")
          cookies().delete("refresh")

  
          redirect('/authentication')
        }
 catch (error) {
    console.log((error as Error).message)
 
  }
 
  /* finally{
    redirect(`/dashboard/sensors/${sensorId}`)

  } */

}
