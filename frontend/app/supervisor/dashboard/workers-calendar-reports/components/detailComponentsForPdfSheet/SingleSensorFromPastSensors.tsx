"use client"
import React from 'react'
import LeftStack from './LeftStack';
import { Client } from '@googlemaps/google-maps-services-js';
import { ImageResponseDTO } from './DetailsPage';


const SingleSensorFromPastSensors =  ({session,sessionId} : {
session:string,
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