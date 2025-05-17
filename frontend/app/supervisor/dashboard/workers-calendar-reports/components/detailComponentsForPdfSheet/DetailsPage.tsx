"use client"

import React, { useEffect, useState } from 'react'
import { cookies } from 'next/headers'
import { Client } from '@googlemaps/google-maps-services-js';
import { UserProfile } from '@/app/components/SensorComponents/SensorList';
import Heading from './Heading';
import SingleSensorFromPastSensors from './SingleSensorFromPastSensors';
import axios from 'axios';
import { Button } from '@/components/ui/button';

import html2pdf from "html2pdf.js";

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
 
}
const DetailsPage =  ({session,sessionId} : {session:string,sessionId: string}) => {

   const downloadPDF = () => {
    const element = document.getElementById("pdf-report");
    if (!element) return;
    html2pdf().set({
      margin: 0.5,
      filename: `sensör-raporu.pdf`,
      image: { type: "jpeg", quality: 0.98 },
      html2canvas: { scale: 2 },
      jsPDF: { unit: "in", format: "a4", orientation: "portrait" },
    }).from(element).save();
  };
  return (

    <div className='flex flex-col h-full w-full justify-start items-center'
    id='pdf-report'
    >

   
<SingleSensorFromPastSensors
    session = {session}
    sessionId = {sessionId}
   />
   <div className='w-full h-fit flex justify-end items-end'>
<Button 
    variant={null
    }
        onClick={downloadPDF}
        className="mt-4 px-4 py-2 bg-blue-600 text-white rounded"
      >Sensör Raporunu Pdf İndir</Button>

   </div>
    
    </div>
  )
}

export default DetailsPage