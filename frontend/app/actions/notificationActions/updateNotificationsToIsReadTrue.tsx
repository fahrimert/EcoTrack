"use server";
import axios from "axios";
import {getServerApi} from '@/app/util/api'

  const api =  getServerApi()

export async function updateNotificationsToIsReadTrue() {
  try {
        const response = await api.put(
            `http://localhost:8080/notifications/workerUpdateNotificationMarkIsRead`,
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
