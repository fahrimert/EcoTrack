export interface DifferentUserProfileType {
  id: number;
  email: string;
  firstName: string;
  surName: string;
  password: string;
  refreshToken: {
    token: string;
    expiresAt: string;
    id: number;
  };
  role: string;
  sensorSessions: [
    {
      id: number;
      sensor: {
        id: number;
        sensorName: string;
        status: string;
        installationDate: number;
      };
      startTime: string;
      completedTime: string;
      note: null;
    }
  ] | undefined;
}

export interface hooksUserTypes {
  id:number;
    firstName:string;
    surName:string;
    role:string;
    userOnlineStatus: {  
      id: number;
  isOnline: boolean;
  lastOnlineTime: string;
  createdAt: string; }

}

export interface UserOnlineStatusDTO {
  id: number;
  firstName: string;
  surName: string;
  role: "ADMIN" | "WORKER" | "MANAGER" | "SUPERVISOR";
  userOnlineStatus: {
    id: number;
    isOnline: boolean;
    createdAt: string | null;
  } | null;
}


export interface Notification {
  supervizorDescription: string;
  superVizorDeadline: string; 
  createdAt: string;          
  senderId: number;
  receiverId: number;
  taskId: number;
  isread: boolean,
  id: number;
}