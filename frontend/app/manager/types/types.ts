import { ImageResponseDTO } from "@/app/supervisor/superVizorDataTypes/types";


export type  ManagerNotificationType = {
    sensorName: string;
    senderName: string;
    id: number;
    isRead:boolean

}[]

export interface PdfReport {
  originalSensorId:number
  sensorId: number;
  sensorName: string;
  technicianNote: string;
  startTime: string; 
  completedTime: string;
  latitude: number;
  longitude: number;
  createdAt: string;
 managerId:number;
     supervizorId:number;
}

export interface PdfReportInduvual{
  success: boolean;
  message: string;
  data: {
    id: number;
    sensorName: string;
    displayName: string;
    color_code: string;
    imageResponseDTO: any[]; 
    sensorIconImage: {
      name: string;
      type: string;
      base64Image: string;
    };
  };
  errors: any; 
  status: number;
   
}