import { cookies } from "next/headers";
import React from "react";
import SensorsAndMap, { Notification } from "./components/SensorComponents/SensorsAndMap";


import NotificationComponentWrapper from "./NotificationComponentWrapper";
import { UserOnlineStatusDTO, UserProfilea } from "@/app/supervisor/superVizorDataTypes/types";
const page = async () => {
  const session = cookies().get("session");


             const responseProfileUser = await fetch(`http://localhost:8080/user/profile/${session?.value}`, {
                  method: "GET",
                  headers: {
                    Authorization: `Bearer ${session?.value}`,
                    "Content-Type": "application/json",
                  },
                });
                const responseProfileUserdata = await responseProfileUser.json() as UserProfilea
             const response = await fetch(`http://localhost:8080/notifications/getNotifications/${responseProfileUserdata?.id}`, {
                  method: "GET",
                  headers: {
                    Authorization: `Bearer ${session?.value}`,
                    "Content-Type": "application/json",
                  },
                });


      
            
                const notifications = await response.json() as Notification[]
                const senderIds = [...new Set(notifications.map(n =>n.senderId))]; 
const res = await fetch("http://localhost:8080/user/getProfilesOfUsers", {
  method: "POST",
  headers: {
    Authorization: `Bearer ${session?.value}`,
    "Content-Type": "application/json",
  },
  body: JSON.stringify(senderIds),
});
const users: UserOnlineStatusDTO[] = await res.json();

const enrichedNotifications = notifications.map((notif) => {
  const sender = users.find(u => u.id === notif.senderId);
  return {
    ...notif,
    sender,
  };
});


  return (
    <>
      <div className=" h-fit w-full">
    
<NotificationComponentWrapper session = {session} enrichedNotifications = {enrichedNotifications}/>


        <SensorsAndMap session={session} />
      </div>
    </>
  );
};

export default page;
