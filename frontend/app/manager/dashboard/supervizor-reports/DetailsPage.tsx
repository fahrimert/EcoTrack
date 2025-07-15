"use client"

import React, { useEffect, useState } from 'react'

import { Button } from '@/components/ui/button';
import { Dialog,DialogTrigger } from "@/components/ui/dialog";

import html2pdf from "html2pdf.js";
import axios from 'axios';
import SingleSensorFromPastSensors from './SingleSensorFromPastSensors';
import { PdfReport, PdfReportInduvual } from '../../types/types';
import { SensorDetailForWorkerPastSensor } from '@/app/supervisor/superVizorDataTypes/types';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';





const DetailsPage =  ({session,sensor} : {session:RequestCookie | undefined,sensor : PdfReport}) => {
   const [open,setOpen] = useState(false);
  console.log(sensor);
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


 
      const [sensorData,setSensorData] = useState< PdfReportInduvual>() 
    useEffect(() => {
    axios.get(`http://localhost:8080/manager/getPdfReportInduvualSensor/${sensor.originalSensorId}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) =>  setSensorData(res.data))
    .catch((err) => {
  console.log("Sensör verisi alınamadı:", err);
  setSensorData([]);
  })
  },[]); 
  


  console.log(sensorData);
  return (

    <div className='flex flex-col h-full w-full justify-start items-center'
    id='pdf-report'
    >

   
<SingleSensorFromPastSensors
sensor = {sensor }
   sensorData = {sensorData}
   /> 
   <div className='w-full h-fit flex justify-end items-end gap-[10px]'>



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