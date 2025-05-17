"use client"
import React, { useCallback, useEffect, useState } from 'react'
import { Client, over } from "stompjs";
import SockJS from "sockjs-client";
import OnlineUser from './OnlineUser';


export interface UserOnlineStatusDTO {
  id: number;
  firstName: string;
  surName: string;
  role: 'ADMIN' | 'WORKER' | 'MANAGER' | 'SUPERVISOR';
  userOnlineStatus: {
    id: number;
    isOnline: boolean;
    createdAt: string | null;
  } | null;
}

  
const OnlineUsers = () => {
  const [mapLoaded, setMapLoaded] = useState(false);
      const [map, setMap] = useState<google.maps.Map | null>(null);

const [onlineUsers, setOnlineUsers] = useState<UserOnlineStatusDTO[]>([]);
      const onUnmount = useCallback(() => setMap(null), []);
let stompClient: Client;

    //burda tüm userları gösterecez sadece bunu eşleşenleri online diye gösterecez onu da backgroundu yeşil yaparız 

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws-users'); 
    stompClient = over(socket);
    stompClient.connect({}, (frame) => {
      console.log("Connected: " + frame); 
      stompClient.subscribe('/topic/users', (message) =>  
        {const user = JSON.parse(message.body) as  UserOnlineStatusDTO[];
          setOnlineUsers(user)});
    }, (error) => {
      console.error("WebSocket bağlantı hatası:", error);
    });
  }, [onlineUsers]);
  return (
<div className='w-full h-fit'>
  <div className=" w-full h-fit grid grid-cols-3 bg-[#c2cecb] items-center justify-center     gap-[5px] rounded-[30px] max-xl:grid max-xl:grid-cols-2 max-md:grid max-md:grid-cols-1">
    {onlineUsers.length == 0  ?  
    
    (
      <div className=' w-full h-[300px] flex items-center justify-center '>
        <h2 className='w-fit h-fit text-[24px] text-white flex items-center justify-center'>

        Herhangi Bir Online Kullanıcı bulunmamakta
        </h2>
      </div>
    ) : 
    <>
    
    {onlineUsers.map((c) => (
          <OnlineUser user = {c}/>
          
        ))}  
    </>
        
        }

        </div>

</div>
  )
}

export default OnlineUsers