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