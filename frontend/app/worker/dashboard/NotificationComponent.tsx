"use client"
import { UserOnlineStatusDTO } from '@/app/supervisor/dashboard/components/OnlineUsers';
import React, { useEffect, useState } from 'react'
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { IoNotifications } from "react-icons/io5";
import { ScrollArea } from '@/components/ui/scroll-area';
import { format } from 'date-fns';
import Link from 'next/link';
import { Client, over } from 'stompjs';
import SockJS from 'sockjs-client';
import { Sensor } from './past-sensors/[id]/page';
import { tr } from 'date-fns/locale';
import { UserProfile } from './components/SensorComponents/SensorList';
import axios from 'axios';
import { updateNotificationsToIsReadTrue } from '@/app/actions/notificationActions/updateNotificationsToIsReadTrue';
const NotificationComponent = ({session , enrichedNotifications} : {
  session:string,
  
  enrichedNotifications: {
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
  const [userProfile,setUserProfile] = useState<UserProfile>()
    useEffect(() => {
    axios.get(`http://localhost:8080/user/profile/${session}`, {
      headers: { Authorization: `Bearer ${session}` },
      withCredentials: true,
    })
    .then((res) => setUserProfile(res.data))
    .catch((err) => console.log(err));
  }, []);
  const makeMyNotificationRead = async(userId:string ) => {
 try {
    const response =  await updateNotificationsToIsReadTrue(session,userId)
  console.log(response);
 } catch (error) {
    console.log(error);

 }

  }

  
/*   const [notifications,setNotification] = useState< {
    sender: UserOnlineStatusDTO | undefined;
    supervizorDescription: string;
    superVizorDeadline: string;
    createdAt: string;
    senderId: number;
    receiverId: number;
    taskId: number;
    id: number;
}[]>(enrichedNotifications) */

    
     /*  let stompClient: Client;
    
      useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws'); 
        stompClient = over(socket);
        stompClient.connect({}, (frame) => {
          console.log("Connected: " + frame); 
          stompClient.subscribe('/topic/notifications', (message) => {
            const updatedNotification = JSON.parse(message.body);
            setNotification(updatedNotification)
          });
        }, (error) => {
          console.error("WebSocket bağlantı hatası:", error);
        });
    
       
      }, []); */

      /* buradaki notificationdaki göreve git mevzusu deadlinenin */
  return (
 <div  className=" h-fit w-full flex justify-end items-end mt-[10px]">
                 <Popover>
      <PopoverTrigger onClick={() => {
    if (enrichedNotifications?.some(n => !n.isread)) {
      makeMyNotificationRead(userProfile?.id);
    }
  }}   className="w-fit h-fit relative mr-[10px] ">
           <Badge variant="destructive">
        <h2 className= "border-transparent bg-primary   text-primary-foreground shadow hover:bg-[#cacaca] gap-[10px]">

          <IoNotifications size={30} color=" black
          "/>
        </h2>
     <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs font-bold px-1.5 py-0.5 rounded-full">
    {enrichedNotifications.filter(n => !n.isread).length }
  </span>
        </Badge>
      </PopoverTrigger>
      <PopoverContent className="w-fit bg-white gap-[5px] flex flex-col ">

        <h2 className="text-[24px]">Görev Bildirimleri</h2>
    <ScrollArea className="h-[400px]  w-fit gap-[10px]">
      <div className="w-full h-fit flex flex-col  p-[10px] rounded-[5px] gap-[10px]">
        {enrichedNotifications.map((c) => (
<div className='gap-[10px] bg-[#cacaca] rounded-[10px] p-[5px]'>

        <div className="w-full h-fit flex flex-col">
          <h2 className="text-[20px]">Gönderen : {c.sender?.firstName}</h2>
        </div>
<div className="w-full h-fit flex flex-col">
          <h2>Görev Açıklama: {c.supervizorDescription}</h2>
          <h2>Görev Bitimi Deadline: {format(new Date(c.superVizorDeadline), 'dd MMMM yyyy HH:mm', { locale: tr })}</h2>
        </div>
                      <span className="font-medium">
                        Görevin Oluşturulma Tarihi : {format(new Date(c.createdAt), 'dd MMMM yyyy HH:mm', { locale: tr })}
                      </span>
                      {c.superVizorDeadline - Date.now() > 0 ? 
                      <Link className="w-fit flex flex-col justify-end items-end p-[5px]" href={`/worker/dashboard/tasks/${c.taskId}`}>
<div className="w-fit h-fit ">
  
                      <span className="font-medium bg-black text-white p-[5px] rounded-lg">
                     Göreve Git
                      </span>
</div>
        
                      
                      </Link> : null}

</div>

        ))}
      
      </div>
    </ScrollArea>

      </PopoverContent>
    </Popover> 
 </div>
  )
}

export default NotificationComponent