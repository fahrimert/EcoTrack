import React from "react";
import { cookies } from "next/headers";
import AssignedSensorAndMap from "./components/AssignedSensorAndMap";
import { sensorService } from "@/app/services/sensorService";
import { userService } from "@/app/services/userService";
import { redirect } from "next/navigation";

const page = async ({ params }: { params: { id: string } }) => {
  const session = cookies().get("session");

let singleSensor = null;
  let userLocation = null;

  try {
      [singleSensor, userLocation] = await Promise.all([
          sensorService.getInduvualSensorForSensorSolving(params.id),
          userService.getUserLocation()
      ]);
  } catch (error) {
      console.error("Sensör sayfasına erişim hatası:", error);
      redirect("/worker/dashboard?error=access_denied");
  }

  if (!singleSensor.currentSensorSession) {
       redirect("/worker/dashboard?error=session_closed");
 }


  return (
    <>
    <main className="w-full min-h-screen bg-gray-50/50">
      <AssignedSensorAndMap initialData={singleSensor} userLocation = {userLocation} session={session} />
    </main>

    </>
  );
};

export default page;
