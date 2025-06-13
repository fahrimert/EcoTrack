import React from 'react'
import Image from "next/image";
import { cn } from '@/lib/utils';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import { SensorData } from './PastSensorList';
import { formatDuration, intervalToDuration } from 'date-fns';
import { tr } from 'date-fns/locale';

const PastSensor = ({sensor,session} : {
  sensor: SensorData
session: RequestCookie

    }) => {
      function getFormattedDuration(start: string): string {

      const startDate = new Date(sensor.startTime);
      const now = new Date();
      
      const duration = intervalToDuration({
        start: startDate,
        end: now
      });
    
      return formatDuration(duration, {
        locale: tr, // Türkçe çıktı için
        format: ['days', 'hours', 'minutes'],
        zero: true,
        delimiter: ' '
      });
    }
    const passedTime = getFormattedDuration(sensor.startTime)


    //ne kadar sürede müdahele edildiği kodu da şu 

/*     function getFormattedDuration(start: string, end: string): string {
      const startDate = new Date(start);
      const endDate = new Date(end);
      
      const duration = intervalToDuration({
        start: startDate,
        end: endDate
      });
    
      return formatDuration(duration, {
        locale: tr, // Türkçe çıktı için
        format: ['days', 'hours', 'minutes', 'seconds'],
        zero: true,
        delimiter: ' '
      });
    }
    
    // Kullanım
    console.log(getFormattedDuration(
      "2025-05-03T20:35:41.891+00:00",
      "2025-05-03T20:36:36.939+00:00"
    )); */
    console.log(sensor);
    return (
     
        
        <div className={cn(`flex flex-col   w-full h-[fit]  justify-center items-center rounded-[30px]  p-[10px] gap-[10px]  `)} >
      
        
      
      <div 
    
      
      className=" bg-[#f1f0ee] rounded-[30px] flex flex-col w-full h-fit p-[10px]   justify-start items-start  shadow-lg  hover:scale-105 duration-300 cursor-pointer ; ">
      <Image
                  src={"/indir.jpg"}
                  alt="232"
                 
                  className={cn(` w-[200px] h-[100px]  object-fit  rounded-[30px] cursor-pointer  `)}
                  width={100} 
                  height={100}
                />
        <div className=" h-full w-full justify-start items-start flex flex-col p-[5px] gap-[5px]  ">
      
          <h2 className="w-full text-[16px] font-normal   ">{sensor.sensor.sensorName} </h2>
    
      
      
        </div>
        <div className=' bg-[#c0ccc9]  w-full p-[5px] rounded-[5px] mb-[5px]'>
    
     {/*    <h2 className="text-[13px] font-normal   text-white ">{sensor.latitude } </h2>
          <h2 className="text-[13px] font-normal  text-white   ">{sensor.longitude } </h2> */}
          <h2 className="text-[13px] font-normal text-white   ">{sensor.sensor.status == "ACTIVE" ? "Düzeltildi" : "Düzeltilemedi"} </h2>
           <h2 className="text-[13px] font-normal text-white   ">{passedTime + " süre önce müdahele edinildi"} </h2>
       </div> 
    
        <div className="h-full w-full flex relative justify-betweeen items-end gap-[50px]">
      

      </div>
      </div>
    
      
      <button  type="button" className="  h-[50px] w-full  rounded-[20px] box-border text-sm font-medium   text-black bg-white border-[2px] shadow-md  ">
     Detaylar
    </button>
      </div> 

)
}

export default PastSensor