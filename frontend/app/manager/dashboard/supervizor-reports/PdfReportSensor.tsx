"use client"
import React, {useEffect, useState } from "react";
import { cn } from "@/lib/utils";
import {
  Dialog,
  DialogContent,
  DialogTrigger,
} from "@/components/ui/dialog"
import { PdfReport, PdfReportInduvual } from "../../types/types";

import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { format } from "date-fns";
import DetailsPage from "./DetailsPage";
import { tr } from "date-fns/locale";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import Image from "next/image";
import axios from "axios";

const   PdfReportSensor = ({
  sensor,
  session
}: {
  sensor: PdfReport | undefined,
  session: RequestCookie | undefined
}) => {
  console.log(sensor);
  const [address,setAddress] = useState()

const fetchAddressFromCoordinates = async (lat: number, lng: number) => {
    const res = await fetch(
      `https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}&key=AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E`
    );
  
    const data = await res.json();
  
    if (data.status === 'OK') {
      return data.results[0].formatted_address;
    } else {
      throw new Error('Geocoding failed');
    }
  };
  useEffect(() => {
    fetchAddressFromCoordinates(sensor?.longitude!,sensor?.latitude!)
      .then((address) => {
        setAddress(address);
      })
      .catch((err) => console.error(err));
  }, [])

   const [open,setOpen] = useState(false);

    const [openmaindialog,setOpenMainDialog] = useState(false);
    
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
/*     const base64Image = sensors?.imageResponseDTO?.base64Image;
 */
  return (
    <>

  <div className="space-y-2 mt-2">
    <div className="p-3 border rounded-lg flex flex-col bg-gray-50 w-fit">
                               <Sheet >
  <SheetTrigger className="flex flex-col justify-between items-start gap-[10px]">
 
          <div className={cn(`flex flex-col w-full h-fit justify-center items-center rounded-[30px] p-[10px] gap-[10px]`)}>
            <div className={cn(`rounded-[30px] flex flex-col w-fit h-fit p-[10px] justify-start items-start shadow-lg hover:scale-105 duration-300 cursor-pointer gap-[10px]`)}>
       <Image
  src={sensorData?.data.sensorIconImage.base64Image ? `data:image/png;base64,${sensorData?.data.sensorIconImage.base64Image}` : "/indir.jpg"}
                alt="sensor-image"
                
                className={cn(`w-[200px] h-[100px] object-fit rounded-[30px] cursor-pointer`)}
                width={100}
                height={100}
              />  
    
              <div className="w-full p-[5px] rounded-[5px] mb-[5px] gap-[10px]">
                <div className=" w-full flex flex-row gap-[5px]">
                  <h2 className="  w-fit  text-[13px] font-normal text-black">
                   En Son Konum:
                  </h2>
                  <h2 className=" w-full  text-[13px] font-normal text-black">
                    {address}
                  </h2>
                </div>
                <div className="flex flex-row gap-[5px]">
                  <h2 className="text-[13px] font-normal text-black">
                    Sensor İsmi:
                  </h2>
                  <h2 className="text-[13px] font-normal text-black">
                    {sensor?.sensorName}
                  </h2> 
                </div>
              </div>
            </div>
          </div>


                      
                          
       </SheetTrigger>
  <SheetContent className="bg-white w-[800px] items-center justify-center flex">
  <DetailsPage session= {session} sensor = {sensor} />
  </SheetContent>
</Sheet>
                 

    </div>
  </div>
    </>
  );
};

export default PdfReportSensor;
