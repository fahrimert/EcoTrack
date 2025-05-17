import React, { useState } from 'react'
import Heading from '../components/Heading'
import SingleSensorFromPastSensors from './components/SingleSensorFromPastSensors'
import { cookies } from 'next/headers'
import { Client } from '@googlemaps/google-maps-services-js';
import { UserProfile } from '@/app/components/SensorComponents/SensorList';
export interface ImageResponseDTO {
  name: string;
  type: string;
  base64Image: string;
}

interface SensorData {
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
}



export interface Sensor {
    data: {
        id: number,
        sensorName: string,
        displayName: string,
        color_code: string,
        note: string,
        startTime: string,
        completedTime: string,
        latitude: number,
        longitude: number
  imageResponseDTO: ImageResponseDTO[];

      },
}
const page = async ({params} : {params:{id:string}}) => {
    const session =   cookies().get("session")?.value
    const response = await fetch(`http://localhost:8080/sensors/getPastSensorDetail/${params.id}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${session}`,
        'Content-Type': 'application/json'
      }
    });
    
    const initialdata = await response.json() as  SensorData
    

  return (

    <div className='flex flex-col h-fit w-full'>

    <Heading 
    title={"Geçmişte Uğraştığınız Sensörler"}
    description={"Çözdüğünüz veya Çözemediğiniz Tüm Geçmişteki Sensörler Bu Sayfada gözükür "}
    />
    <SingleSensorFromPastSensors 
    session = {session}
    initialData = {initialdata}/>

    </div>
  )
}

export default page