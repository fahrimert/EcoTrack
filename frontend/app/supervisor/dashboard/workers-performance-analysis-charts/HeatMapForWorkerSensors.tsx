import { Separator } from '@/components/ui/separator';
import { Wrapper } from '@googlemaps/react-wrapper'
import { GoogleMap, HeatmapLayer } from '@react-google-maps/api'
import dynamic from 'next/dynamic';
import { cookies } from 'next/headers';
import React, { useEffect, useMemo, useState } from 'react'
import HeatMapForWorkerSensorsComponent from './HeatMapForWorkerSensorsComponent';
const HeatMapComponent = dynamic(() => import("./HeatMapForWorkerSensorsComponent"), {
  ssr: false,
});
const HeatMapForWorkerSensors = ({response } : {response:[{
    id: string;
    latitude: number;
    longitude: number;
}]}) => {




  return <div className=' w-full h-fit flex flex-col gap-[10px]'>
    <h2 className='text-[24px]'>Kullanıcının Çözdüğü Hatalı Sensörlerin Sıcaklık Haritaları</h2>
    <Separator/>
    <HeatMapForWorkerSensorsComponent response={response} />
  </div>

}

export default HeatMapForWorkerSensors