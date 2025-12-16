import React from "react";
import Heading from "./components/Heading";
import { userService } from "@/app/services/userService";
import PastSensorList from "./components/PastSensorList";

const page = async () => {

 let pastSensors = [];

  try {
    const response = await userService.getWorkerPastSensors();
    pastSensors = response.data || [];
  } catch (error) {
    console.error("Geçmiş sensörler yüklenemedi:", error);
  }
  return (
    <>
<div className="flex flex-col h-full w-full p-6 space-y-6">        <Heading
          title={"Geçmişte Uğraştığınız Sensörler"}
     description="Tamamladığınız tüm sensör bakım ve onarım görevlerinin listesi."
        />
        <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-4">

        <PastSensorList pastSensors={pastSensors} />
        </div>

      </div>
    </>
  );
};

export default page;
