"use client";
import React from "react";
import Sensor from "./Sensor";
import { ScrollArea } from "@/components/ui/scroll-area";
import { UserProfile, UserProfilea } from "../../superVizorDataTypes/types";

//interface for sensorlist



const SensorList = ({
userListData,
userProfile
}: {
  userListData: UserProfilea[] | undefined;
userProfile : UserProfilea
}) => {
//burada tüm userları dönüp online olanlara online offline olanlara offline dicez
  const userBasedsensor = userListData?.map(
    (g) =>
      g.id == userProfile?.sensorSessions![0].id
  );
  const customSensorListData = sensorListData?.map((sensor) => {
    const isUserSensor = userProfile?.sensorSessions!.some(
      (session) => session.id === sensor.currentSensorSession?.id
    );

    return {
      ...sensor,
      userBasedSensor: isUserSensor, 
    };
  });
  return (
    <ScrollArea className="h-screen  w-full">
      <div className=" w-full h-fit grid grid-cols-2 bg-[#c2cecb] items-start justify-start     gap-[5px] rounded-[30px] max-xl:grid max-xl:grid-cols-3 max-md:grid max-md:grid-cols-1">
        {customSensorListData?.map((sensors) => (
          <Sensor
            session={session}
            sensors={sensors}
            userProfile={userProfile}
            userBasedsensor={userBasedsensor}
          />
        ))}
      </div>
    </ScrollArea>
  );
};

export default SensorList;
