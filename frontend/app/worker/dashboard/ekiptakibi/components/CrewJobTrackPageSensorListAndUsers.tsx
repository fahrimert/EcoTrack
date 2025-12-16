"use client";
import React from "react";
import SingleUserAndTheirSensor from "./SingleUserAndTheirSensor";
import { CrewJobsUserAndSessionSensorDTO } from "@/app/worker/types/types";
import { UserProfileDTO } from "@/app/sharedTypes";
import Heading from "../../past-sensors/[id]/components/Heading";
import { ScrollArea } from "@/components/ui/scroll-area";



const CrewJobTrackPageSensorListAndUsers = ({
  userProfile,
  crewJobTrackPageSensorsAndTheirLocations,
}: {
  userProfile: UserProfileDTO
  crewJobTrackPageSensorsAndTheirLocations:  CrewJobsUserAndSessionSensorDTO[]
}) => {
console.log("CREWJOBTRACKPAGESENSORANDLOCATİONSS",crewJobTrackPageSensorsAndTheirLocations);
  return (
    <div className="h-full w-full bg-white border border-gray-200 shadow-lg rounded-2xl flex flex-col overflow-hidden">
      <div className="p-6 border-b border-gray-100 bg-gray-50/50">
        <Heading 
            title="Saha Ekipleri" 
            description="Şu an aktif görevde olan personeller ve konumları." 
        />
      </div>
      
      <ScrollArea className="flex-1 bg-gray-50/30">
        <div className="p-4 flex flex-col gap-3">
          {crewJobTrackPageSensorsAndTheirLocations.length > 0 ? (
            crewJobTrackPageSensorsAndTheirLocations.map((crew) => (
              <SingleUserAndTheirSensor
                userProfile={userProfile}
                crew={crew}
              />
            ))
          ) : (
            <div className="text-center text-gray-400 py-10">
                Aktif çalışan ekip bulunmuyor.
            </div>
          )}
        </div>
      </ScrollArea>
    </div>
  );
  
};

export default CrewJobTrackPageSensorListAndUsers;
