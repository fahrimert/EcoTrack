"use client";

import React, { useContext, useEffect, useState } from "react";
import Image from "next/image";
import Link from "next/link";
import { cn } from "@/lib/utils";
import { DestinationContext } from "@/context/DestinationContext";
import { WorkerDashboardSensorDto } from "@/app/worker/types/types";
import { MdLocationOn, MdSensors } from "react-icons/md";
import { Badge } from "@/components/ui/badge"; 
import { Button } from "@/components/ui/button"; 
import { goToSensor } from "@/app/actions/sensorActions/goToSensor";
import toast from "react-hot-toast";

interface SensorCardProps {
  sensor: WorkerDashboardSensorDto;
  isMySensor:boolean
}
const SensorCard = ({ sensor, isMySensor }: SensorCardProps) => {
  const { setDestination } = useContext(DestinationContext);
  
  const isDisabled = sensor.status === "IN_REPAIR" && !isMySensor;

  const handleSetDestination = () => {
    if (isDisabled) return;
    setDestination({ lat: sensor.latitude, lng: sensor.longitude });
  };

  const handleAction = async (id:number) => {
    setDestination({
      lat: sensor.latitude,
      lng: sensor.longitude,
    });
    const response = await goToSensor(id);

    if (response.serverError) {
        toast.error(response.serverError); 
    } else {
        toast.success("Tamir işlemi başladı!");
        // response.serverData ile işlem yap...
    }
  };

  return (
    <div
      onMouseEnter={handleSetDestination}
      className={cn(
        "relative flex flex-col bg-white border border-gray-200 rounded-xl overflow-hidden shadow-sm hover:shadow-md transition-all duration-300 group",
        isDisabled && "opacity-50 grayscale pointer-events-none"
      )}
    >
      <div className="relative h-32 w-full bg-gray-100">
        <Image
          src="/indir.jpg"
          alt={sensor.sensorName}
          fill
          className="object-cover transition-transform duration-500 group-hover:scale-105"
        />
        <div className="absolute top-2 right-2">
            <Badge 
                className={cn(
                    "text-[10px] font-bold uppercase shadow-sm border-0",
                    sensor.status === "ACTIVE" ? "bg-emerald-500 hover:bg-emerald-600" : 
                    sensor.status === "IN_REPAIR" ? "bg-yellow-500 hover:bg-yellow-600" : "bg-gray-500"
                )}
            >
                {sensor.status}
            </Badge>
        </div>
      </div>

      <div className="p-4 flex flex-col gap-3">
        <div className="flex items-center gap-2">
            <div 
                className="w-2 h-2 rounded-full" 
                style={{ backgroundColor: sensor.colorCode }} 
            />
            <h3 className="font-semibold text-gray-800 text-sm truncate">
                {sensor.sensorName}
            </h3>
        </div>

        <div className="flex items-center gap-1 text-xs text-gray-500">
            <MdLocationOn size={14} className="text-gray-400" />
            <span className="truncate">Lat: {sensor.latitude.toFixed(4)}, Lng: {sensor.longitude.toFixed(4)}</span>
        </div>

        {isMySensor ? (
            <Link href={`/worker/dashboard/sensors/${sensor.id}`} className="w-full">
                <Button className="w-full h-8 text-xs bg-black hover:bg-gray-800 text-white">
                    Detayları Gör
                </Button>
            </Link>
        ) : (
            <Button 
                onClick={() => handleAction(sensor.id)} 
                disabled={isDisabled}
                variant="outline"
                className="w-full h-8 text-xs border-gray-300 hover:bg-emerald-50 hover:text-emerald-600 hover:border-emerald-200"
            >
                Konuma Git
            </Button>
        )}
      </div>
    </div>
  );
};

export default SensorCard;