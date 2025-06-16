import React from 'react'
import LeftStack from './LeftStack';
import { Client } from '@googlemaps/google-maps-services-js';
import { SensorDetailForWorkerPastSensor } from '@/app/supervisor/superVizorDataTypes/types';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';


const SingleSensorFromPastSensors = async ({initialdata,session} : {initialdata :  SensorDetailForWorkerPastSensor
session:RequestCookie | undefined
}) => {
    const client = new Client({});
  const result = await client.reverseGeocode({
    params: {
      latlng: { lat: initialdata.data.latitude, lng: initialdata.data.longitude },
      key: 'AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E',
    },
  });
  const addressComponents = result.data.results[0].address_components;
 
  return (
    <div className='w-full h-fit flex flex-col justify-start items-center gap-[10px] bg-[#EEF0F3]'>
    <div className='relative  w-full h-fit flex flex-row justify-center items-start gap-[20px] p-[30px] '>
         <LeftStack session = {session} addressComponents = {addressComponents}  initialdata = {initialdata}/>
    </div>

</div>
  )
}

export default SingleSensorFromPastSensors