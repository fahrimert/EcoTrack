export interface UserStats {
    [key: string]: number; 
  }

export interface TimeBasedUserStats {
    last_month?: UserStats;
    last_week?: UserStats;
    last_day?: UserStats;
  }

export type FullUserStats = TimeBasedUserStats[]


export type statusEnums  =   {
  ACTIVE: number;
  IN_REPAIR: number;
  FAULTY: number;
}


export type workerTimeBasedStatsGraph = {
    period: string;
    labels: string[];
    data: unknown[];
}[]

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

export interface UserAndSupervizorsDTO {
  id: number;
  firstName: string;
  surName: string;
  email:string
  role: "ADMIN" | "WORKER" | "MANAGER" | "SUPERVISOR";
  lastLoginTime:Date
  lastOnlineTime: Date
}




export interface SensorTypeCount {
    "name": string ,
    "count": number
  
}

export interface DateTypeCount {
    "date": string ,
    "count": number
  
}

export type UserDataMap = Record<string, number>;

export interface averageTaskMinutesChartData {
  averageChartData?: UserDataMap[];
}
export interface LeaderboardData {
  averageChartData?: UserDataMap[];
  totalSensorChartData?: UserDataMap[];
  last_day?: UserDataMap[];
}
export interface LeaderboardDataSuperVizor {
  SuperVizorGivenCompletedTasksAverageMinLastMonth?: UserDataMap[];
  SupervizorGivenTasksTotalCount?: UserDataMap[];
  SupervizorGivenTaskCountOfLastDay?: UserDataMap[];
}
export type LeaderboardResponse = LeaderboardData[];
export type LeaderboardResponseSuperVizor = LeaderboardDataSuperVizor[];


export interface RadarData {
  SupervizorTotalGivenTasksAverageMins?: number;
  SupervizorTotalGivenTasks?: number;
  SuperVizorLastDayGivenTaskCount?: number;
}


export interface RadarDataForWorker {
  GivenTasksSolvedAfterDeadlineRatioForWorker?: number;
  GivenTasksSolvedBeforeDeadlineRatioForWorker?: number;
  GivenTasksFinishedRatioForWorker?: number;
  GivenTasksAndSessionFinalStatusActıveRatioForWorker?: number;
  GivenTasksAndSessionFinalStatusNotActıveRatioForWorker?: number;
}
export type GroupedSensorDataOnPDFReport = {
  id: number;
  sensorName: string;
  sessionkey: {
    name: string;
    value: string;
  };
  userİd: number;
  username: string;
  status: string | null;
  installationDate: string;
  sessions: {
    id: number;
    sessionkey: {
      name: string;
      value: string;
    };
    startTime: string;
    completedTime: string;
    note: string;
  }[];
}
export interface GroupedSensorData {
  id: number;
  userİd:number
  sensorName: string;
  status: string;
  username:string
  installationDate: string;
  sessions: {
    id: number;
    startTime: string;
    completedTime: string;
    note: string;
  }[];
}
export interface ImageResponseDTO {
  name: string;
  type: string;
  base64Image: string;
}
export interface SensorDataDifferentOne {
  data : {
  id: number;
  sensorName: string;
  status: string;
  color_code: string;
  latitude: number;
  longitude: number;
  currentSensorSession: {  id: number;
  sensorName: string;
  displayName: string;
  color_code: string;
  note: string | null;
  startTime: string; // ISO formatta tarih
  completedTime: string | null;
  latitude: number;
  longitude: number;};
  
}

}
interface SensorData {
  id: number,
        sensorName: string,
        displayName: string,
        color_code: string,
        note: string,
        startTime: string,
        completedTime: string,
        latitude: number,
        longitude: number
  sensorİconİmage: ImageResponseDTO;

  imageResponseDTO: ImageResponseDTO[];
}

export interface UserProfilea {
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
  twoFactorCode: null;
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
  twoFactorAuthbeenverified: boolean;
}
export interface UserProfile {
   id: number;
  firstName: string;
  surName: string;
  role: string;
  userOnlineStatus: {
  id: number;
  isOnline: boolean;
  createdAt: string | null;
}
;
}

export interface SensorDetailForWorkerPastSensor{
    data: {
        id: number,
        sensorName: string,
        displayName: string,
        color_code: string,
        note: string,
        startTime: string,
         sensorIconImage: {
      name: string,
      type: string,
      base64Image:string }
        finalStatus:string,
        completedTime: string,
        latitude: number,
        longitude: number
  imageResponseDTO: ImageResponseDTO[];

      },
}
export interface Sensor {
    data: {
        id: number,
        sensorName: string,
        displayName: string,
        color_code: string,
        note: string,
        startTime: string,
        completedTime: string,
        latitude: number,
        longitude: number
  imageResponseDTO: ImageResponseDTO[];

      },
}

export interface SensorSessionWithser {
  sensorsid:number
  sensorsessionsid: number;
  sensorName: string;
  note: string;
  finalStatus: string | null;
  startTime: string; // ISO string formatı
  completedTime: string; // ISO string formatı
  userid: number;
  firstName: string;
  surName: string;
  role: 'WORKER' | 'SUPERVIZOR' | string; // ihtiyaca göre genişletilebilir
}