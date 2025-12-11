"use server";

import { userService } from "@/app/services/userService";
import { revalidatePath } from "next/cache";

export async function createWorkerLocationAction(lat: number, lng: number) {
  try {
    console.log(lat,lng);
    await userService.createWorkerLocation(lat, lng);
    revalidatePath("/worker/dashboard"); 
    return { success: true, message: "Konum başarıyla güncellendi." };
  } catch (error) {
    return { success: false, message: "Konum güncellenemedi." };
  }
}