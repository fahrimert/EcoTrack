import React, { useContext, useEffect, useState } from "react";
import Image from "next/image";
import { cn } from "@/lib/utils";
import { DestinationContext } from "@/context/DestinationContext";
import Link from "next/link";
import toast from "react-hot-toast";
import { goToSensor } from "@/app/actions/sensorActions/goToSensor";

const Sensor = ({
  sensors,
}: {
  sensors: {
    id: string;
    sensorName: string;
    status: string;
    latitude: number;
    longitude: number;
    color_code: string;
    userBasedSensor: boolean;

    currentSensorSession:
      | {
          id: number;
          sensor: {
            id: string;
            sensorName: string;
            status: string;
            installationDate: number;
          };
          startTime: number;
          completedTime: number;
          note: null;
        }
      | undefined;
  };
}) => {
  const { destination, setDestination } = useContext(DestinationContext);

  const handleGO = async (sensorId: string) => {
    setDestination({
      lat: sensors.latitude,
      lng: sensors.longitude,
    });
    try {
      const returnData = await goToSensor(sensorId);
      toast.success(returnData.serverData);
    } catch (error) {
      console.log(error.message);
    }
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

  return (
    <>
      {sensors.userBasedSensor ? (
        <Link
          href={`/worker/dashboard/sensors/${String(
            sensors.id
          )}`}
        >
          <div
            className={cn(
              `flex flex-col   w-full h-fit  justify-center items-center rounded-[30px]  p-[10px] gap-[10px] ${
                sensors.status == "IN_REPAIR" &&
                !sensors.userBasedSensor &&
                "blur-sm"
              } `
            )}
          >
            <div
              onMouseEnter={() =>
                sensors.status !== "IN_REPAIR" &&
                !sensors.userBasedSensor &&
                setDestination({
                  lat: sensors.latitude,
                  lng: sensors.longitude,
                })
              }
              className=" bg-[#f1f0ee] rounded-[30px] flex flex-col w-fit h-fit p-[10px]   justify-start items-start  shadow-lg  hover:scale-105 duration-300 cursor-pointer ; "
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
                  {sensors.sensorName}{" "}
                </h2>
              </div>
              <div className=" bg-[#c0ccc9]  w-full p-[5px] rounded-[5px] mb-[5px]">
                <h2 className="text-[13px] font-normal   text-white ">
                  {address}
                </h2>
                <h2 className="text-[13px] font-normal  text-white   ">
                  {sensors.longitude}{" "}
                </h2>
                <h2 className="text-[13px] font-normal text-white   ">
                  {sensors.status}{" "}
                </h2>
              </div>

              <div className="h-full w-full flex relative justify-betweeen items-end gap-[50px]">
                <button
                  type="button"
                  className="  h-[50px] w-full  rounded-[20px] box-border text-sm font-medium   text-black bg-white border-[2px] shadow-md  "
                >
                  {sensors.userBasedSensor ? "Detaylar" : "Gidin"}
                </button>
              </div>
            </div>
          </div>
        </Link>
      ) : (
        <div
          className={cn(
            `flex flex-col   w-full h-fit  justify-center items-center rounded-[30px]  p-[10px] gap-[10px] ${
              sensors.status == "IN_REPAIR" &&
              !sensors.userBasedSensor &&
              "blur-sm"
            } `
          )}
        >
          <div
            onMouseEnter={() =>
              sensors.status !== "IN_REPAIR" &&
              !sensors.userBasedSensor &&
              setDestination({
                lat: sensors.latitude,
                lng: sensors.longitude,
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
                {sensors.sensorName}{" "}
              </h2>
            </div>
            <div 
            style={{ backgroundColor: sensors.color_code , opacity: 0.6}}
            
            className=" bg-[#c0ccc9]  w-full p-[5px] rounded-[5px] mb-[5px]">
              <h2 className="text-[13px] font-normal   text-white ">
                {address}{" "}
              </h2>
        
              <h2 className="text-[13px] font-normal text-white   ">
                {sensors.status}{" "}
              </h2>
            </div>

            <div className="h-full w-full flex relative justify-betweeen items-end gap-[50px]">
              <button
                onClick={() => handleGO(sensors.id)}
                type="button"
                className="  h-[50px] w-full  rounded-[20px] box-border text-sm font-medium   text-black bg-white border-[2px] shadow-md  "
              >
                {sensors.userBasedSensor ? "Detaylar" : "Gidin"}
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default Sensor;
