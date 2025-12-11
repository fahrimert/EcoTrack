"use client"

import dynamic from "next/dynamic";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client, over } from "stompjs";
import { EnrichedNotification } from "@/app/sharedTypes";
import { updateNotificationsToIsReadTrue } from "@/app/actions/notificationActions/updateNotificationsToIsReadTrue";
const NotificationComponent = dynamic(() => import('./NotificationComponent'), { ssr: false });
  interface WrapperProps {
  userId: number;
  enrichedNotifications: EnrichedNotification[];
  }
const NotificationComponentWrapper = ({ userId,enrichedNotifications} : WrapperProps) => {


  const [notification,setNotification] = useState<EnrichedNotification[]>(enrichedNotifications || [])
const unreadCount = notification.filter(n => !n.isread).length;
    let stompClient: Client;
      useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws'); 
    stompClient = over(socket);
    stompClient.connect({}, (frame) => {
      console.log("Connected: " + frame); 
stompClient.subscribe(`/topic/notifications/${userId}`, (message) => { 
         const updatedNotification = JSON.parse(message.body);
         setNotification(prevTasks =>{
            const exists = prevTasks.some(notif => notif.id === updatedNotification.id);
        if (exists) return prevTasks;

        return [updatedNotification, ...prevTasks];
         }
);
      });
    }, (error) => {
      console.error("WebSocket  hatası var ", error);
    });

   
  }, [enrichedNotifications,userId]);

  //bunu da servisten alabilirim ama şimdilik böyle kalsın actiondan alınacak şekilde servisten alma yapıya daha çok uyabilir ama
  const handleMarkAsRead = async () => {
    if (unreadCount === 0) return;

    const updatedList = notification.map(n => ({ ...n, isRead: true }));
    setNotification(updatedList);

    try {
       await updateNotificationsToIsReadTrue(String(userId));
    } catch (error) {
       console.error("Bildirim okundu hatası:", error);
    }
  };
  return (
    <div>
        <NotificationComponent 
        notifications = {notification}
        onMarkAsRead={handleMarkAsRead}
        unreadCount={unreadCount}
        />
    </div>
  )
}

export default NotificationComponentWrapper