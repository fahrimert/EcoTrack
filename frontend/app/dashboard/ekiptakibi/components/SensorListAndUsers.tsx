"use client";
import React, { useState } from "react";
import Sensor from "./SingleUserAndTheirSensor";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { ScrollArea } from "@/components/ui/scroll-area";
import SingleUserAndTheirSensor from "./SingleUserAndTheirSensor";
import { SourceContext } from "@/context/SourceContext";
import { DestinationContext } from "@/context/DestinationContext";
import { HoverContext } from "@/context/HoverContext";

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
      startTime: number;
      completedTime: number;
      note: null;
    }
  ];
  twoFactorAuthbeenverified: boolean;
}

const SensorListAndUsers = ({
  session,
  sensorListData,
}: {
  session: RequestCookie;
  sensorListData:  [{
    id: number;
    name: string;
    latitude: number;
    longitude: number;
    sensorlatitude: number;
    sensorlongitude: number;
    sensor: {
        id: number;
        sensorName: string;
        status: string;
        installationDate: string;
    };
}]| undefined;
}) => {

  return (

      <div className=" w-full h-fit flex flex-col bg-[#c2cecb] items-start justify-start     gap-[5px] rounded-[30px]">
        {sensorListData?.map((sensors) => (
          <SingleUserAndTheirSensor
            sensors={sensors}
          />
        ))}
      </div>

            



  );
  
};

export default SensorListAndUsers;
