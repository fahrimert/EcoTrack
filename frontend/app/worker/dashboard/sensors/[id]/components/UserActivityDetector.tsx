"use client"

import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";
import { Client, over } from "stompjs";
//useRefi bayadır kullanmıyom unutmuşum useRef genel olarak referans değeri tutmaya yarıyor.

const UserActivityDetector = ({session} : {session: string | undefined}) => {
      const [isOnline, setIsOnline] = useState(false);
      const timeoutRef = useRef<NodeJS.Timeout | null>(null);
      //setTimeout ile başlatılan zamanlayıcıyı tutuyor

 let stompClient: Client;
  const lastSentTimeRef = useRef(0); // Son gönderim zamanını saklamak için

 const sendHearthbeat = (isOnline) => {
  
fetch("http://localhost:8080/hearthbeat",{
  method:"POST",
       headers: {
        Authorization: `Bearer ${session}`,
        "Content-Type": "application/json",
      },
    body:JSON.stringify({isOnline})
}

)
 }
 

      useEffect(() => {
         if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    const handleMouseMove = () => {

        const now = Date.now()

        if (now -lastSentTimeRef.current > 10000 ) {
          sendHearthbeat(true)
              console.log("Kullanıcı aktif");
          lastSentTimeRef.current = now
        }
   
        //hareket edince direkt state true yapılır
        if (timeoutRef.current) {
            clearTimeout(timeoutRef.current)
        }
        //timeoutref sayesinde 10 saniye sonra resetleyebiliyon ona referans verdiriyoz
        timeoutRef.current = setTimeout(() => {
          sendHearthbeat(false)
            console.log("Kullanıcı inaktif");
        }, 10000) } 
        //eğer 10 saniye geçerse onlineyi false yaptır
        window.addEventListener("mousemove", handleMouseMove);
    //her mouse movementinda bu fonksiyonu çağır


        // the function returned here will remove, or "clean up", the event listener
        return () => {
          window.removeEventListener("mousemove", handleMouseMove);
          if (timeoutRef.current) clearTimeout(timeoutRef.current)
        };
      }, [isOnline]);
    
  return null; 

}

export default UserActivityDetector