
import { getServerApi } from "@/app/util/api";
import { SensorSolvingSensorDto, WorkerDashboardSensorDto } from "../worker/types/types";

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

    getInduvualSensorForSensorSolving: async (sensorId: string): Promise<SensorSolvingSensorDto> => {
    const api =  getServerApi();
    try {
      const response = await api.get(`/${sensorId}/nonTaskSolvingDetails`);
      return response.data.data;
    } catch (error) {
      console.error("Sensör servisi hatası:", error);
      console.log("errormessage",error.message);
      throw new Error("Sensor Servisi hatası oluştu.");
    }
  },

    solveNonTaskSensor: async (sensorId: string,formData: FormData): Promise<SensorSolvingSensorDto> => {
    const api =  getServerApi();
    try {
      const response = await api.put(`/worker/nonTaskSensorSolving/${sensorId}` ,formData);
      return response.data;
    } catch (error) {
      console.error("Sensör servisi hatası:", error);
      console.log("errormessage",error.message);
      throw new Error("Sensor Servisi hatası oluştu.");
    }
  },


};