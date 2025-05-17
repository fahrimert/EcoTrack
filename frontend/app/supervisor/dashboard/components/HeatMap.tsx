import { Separator } from '@/components/ui/separator';
import { Wrapper } from '@googlemaps/react-wrapper'
import { GoogleMap, HeatmapLayer } from '@react-google-maps/api'
import dynamic from 'next/dynamic';
import { cookies } from 'next/headers';
import React, { useEffect, useMemo, useState } from 'react'
const HeatMapComponent = dynamic(() => import("./HeatMapComponent"), {
  ssr: false,
});
const HeatMap = ({response } : {response:[{
    id: string;
    latitude: number;
    longitude: number;
}]}) => {




  return <div className=' w-full h-fit flex flex-col gap-[10px]'>
    <h2 className='text-[24px]'>Geçmişteki Hatalı Sensörlerin Sıcaklık Haritalar</h2>
    <Separator/>
    <HeatMapComponent response={response} />
  </div>

}

export default HeatMap