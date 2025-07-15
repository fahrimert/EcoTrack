"use client"

import React, { useEffect, useState } from 'react'

import SingleSensorFromPastSensors from './SingleSensorFromPastSensors';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogTrigger } from "@/components/ui/dialog";

import html2pdf from "html2pdf.js";
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import axios from 'axios';
import SendToManagerComponent from './SendToManagerComponent';
import { DifferentUserProfileType, hooksUserTypes } from '@/app/sharedTypes';

export interface ImageResponseDTO {
  name: string;
  type: string;
  base64Image: string;
}





const DetailsPage =  ({session,sessionId} : {session:RequestCookie | undefined,sessionId: string}) => {
   const [open,setOpen] = useState(false);

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
   const [data,setData] = useState<  {data:{
        id: number,
        sensorName: string,
        displayName: string,
        color_code: string,
        note: string,
        status:string
        startTime: string,
        completedTime: string,
        latitude: number,
        longitude: number
  imageResponseDTO:  {
  name: string;
  type: string;
  base64Image: string;
}[];

      }}>()
  

      
      useEffect(() => {
        axios.get(`http://localhost:8080/worker/getPastNonTaskSensorDetail/${sessionId}`, {
          headers: { Authorization: `Bearer ${session?.value}` },
          withCredentials: true,
        })
        .then((res) => {setData(res.data)}
      )
        .catch((err) => console.log(err));
      }, []); 
 
  
  
  return (

    <div className='flex flex-col h-full w-full justify-start items-center'
    id='pdf-report'
    >

   
<SingleSensorFromPastSensors
    session = {session}
    sessionId = {sessionId}
   />
   <div className='w-full h-fit flex justify-end items-end gap-[10px]'>
<div className="w-full justify-end items-end h-fit flex gap-[10px] ">

<Dialog open ={open} onOpenChange={setOpen}>
<DialogTrigger >
<div className=" flex justify-end items-center bg-red-400 w-fit p-[10px] rounded-[15px]">
  <h2 className="text-white">

Müdüre Gönder 

  </h2>
</div>

</DialogTrigger>
<DialogContent className="bg-white">
{/*   <div className="gap-[5px]">

  <h2 className=" text-[24px]">Sensörü silmeye emin misiniz? </h2>
  </div>
  <Button variant={null} onClick={() => {handleDelete(sensors.id)} }  className=" flex justify-end items-center bg-red-400 w-fit p-[10px] rounded-[15px]">
  <h2 className="text-white">

Müdüre Gönder 
  </h2>
</Button> */}
  <SendToManagerComponent  onSuccess={() => setOpen(false)}data = {data} session = {session}/>

</DialogContent>
</Dialog>
</div>


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