
import { getServerApi } from "@/app/util/api";
import { WorkerDashboardSensorDto } from "../worker/types/types";

export const sensorService = {
  
//bunun apisine net bişe diyemedim bu apiyi uygulamanın heryerinde kullanıyorum neredeyse
//DTO suna da bişey diyemedim kesin olarak o yüzden
  getWorkerDashboardSensors: async (): Promise<WorkerDashboardSensorDto[]> => {
    const api =  getServerApi();
    try {
      const response = await api.get(`/workerDashboard/sensors`);
      return response.data;
    } catch (error) {
      console.error("Sensör servisi hatası:", error);
      return [];
    }
  },


};