import React from 'react'
import LeftStack from './LeftStack';
import { Client } from '@googlemaps/google-maps-services-js';
import { ImageResponseDTO } from '@/app/supervisor/superVizorDataTypes/types';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import { PastSensorDetailDto } from '@/app/worker/types/types';
import { UserProfileDTO } from '@/app/sharedTypes';


// en son burada kaldım tipleri yazıp devam edecem
const SingleSensorFromPastSensors = async ({initialData ,userProfileDto } : {initialData :PastSensorDetailDto, 
  userProfileDto:UserProfileDTO
}) => {
    console.log("INIITALDATAA",initialData);
    const client = new Client({});
  const result = await client.reverseGeocode({
    params: {
      latlng: { lat: initialData.latitude, lng: initialData.longitude },
      key: 'AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E',
    },
  });
  const addressComponents = result.data.results[0].address_components;
 
  return (
    <div className='w-full h-fit flex flex-col justify-start items-center gap-[10px] bg-[#EEF0F3]'>
    <div className='relative  w-full h-fit flex flex-row justify-center items-start gap-[20px] p-[30px] '>
         <LeftStack userProfile = {userProfileDto} addressComponents = {addressComponents}  initialData = {initialData}/>
    </div>

</div>
  )
}

export default SingleSensorFromPastSensors