"use client"

import { UserOnlineStatusDTO } from "@/app/supervisor/dashboard/components/OnlineUsers";
import dynamic from "next/dynamic";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client, over } from "stompjs";
import { UserProfile } from "./components/SensorComponents/SensorList";
import axios from "axios";
const NotificationComponent = dynamic(() => import('./NotificationComponent'), { ssr: false });

const NotificationComponentWrapper = ({session,enrichedNotifications} : {session:string , enrichedNotifications: {
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
      const [userProfile,setUserProfile] = useState<UserProfile>()
  
        useEffect(() => {
    axios.get(`http://localhost:8080/user/profile/${session}`, {
      headers: { Authorization: `Bearer ${session}` },
      withCredentials: true,
    })
    .then((res) => setUserProfile(res.data))
    .catch((err) => console.log(err));
  }, []);

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