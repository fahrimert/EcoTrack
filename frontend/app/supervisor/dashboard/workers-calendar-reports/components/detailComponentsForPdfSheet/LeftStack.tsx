  "use client"
  
  import Image from "next/image";
  import { differenceInDays, differenceInHours, differenceInMinutes, differenceInSeconds, format } from "date-fns";
  import { Separator } from "@/components/ui/separator";
import { useEffect, useState } from "react";

import axios from "axios";

import { useUserProfile } from "@/hooks/useUserProfile";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";

  const LeftStack = ({session,sessionId} : {

  session: RequestCookie | undefined
  sessionId:string
  } , ) => {

  const { userProfile, loading, error } = useUserProfile(session);

  
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

      console.log(sessionId);
      useEffect(() => {
        axios.get(`http://localhost:8080/sensors/getPastSensorDetail/${sessionId}`, {
          headers: { Authorization: `Bearer ${session?.value}` },
          withCredentials: true,
        })
        .then((res) => {setData(res.data)}
      )
        .catch((err) => console.log(err));
      }, []); 
        console.log(data?.data);
    return (
      <div className='relative  w-full h-full flex flex-col justify-start items-start gap-[5px] p-[10px]  '>
                      <h2 className={`w-fit h-fit  text-black text-[24px] leading-[24px] border-b-[1px]`}>Sensör Raporu</h2>

    <div className="w-full  h-fit flex flex-col justify-start items-start">
     <div className=" w-full  px-[5px] flex flex-col py-[10px] gap-[10px] ">
          <div className=" w-full flex flex-row items-center  justify-between  gap-[10px]">
      
       <div  className=" w-full flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
  <h2 className="w-full text-[16px] text-black">Sensör İsmi : </h2>
     {data?.data.sensorName ? (
  <h2 className="w-full text-black text-[14px]">
        {data.data.sensorName}
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
                        {data?.data.id}
    </h2>
  </div>
        
                  <div className=" w-full flex flex-row items-center  gap-[10px] border-b-[1px]">
  <h2 className="w-full text-[14px] text-black">Servisin Bırakılma Durumu : </h2>
  <h2 className="w-full  text-[14px] ">
        {data?.data.status ? data.data.status : "Bilgi girilmemiş" }
    </h2> 
    
  </div>
      <div  className=" w-full h-fit flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
  <h2 className=" w-full h-fit  text-[14px] text-black">Servisin Başlama Zamanı : </h2>
  <h2 className=" w-full h-fit  text-[14px] text-black">

{data?.data?.startTime ? 

                        format(new Date(data?.data.startTime), 'dd MMMM yyyy HH:mm')
 : "Bilgi yok"
}
  </h2>

       </div>
  <div  className=" w-full h-fit flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
  <h2 className=" w-full h-fit  text-[14px] text-black">Servisin Bitme Zamanı : </h2>
  <h2 className=" w-full h-fit  text-[14px] text-black">

                      {data?.data.completedTime ? 

                        format(new Date(data?.data.completedTime), 'dd MMMM yyyy HH:mm')
 : "Bilgi yok"
}
  </h2>


       </div>




         <div  className=" w-full flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
  <h2 className=" w-full  text-[14px] text-black">Çözülme Süresi : </h2>
     {data?.data.completedTime ? (
  <div className="w-full text-black flex gap-[5px]">
    <h2 className=" text-[14px]">

        {differenceInDays(data.data.completedTime , data.data.startTime)} Gün
    </h2>
    <h2 className=" text-[14px]">

        {differenceInHours(data.data.completedTime , data.data.startTime)} Saat
    </h2>
    <h2 className=" text-[14px]">

        {differenceInMinutes(data.data.completedTime , data.data.startTime)} Dakika
    </h2>
    <h2 className=" text-[14px]">

       {differenceInSeconds(data.data.completedTime , data.data.startTime)}  Saniye
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
  <h2 className="w-full text-[14px] text-black">Teknisyen İsmi:</h2>
     {userProfile?.firstName ? (
  <h2 className="w-full text-black text-[14px]">
        {userProfile?.firstName}
    <Separator/>

    </h2>
) : (
  <div className="w-[100px] h-[100px] flex items-center justify-center bg-gray-100 text-sm text-gray-500">
İsim yok  </div>
)}
       </div>

    
  </div>
       <div  className=" w-full flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
  <h2 className="w-full text-[14px] text-black">Teknisyen Maili:</h2>
     {userProfile?.email ? (
  <h2 className="w-full text-black text-[14px]">
        {userProfile?.email}
    <Separator/>

    </h2>
) : (
  <div className="w-full h-fit flex items-start justify-start text-sm text-gray-500">
Email yok  </div>
)}
       </div>
  
  <div  className=" w-full flex flex-row items-start  justify-between  gap-[10px] border-b-[1px]">
  <h2 className="w-full text-[14px] text-black">Teknisyenin notu: </h2>
     {data?.data?.note ? (
      
  <h2 className="w-full h-fit text-black text-[14px]">
{data.data.note}
    <Separator/>

    </h2>
) : (
  <div className="w-[100px] h-[100px] flex items-center justify-center bg-gray-100 text-sm text-gray-500">
Not yok  </div>
)}
       </div>

        
            
     </div>



 
    </div>

     <div className=" w-full  px-[5px] flex flex-col py-[10px] gap-[10px] ">
  <h2 className="w-full text-[16px] text-black">Sensöre Ait Fotoğraflar </h2>
  <div className="w-full h-fit grid grid-cols-3 gap-[20px]">
  {
  data?.data.imageResponseDTO.map((d,g) =>
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