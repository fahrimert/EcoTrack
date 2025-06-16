"use client";
import React from "react";
import SingleUserAndTheirSensor from "./SingleUserAndTheirSensor";


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
}

const SensorListAndUsers = ({
  sensorListData,
}: {
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
