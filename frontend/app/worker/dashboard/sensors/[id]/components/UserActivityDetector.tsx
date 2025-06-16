"use client"

import { useEffect, useRef, useState } from "react";
import { Client } from "stompjs";

const UserActivityDetector = ({session} : {session: string | undefined}) => {
      const [isOnline, setIsOnline] = useState(false);
      const timeoutRef = useRef<NodeJS.Timeout | null>(null);

 let stompClient: Client;
  const lastSentTimeRef = useRef(0);

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
   
        if (timeoutRef.current) {
            clearTimeout(timeoutRef.current)
        }
        timeoutRef.current = setTimeout(() => {
          sendHearthbeat(false)
            console.log("Kullanıcı inaktif");
        }, 10000) } 
        window.addEventListener("mousemove", handleMouseMove);

        return () => {
          window.removeEventListener("mousemove", handleMouseMove);
          if (timeoutRef.current) clearTimeout(timeoutRef.current)
        };
      }, [isOnline]);
    
  return null; 

}

export default UserActivityDetector