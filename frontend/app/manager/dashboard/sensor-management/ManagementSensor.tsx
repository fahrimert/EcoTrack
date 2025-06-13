import React, {useEffect, useState } from "react";
import Image from "next/image";
import { cn } from "@/lib/utils";
import Link from "next/link";
import {
  Dialog,
  DialogContent,
  DialogTrigger,
} from "@/components/ui/dialog"
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import DetailsComponentOfSensors from "./DetailsComponentOfSensor";
import { SensorListForManagerUse } from "./SensorManagementSensorsWrapperComponent";
const   ManagementSensor = ({
  sensors,
  session
}: {
  sensors: SensorListForManagerUse | undefined,
session : RequestCookie | undefined

}) => {

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
    fetchAddressFromCoordinates(sensors?.latitude!, sensors?.longitude!)
      .then((address) => {
        setAddress(address);
      })
      .catch((err) => console.error(err));
  }, [])

   const [open,setOpen] = useState(false);

    const [openmaindialog,setOpenMainDialog] = useState(false);

    const base64Image = sensors?.imageResponseDTO?.base64Image;

  return (
    <>

  <div className="space-y-2 mt-2">
    <div className="p-3 border rounded-lg flex flex-col bg-gray-50 w-fit">
      <Dialog open={openmaindialog} onOpenChange={setOpenMainDialog}>
        <DialogTrigger className="flex flex-col justify-between items-start gap-[10px]">
          <div className={cn(`flex flex-col w-full h-fit justify-center items-center rounded-[30px] p-[10px] gap-[10px]`)}>
            <div className={cn(`rounded-[30px] flex flex-col w-fit h-fit p-[10px] justify-start items-start shadow-lg hover:scale-105 duration-300 cursor-pointer gap-[10px]`)}>
             <Image
  src={base64Image ? `data:image/png;base64,${base64Image}` : "/indir.jpg"}
                alt="sensor-image"
                
                className={cn(`w-[200px] h-[100px] object-fit rounded-[30px] cursor-pointer`)}
                width={100}
                height={100}
              /> 
              <div  style={{ backgroundColor: sensors?.color_code, opacity: 0.6 }} className="h-full w-full justify-start items-start flex flex-col p-[5px] gap-[5px] rounded-[5px]">
                <div className="flex flex-row gap-[5px] w-full h-full rounded-[5px]">
                  <h2 className="text-[16px] font-normal w-fit h-full text-black">
                    Sensör İsmi:
                  </h2>
                  <h2 className="w-full text-[16px] h-fit font-normal">
                    {sensors?.sensorName}
                  </h2>
                </div>
              </div>
              <div style={{ backgroundColor: sensors?.color_code, opacity: 0.6 }} className="w-full p-[5px] rounded-[5px] mb-[5px] gap-[10px]">
                <div className=" w-full flex flex-row gap-[5px]">
                  <h2 className="  w-fit  text-[13px] font-normal text-white">
                   En Son Konum:
                  </h2>
                  <h2 className=" w-full  text-[13px] font-normal text-white">
                    {address}
                  </h2>
                </div>
                <div className="flex flex-row gap-[5px]">
                  <h2 className="text-[13px] font-normal text-white">
                    Durum:
                  </h2>
                  <h2 className="text-[13px] font-normal text-white">
                    {sensors?.status}
                  </h2>
                </div>
              </div>
            </div>
          </div>
        </DialogTrigger>
        <DialogContent className="bg-white w-fit items-center justify-center flex">
          <DetailsComponentOfSensors
            open={open}
            setOpenMainDialog={setOpenMainDialog}
            setOpen={setOpen}
            sensors={sensors}
            session={session}
          />
        </DialogContent>
      </Dialog>
    </div>
  </div>
    </>
  );
};

export default ManagementSensor;
