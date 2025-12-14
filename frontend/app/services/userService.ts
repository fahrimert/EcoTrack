
import { getServerApi } from "@/app/util/api";
import { UserProfileDTO,  EnrichedNotification, UserLocationDTO } from "@/app/sharedTypes"
import { TaskSensorWithTask } from "../worker/dashboard/components/SensorComponents/SensorsAndMap";
import { WorkerDashboardTaskSensorWithTaskDto } from "../worker/types/types";

export const userService = {
  
//bunun apisine net bişe diyemedim bu apiyi uygulamanın heryerinde kullanıyorum neredeyse
//DTO suna da bişey diyemedim kesin olarak o yüzden
  getProfile: async (): Promise<UserProfileDTO> => {
    const api =  getServerApi();
    try {
      const response = await api.get("/user/me");
      return response.data; 
    } catch (error) {
      console.error("Profil çekilemedi:", error.message);
      throw new Error("Profil yüklenirken hata oluştu.");
    }
  },

   getUserLocation: async (): Promise<UserLocationDTO> => {
    const api =  getServerApi();
    try {
      const response = await api.get("/user/getUserLocation");
      return response.data; 
    } catch (error) {
      console.error("Profil çekilemedi:", error.message);
      throw new Error("Profil yüklenirken hata oluştu.");
    }
  },


  //bunun apisini tek bir apide toparladım 2 api call yerine 
  getEnrichedNotifications: async (): Promise<EnrichedNotification[]> => {
    const api =  getServerApi();
    
    try {
      const notifResponse = await api.get(`/user/getNotifications`);
      const enrichedNotifications = notifResponse.data as EnrichedNotification[];

      return enrichedNotifications;

    } catch (error) {
      console.error("Bildirim servisi hatası:", error);
      return []; 
    }
  },


    getSensorListFromTasksOfSingleUser: async (userId: number): Promise<WorkerDashboardTaskSensorWithTaskDto[]> => {
    const api =  getServerApi();
    
    try {
      const response = await api.get(`/workerDashboard/getTasksOfMe/${userId}`);

      return response.data;
    } catch (error) {
      console.error("User Görev servisi hatası:", error);
      return [];
    }
  },

    getSensorListFromTasksOfSingleUser: async (userId: number): Promise<WorkerDashboardTaskSensorWithTaskDto[]> => {
    const api =  getServerApi();
    
    try {
      const response = await api.get(`/workerDashboard/getTasksOfMe/${userId}`);

      return response.data;
    } catch (error) {
      console.error("User Görev servisi hatası:", error);
      return [];
    }
  },
    createWorkerLocation: async (lat:number , lng:number,) => {
    const api =  getServerApi();
      
    try {
      const response = await api.post(`/workers/saveWorkersLocation?lat=${lat}&longtitude=${lng}`);

      return response.data;
    } catch (error) {
      console.error("User Location servisi hatası:", error);
      return [];
    }
  },

    workerDashboardGoToSensor: async (sensorId: number) => {
    const api =  getServerApi();
      
    try {
      const response = await api.put(`/sensor/goToThesensorSessionNotTheTask/${sensorId}`);
      return { success: true, data: response.data };
    } catch (error) {
     console.error("User Go To Sensor Methodu hatası:", error);
    const errorMessage = error.response?.data || "Beklenmedik bir hata oluştu.";
    return { success: false, error: errorMessage };
    }
  },
  getDashboardData: async () => {
    const profile = await userService.getProfile();
    
    const notifications = await userService.getEnrichedNotifications(profile.id);

    return {
      userProfile: profile,
      notifications: notifications
    };
  }

  
};