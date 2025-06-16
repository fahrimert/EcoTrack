"use server";
import axios from "axios";

export async function updateNotificationsToIsReadTrue(session:string,userId:string) {
  try {
        const response = await axios.put(
            `http://localhost:8080/notifications/workerUpdateNotificationMarkIsRead/${userId}`,
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
