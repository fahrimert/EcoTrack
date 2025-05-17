"use client";
import React from "react";
import Sensor from "./Sensor";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { ScrollArea } from "@/components/ui/scroll-area";

//interface for sensorlist
export interface SensorList {
  sensorName: string;
  status: string;
  colorCode: string;
  latitude: number;
  longitude: number;
  currentSensorSession:
    | {
        id: number;
        sensor: {
          id: number;
          sensorName: string;
          status: string;
          installationDate: string;
        };
        startTime: string;
        completedTime: null;
        note: null;
      }
    | undefined;
}
/* interface for user profile data */
export interface UserProfile {
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
  ];
  twoFactorAuthbeenverified: boolean;
}

const SensorList = ({
  session,
  sensorListData,
  userProfile,
}: {
  session: RequestCookie;
  sensorListData: SensorList[] | undefined;
  userProfile: UserProfile | undefined;
}) => {

  const userBasedsensor = sensorListData?.map(
    (g) =>
      g.currentSensorSession &&
      g.currentSensorSession.id == userProfile?.sensorSessions[0].id
  );
  const customSensorListData = sensorListData?.map((sensor) => {
    const isUserSensor = userProfile?.sensorSessions.some(
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
