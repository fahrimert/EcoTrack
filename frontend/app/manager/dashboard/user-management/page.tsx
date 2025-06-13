import React, { useState } from 'react'
import OnlineUsers from './WorkersAndSupervizors'
import { cookies } from 'next/headers';

const page = async () => {
    
         const session = cookies().get("session");
    return (  


              <div  className=" w-full h-full bg-white rounded-[20px] p-[10px]  gap-[10px] mt-[10px]">
    <h2 className='text-[24px]'>Kullanıcı Listesi</h2>

              <OnlineUsers session ={session}/>
              </div>
  )
}

export default page