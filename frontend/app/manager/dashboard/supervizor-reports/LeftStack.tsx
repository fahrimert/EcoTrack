  "use client"
  
  import Image from "next/image";
  import { differenceInDays, differenceInHours, differenceInMinutes, differenceInSeconds, format } from "date-fns";
  import { Separator } from "@/components/ui/separator";
import { useEffect, useState } from "react";

import axios from "axios";

import { PdfReport, PdfReportInduvual } from "../../types/types";

  const   LeftStack = ({sensor,sensorData } : {sensor:PdfReport,sensorData : PdfReportInduvual} ) => {
  const[sensorDataPdf , setSensorDataPdf] = useState<PdfReportInduvual>()


  useEffect(() => {
    if (sensorData?.data) {
      setSensorDataPdf(sensorData)
  console.log(sensorDataPdf);

    }
  }, [sensorData])
     

    return (
      <div className='relative  w-full h-full flex flex-col justify-start items-start gap-[5px] p-[10px]  '>
                           <h2 className={`w-fit h-fit  text-black text-[24px] leading-[24px] border-b-[1px]`}>Sensör Raporu</h2>
     
         <div className="w-full  h-fit flex flex-col justify-start items-start">
          <div className=" w-full  px-[5px] flex flex-col py-[10px] gap-[10px] ">
               <div className=" w-full flex flex-row items-center  justify-between  gap-[10px]">
           
            <div  className=" w-full flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
       <h2 className="w-full text-[16px] text-black">Sensör İsmi : </h2>
          {sensorDataPdf?.data.sensorName ? (
       <h2 className="w-full text-black text-[14px]">
             {sensorDataPdf?.data.sensorName}
         <Separator/>
     
         </h2>
     ) : (
       <div className="w-[100px] h-[100px] flex items-center justify-center bg-gray-100 text-sm text-gray-500">
     İsim yok  </div>
     )}
            </div>
     
         
       </div>
         <div className=" w-full flex flex-row items-center  gap-[10px] border-b-[1px]">
       <h2 className="w-full  text-[14px] text-black">Sensör Konum: </h2>
       
     {/*       <h2 className="w-full text-black  text-[14px]">
             {data.data.longitude}
         <Separator/>
     
         </h2> */}
       </div>
     
       <div className=" w-full flex flex-row items-center  gap-[10px] border-b-[1px]">
     
                             <h2 className=" w-full text-[14px] text-black">
         Sensörün ID Numarası:
         </h2>
         
                             <h2 className=" w-full   text-[14px] text-black">
                             {sensorDataPdf?.data.id}
         </h2>
       </div>
             
          <div className=" w-full flex flex-row items-center  gap-[10px] border-b-[1px]">
       <h2 className="w-full text-[14px] text-black">Servisin Bırakılma Durumu : </h2>
       <h2 className="w-full  text-[14px] ">
             {sensorDataPdf?.data.displayName ? sensorDataPdf?.data.displayName : "Bilgi girilmemiş" }
         </h2> 
         
       </div>
      <div  className=" w-full h-fit flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
       <h2 className=" w-full h-fit  text-[14px] text-black">Servisin Başlama Zamanı : </h2>
       <h2 className=" w-full h-fit  text-[14px] text-black">
     
     {sensor.startTime ? 
     
                             format(new Date(sensor.startTime), 'dd MMMM yyyy HH:mm')
      : "Bilgi yok"
     }
       </h2>
     
            </div>
       <div  className=" w-full h-fit flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
       <h2 className=" w-full h-fit  text-[14px] text-black">Servisin Bitme Zamanı : </h2>
       <h2 className=" w-full h-fit  text-[14px] text-black">
     
                           {sensor.completedTime ? 
     
                             format(new Date(sensor.completedTime), 'dd MMMM yyyy HH:mm')
      : "Bilgi yok"
     }
       </h2>
     
     
            </div>
     
     
     
     
          <div  className=" w-full flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
       <h2 className=" w-full  text-[14px] text-black">Çözülme Süresi : </h2>
          {sensor.completedTime ? (
       <div className="w-full text-black flex gap-[5px]">
         <h2 className=" text-[14px]">
     
             {differenceInDays(sensor.completedTime , sensor.startTime)} Gün
         </h2>
         <h2 className=" text-[14px]">
     
             {differenceInHours(sensor.completedTime , sensor.startTime)} Saat
         </h2>
         <h2 className=" text-[14px]">
     
             {differenceInMinutes(sensor.completedTime , sensor.startTime)} Dakika
         </h2>
         <h2 className=" text-[14px]">
     
            {differenceInSeconds(sensor.completedTime , sensor.startTime)}  Saniye
         </h2>
     
     
     
         </div>
     ) : (
       <div className="w-[100px] h-[100px] flex items-center justify-center bg-gray-100 text-sm text-gray-500">
     Bilgi yok  </div>
     )}
            </div> 
          </div>
     
     
     
          <div className=" w-full  px-[5px] flex flex-col py-[10px] gap-[10px] ">
       <h2 className="w-full text-[16px] text-black">Servisi Veren Teknisyen Bilgileri : </h2>
     
        <div className=" w-full flex flex-row items-center  justify-between  gap-[10px]">
           
            <div  className=" w-full flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
       <h2 className="w-full text-[14px] text-black">Teknisyen Notu:</h2>
          {sensor.technicianNote ? (
       <h2 className="w-full text-black text-[14px]">
             {sensor?.technicianNote }
         <Separator/>
     
         </h2>
     ) : (
       <div className="w-[100px] h-[100px] flex items-center justify-center bg-gray-100 text-sm text-gray-500">
     Teknisyen Notu yok  </div>
     )}
            </div>
     
         
       </div>
       
 {/*       <div  className=" w-full flex flex-row items-start  justify-between  gap-[10px] border-b-[1px]">
       <h2 className="w-full text-[14px] text-black">Teknisyenin notu: </h2>
          {sensor.technicianNote ? (
           
       <h2 className="w-full h-fit text-black text-[14px]">
     {sensor.technicianNote}
         <Separator/>
     
         </h2>
     ) : (
       <div className="w-[100px] h-[100px] flex items-center justify-center bg-gray-100 text-sm text-gray-500">
     Not yok  </div>
     )}
            </div> */}
     
             
                 
          </div>
     
     
     
      
         </div>
     
          <div className=" w-full  px-[5px] flex flex-col py-[10px] gap-[10px] ">
       <h2 className="w-full text-[16px] text-black">Sensöre Ait Fotoğraflar </h2>
       <div className="w-full h-fit grid grid-cols-3 gap-[20px]">
       {
       sensorDataPdf?.data.imageResponseDTO.map((d,g) =>
                           <div className="relative   bg-blue rounded-[20px] bg-blue-200">
       <Image 
       width={200}
       height={200}
       objectFit="true"
       src={`data:image/png;base64,${d.base64Image}`}
     
       alt="212"
       />                                     
     
       </div>
                       )
                     } 
       </div>
          
       
      
             
                 
          </div>
     
     
     
             
           
     
     
     
     
     
           
     
           </div>
     

    )
  }

  export default LeftStack