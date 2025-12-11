import React, { useContext, useEffect, useState } from "react";
import Image from "next/image";
import { DestinationContext } from "@/context/DestinationContext";
import Link from "next/link";
import {  WorkerDashboardTaskWithSensorDto } from "@/app/worker/types/types";
import { MdLocationOn, MdTask } from "react-icons/md";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";


interface TaskSensorProps {
  sensors: WorkerDashboardTaskWithSensorDto;
}

const TaskSensor = ({ sensors }: TaskSensorProps) => {
const { setDestination } = useContext(DestinationContext);
  const [address, setAddress] = useState<string>("Konum aranıyor...");
useEffect(() => {
    let isMounted = true;

    const fetchAddress = async () => {
      try {
        const apiKey = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY;
        if (!apiKey || !sensors) return;

        const res = await fetch(
          `https://maps.googleapis.com/maps/api/geocode/json?latlng=${sensors.latitude},${sensors.longitude}&key=${apiKey}`
        );
        const data = await res.json();

        if (isMounted && data.status === "OK" && data.results[0]) {
          setAddress(data.results[0].formatted_address);
        }
      } catch (error) {
        console.error("Adres alınamadı:", error);
        if (isMounted) setAddress("Adres bulunamadı");
      }
    };

    fetchAddress();

    return () => { isMounted = false; };
  }, [sensors?.latitude, sensors?.longitude]); 

  if (!sensors || !sensors.id) return null;

  const handleSetDestination = () => {
    setDestination({
      lat: sensors.latitude,
      lng: sensors.longitude,
    });
  };

const sensorId = sensors?.id;

  const sensorLink = sensorId
  ? `/worker/dashboard/sensor-tasks/${sensorId}`
  : "#"; 


return (
    <div
      onMouseEnter={handleSetDestination}
      className="group relative flex flex-col bg-white border-2 border-amber-100 rounded-2xl overflow-hidden shadow-sm hover:shadow-xl hover:border-amber-300 transition-all duration-300"
    >
      <div className="absolute top-0 left-0 w-1 h-full bg-amber-400 z-20" />

      <div className="relative h-32 w-full bg-slate-100 overflow-hidden">
        <Image
          src="/indir.jpg"
          alt={sensors.sensorName}
          fill
          className="object-cover transition-transform duration-500 group-hover:scale-110"
        />
        
        <div className="absolute top-2 right-2 z-10">
          <Badge className="bg-amber-500 text-white shadow-sm border-0 px-2 py-1 flex items-center gap-1">
            <MdTask size={12} />
            <span>GÖREV</span>
          </Badge>
        </div>
      </div>

      <div className="p-4 pl-5 flex flex-col gap-3">
        <div className="flex items-center gap-2">
          <div
            className="w-3 h-3 rounded-full shadow-sm ring-1 ring-white shrink-0"
            style={{ backgroundColor: sensors.color_code || "#000" }}
          />
          <h3 className="font-bold text-slate-800 text-sm truncate" title={sensors.sensorName}>
            {sensors.sensorName}
          </h3>
        </div>

        <div className="flex items-start gap-1.5 text-xs text-slate-500 bg-amber-50/50 p-2 rounded-lg border border-amber-100">
          <MdLocationOn className="mt-0.5 text-amber-600 shrink-0" size={14} />
          <p className="line-clamp-2 leading-relaxed">{address}</p>
        </div>

        <div className="text-[10px] font-semibold text-slate-400 uppercase tracking-wide">
            DURUM: <span className="text-slate-700">{sensors.status}</span>
        </div>

        <div className="mt-auto pt-1">
          <Link href={`/worker/dashboard/sensor-tasks/${sensors.id}`} className="w-full block">
            <Button 
                className="w-full h-9 text-xs font-semibold bg-amber-500 text-white hover:bg-amber-600 transition-colors shadow-md"
            >
              Görev Detaylarına Git
            </Button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default TaskSensor;