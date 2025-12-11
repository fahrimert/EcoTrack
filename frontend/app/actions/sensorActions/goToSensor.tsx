"use server";
import { userService } from "@/app/services/userService";

export async function goToSensor(sensorId: number) {
  const result = await userService.workerDashboardGoToSensor(sensorId);

  if (result.success) {
    return {
      serverData: result.data,
    };
  } else {
    return {
      serverError: result.error, 
    };
  }
}