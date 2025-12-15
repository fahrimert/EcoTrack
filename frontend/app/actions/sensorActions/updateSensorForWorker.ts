"use server";

import { sensorService } from "@/app/services/sensorService";

export async function updateSensorForWorker(
  formData: FormData,
  sensorId: string
) {
  try {
    const result = await sensorService.solveNonTaskSensor(sensorId, formData);

    return {
      serverData: result,
    };
  } catch (error) {
    console.log((error as Error).message);
    return {
      serverError: (error as Error).message,
    };
  }
}
