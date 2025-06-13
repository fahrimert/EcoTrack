import { Separator } from '@/components/ui/separator';
import dynamic from 'next/dynamic';
import React from 'react'
const HeatMapComponent = dynamic(() => import("./HeatMapForWorkerSensorsComponent"), {
  ssr: false,
});
const HeatMapForWorkerSensors = ({response } : {response:[{
    id: string;
    latitude: number;
    longitude: number;
}]}) => {




  return <div className=' w-full h-fit flex flex-col gap-[10px]'>
    <h2 className='text-[24px]'>İşçinin Görev Dışı Çözdüğü Hatalı Sensörlerin Sıcaklık Haritaları</h2>
    <Separator/>
    <HeatMapComponent response={response} />
  </div>

}

export default HeatMapForWorkerSensors