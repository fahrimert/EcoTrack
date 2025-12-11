import React from "react";
import NotificationComponentWrapper from "./components/NotificationComponent/NotificationComponentWrapper";
import SensorsAndMap from "./components/SensorComponents/SensorsAndMap";
import { userService} from "@/app/services/userService";
import { sensorService } from "@/app/services/sensorService";
const page = async () => {
  try {
    const { userProfile, notifications } = await userService.getDashboardData();
 const [sensorList, workerSensors,location] = await Promise.all([
  userService.getSensorListFromTasksOfSingleUser(userProfile.id),
  sensorService.getWorkerDashboardSensors(),
  userService.getUserLocation()
]);
    return (
      <div className="h-fit w-full flex flex-col gap-6">
        
        <NotificationComponentWrapper
          userId={userProfile.id}
          enrichedNotifications={notifications} 
        />


        <SensorsAndMap 
        sensorListFromtTaskOfSingleUser = {sensorList}
        workerDashboardSensors = {workerSensors}
         userProfile={userProfile}
         userLocation = {location} />
        
      </div>
    );
  } catch (error) {
    return (
      <div className="flex h-screen items-center justify-center text-red-500">
        <h1>Veriler yüklenirken bir sorun oluştu. Lütfen tekrar giriş yapın.</h1>
      </div>
    );
  }
};

export default page;
