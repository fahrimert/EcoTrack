import { Separator } from '@/components/ui/separator';
import dynamic from 'next/dynamic';
import React from 'react'
const HeatMapComponent = dynamic(() => import("./HeatMapComponent"), {
  ssr: false,
});
const HeatMap = ({response } : {response:[{
    id: string;
    latitude: number;
    longitude: number;
}]}) => {




  return <div className=' w-full h-fit flex flex-col gap-[10px]'>
    <h2 className='text-[24px]'>Geçmişteki Görevler Harici Hatalı Sensörlerin Sıcaklık Haritaları</h2>
    <Separator/>
    <HeatMapComponent response={response} />
  </div>

}

export default HeatMap