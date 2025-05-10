import React from 'react'
import LeftStack from './LeftStack';
import { Client } from '@googlemaps/google-maps-services-js';
import { ImageResponseDTO, Sensor } from '../page';


const SingleSensorFromPastSensors = async ({initialData,session} : {initialData :   { data: {
  id: number,
  sensorName: string,
  displayName: string,
  color_code: string,
  note: string,
  startTime: string,
  completedTime: string,
  latitude: number,
  longitude: number
  sensorİconİmage: ImageResponseDTO;

imageResponseDTO: ImageResponseDTO[];

},
session:string},
}) => {
    const client = new Client({});
  const result = await client.reverseGeocode({
    params: {
      latlng: { lat: initialData.data.latitude, lng: initialData.data.longitude },
      key: 'AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E',
    },
  });
  const addressComponents = result.data.results[0].address_components;
 
  return (
    <div className='w-full h-fit flex flex-col justify-start items-center gap-[10px] bg-[#EEF0F3]'>
    <div className='relative  w-full h-fit flex flex-row justify-center items-start gap-[20px] p-[30px] '>
         <LeftStack session = {session} addressComponents = {addressComponents}  initialData = {initialData}/>
    </div>

</div>
  )
}

export default SingleSensorFromPastSensors