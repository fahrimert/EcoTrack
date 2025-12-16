import { ImageResponseDTO } from "@/app/supervisor/superVizorDataTypes/types";

export interface WorkerDashboardSensorDto {
  id:number
  sensorName: string;
  status: string;
  colorCode: string;
  latitude: number;
  longitude: number;
  currentSensorSession:
    | {
        id: number;
        startTime: string;
        userId: number | null;
      }
    | undefined;
}

export interface WorkerDashboardTaskWithSensorFixDto {
  id: number;
  note: string | null;
  startTime: string; 
  completedTime: string | null;
}
export interface WorkerDashboardTaskWithSensorDto {
  id: number;
  sensorName: string;
  status: string;    
  color_code: string; 
  latitude: number;
  longitude: number;
  currentSensorSession: WorkerDashboardTaskWithSensorFixDto | null;
}

export interface WorkerDashboardTaskSensorWithTaskDto {
  id: number;
  
  taskSensors: WorkerDashboardTaskWithSensorDto; 
  
  superVizorDescription: string;
  superVizorDeadline: string;
  assignedBy: {
    id: number;
    firstName: string;
    surName: string;
  };
  workerArriving: boolean | null;
  workerArrived: boolean | null;
  worker_on_road_note: string | null;
  solvingNote: string | null;
  taskCompletedTime: string | null;
}


export interface SensorSolvingSensorDto {
  id: string;
  sensorName: string;
  status: string;    
  color_code: string; 
  latitude: number;
  longitude: number;
  currentSensorSession: SensorSolvingSensorFixDto | null;
}

export interface SensorSolvingSensorFixDto {
  id: number;
  note: string | null;
  startTime: string; 
  completedTime: string | null;

  userId: number | null;
}

export interface PastSensorsDto {
  sensorId: string;
  sensorName: string;
  status: string;    
  installationDate: string; 
  sessions: PastSensorsSensorFixDto[] | null;
}


export interface PastSensorsSensorFixDto {
  id: string;
  startTime: string;
  completedTime: string;    
  note: string; 
}

export interface PastSensorDetailDto {
  sensorId: string;
  sensorName: string;
  sensorStatus: string;
  iconImage: ImageResponseDTO;
  sessionId: string;
  note: string;
  finalStatus: string;
  startTime: string;
  completedTime: string;
  latitude: number;
  longitude: number;
  evidenceImages: ImageResponseDTO[];
}


export interface CrewJobsUserAndSessionSensorDTO{
     workerId: string;
 workerName: string;
  workerLatitude: number;
  workerLongitude: number;
  isOnline:boolean
   sensorId: string;
 sensorName: string;
 sensorStatus: string;
  sensorLatitude: number;
  sensorLongitude: number;

   sessionIde: string;
 startTimee: string;
}