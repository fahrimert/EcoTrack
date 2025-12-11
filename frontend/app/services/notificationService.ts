"use server";

import { getServerApi } from "@/app/util/api";

export const notificationService = {

    updateNotificationsToIsReadTrue: async (userId: number)=> {
    const api =  getServerApi();
    
    try {
const response = await api.put(
            `/notifications/workerUpdateNotificationMarkIsRead/${userId}`,
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
 

  },
 
};