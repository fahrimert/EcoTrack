import { cookies } from "next/headers";
import React from "react";
import NotificationComponentWrapper from "./components/NotificationComponent/NotificationComponentWrapper";
import { DifferentUserProfileType, Notification, UserOnlineStatusDTO } from "@/app/sharedTypes";
import SensorsAndMap from "./components/SensorComponents/SensorsAndMap";
const page = async () => {
  const session = cookies().get("session");

  const responseProfileUser = await fetch(
    `http://localhost:8080/user/me`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${session?.value}`,
        "Content-Type": "application/json",
      },
    }
  );

  const responseProfileUserdata =
    (await responseProfileUser.json()) as DifferentUserProfileType;

  const response = await fetch(
    `http://localhost:8080/user/getNotifications/${responseProfileUserdata?.id}`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${session?.value}`,
        "Content-Type": "application/json",
      },
    }
  );



  const notifications = (await response.json()) as Notification[];
  const senderIds = [...new Set(notifications.map((n) => n.senderId))];

  const res = await fetch("http://localhost:8080/worker/getProfilesOfWorkers", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${session?.value}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(senderIds),
  });
  const users: UserOnlineStatusDTO[] = await res.json();
  const enrichedNotifications = notifications.map((notif) => {
    const sender = users.find((u) => u.id === notif.senderId);
    return {
      ...notif,
      sender,
    };
  });

  return (
    <>
      <div className=" h-fit w-full">
        <NotificationComponentWrapper
          session={session}
          enrichedNotifications={enrichedNotifications}
        />
        <SensorsAndMap session={session} />
      </div>
    </>
  );
};

export default page;
