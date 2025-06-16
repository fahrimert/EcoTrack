"use client"

import dynamic from "next/dynamic";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client, over } from "stompjs";
import { UserOnlineStatusDTO } from "@/app/supervisor/superVizorDataTypes/types";
import { useUserProfile } from "@/hooks/useUserProfile";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
const NotificationComponent = dynamic(() => import('./NotificationComponent'), { ssr: false });

const NotificationComponentWrapper = ({session,enrichedNotifications} : {session:RequestCookie | undefined , enrichedNotifications: {
    sender: UserOnlineStatusDTO | undefined;
    supervizorDescription: string;
    superVizorDeadline: string;
    createdAt: string;
    senderId: number;
    receiverId: number;
    taskId: number;
    isread:boolean
    id: number;
}[]    }) => {
  const [notification,setNotification] = useState<{
    sender: UserOnlineStatusDTO | undefined;
    supervizorDescription: string;
    superVizorDeadline: string;
    createdAt: string;
    senderId: number;
    receiverId: number;
    taskId: number;
    isread:boolean
    id: number;
} []>(enrichedNotifications || [])

    let stompClient: Client;
       const { userProfile, loading, error } = useUserProfile(session);


      useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws'); 
    stompClient = over(socket);
    stompClient.connect({}, (frame) => {
      console.log("Connected: " + frame); 
      stompClient.subscribe(`/topic/notifications/${userProfile?.id}`, (message) => {
        const updatedNotification = JSON.parse(message.body);
         setNotification(prevTasks =>{
            const exists = prevTasks.some(notif => notif.id === updatedNotification.id);
        if (exists) return prevTasks;

        return [updatedNotification, ...prevTasks]; // En başa eklemek için
         }
);
      });
    }, (error) => {
      console.error("WebSocket bağlantı hatası:", error);
    });

   
  }, [enrichedNotifications,userProfile?.id]);

  return (
    <div>
        <NotificationComponent session = {session} enrichedNotifications = {notification}/>
    </div>
  )
}

export default NotificationComponentWrapper