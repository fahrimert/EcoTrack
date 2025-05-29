import React, { useContext, useEffect, useState } from "react";
import Image from "next/image";
import { cn } from "@/lib/utils";
import { DestinationContext } from "@/context/DestinationContext";
import Link from "next/link";
import { TaskSensorWithTask } from "./SensorsAndMap";

const TaskSensor = ({
  sensors,
}: {
  sensors: {
     id: number;
    sensorName: string;
    status: string; // İsteğe bağlı: enum olarak tanımlanabilir
    color_code: string;
    latitude: number;
    longitude: number;
    currentSensorSession: {
      id: number | null;
      sensorName: string;
      displayName: string;
      color_code: string;
      note: string | null;
      startTime: string | null;
      completedTime: string | null;
      latitude: number;
      longitude: number;
  }
}}) => {
  const { destination, setDestination } = useContext(DestinationContext);

  const handleGO = async () => {
    setDestination({
      lat: sensors.latitude,
      lng: sensors.longitude,
    });
  };
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
    fetchAddressFromCoordinates(sensors.latitude, sensors.longitude)
      .then((address) => {
        setAddress(address);
      })
      .catch((err) => console.error(err));
  }, [])

  console.log(sensors);
const sensorId = sensors?.id;

  const sensorLink = sensorId
  ? `/worker/dashboard/sensor-tasks/${sensorId}`
  : "#"; // veya "/not-found"


  return (
    <>

                <Link
          href={sensorLink}
        >
        <div
          className={cn(
            `flex flex-col   w-full h-fit  justify-center items-center rounded-[30px]  p-[10px] gap-[10px]  `
          )}
        >
          <div
            onMouseEnter={() =>
              setDestination({
                lat: !sensors ? null : sensors.latitude,
                lng: !sensors ? null : sensors.longitude,
              })
            }
            className=  " bg-[#f1f0ee] rounded-[30px] flex flex-col w-fit h-fit p-[10px]   justify-start items-start  shadow-lg  hover:scale-105 duration-300 cursor-pointer ; "
          >
            <Image
              src={"/indir.jpg"}
              alt="232"
              className={cn(
                ` w-[200px] h-[100px]  object-fit  rounded-[30px] cursor-pointer  `
              )}
              width={100}
              height={100}
            />
            <div className=" h-full w-full justify-start items-start flex flex-col p-[5px] gap-[5px]  ">
              <h2 className="w-full text-[16px] font-normal   ">
                {!sensors ? null :  sensors.sensorName }{" "}
              </h2>
            </div>
            <div 
            style={{ backgroundColor: !sensors ? "#000000" : sensors.color_code , opacity: 0.6}}
            
            className=" bg-[#c0ccc9]  w-full p-[5px] rounded-[5px] mb-[5px]">
              <h2 className="text-[13px] font-normal   text-white ">
                {!sensors ? null : address}{" "}
              </h2>
        
              <h2 className="text-[13px] font-normal text-white   ">
                {!sensors ? null : sensors.status}{" "}
              </h2>
            </div>

            <div className="h-full w-full flex relative justify-betweeen items-end gap-[50px]">
              <button
                onClick={() => handleGO()}
                type="button"
                className="  h-[50px] w-full  rounded-[20px] box-border text-sm font-medium   text-black bg-white border-[2px] shadow-md  "
              >
                Görev Detaylar
              </button>
            </div>
          </div>
        </div>
        </Link>

    </>
  );
};

export default TaskSensor;
