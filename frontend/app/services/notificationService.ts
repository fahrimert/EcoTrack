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
             return {
            serverData: responseJson,
          };
  


}
 catch (error) {
    return {
    
      serverError: "Bir Sorun Oluştu   ",
    };
  }
 

  },
 
};