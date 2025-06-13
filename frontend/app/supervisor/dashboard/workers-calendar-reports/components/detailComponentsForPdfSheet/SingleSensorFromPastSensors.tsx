"use client"
import React from 'react'
import LeftStack from './LeftStack';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';


const SingleSensorFromPastSensors =  ({session,sessionId} : {
session:RequestCookie | undefined,
sessionId: string},
) => {
 
  return (
    <div className='w-full h-full flex flex-col justify-center items-center gap-[10px] bg-white'>
    <div className='relative  w-full h-full flex flex-row justify-center items-start gap-[20px] p-[5px] '>
   <LeftStack session = {session}  sessionId = {sessionId}  />
  </div>

</div>
  )
}

export default SingleSensorFromPastSensors