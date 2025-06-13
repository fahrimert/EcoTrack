import React  from 'react'
import Heading from '../components/Heading'
import SingleSensorFromPastSensors from './components/SingleSensorFromPastSensors'
import { cookies } from 'next/headers'
import { SensorDetailForWorkerPastSensor } from '@/app/supervisor/superVizorDataTypes/types';






const page = async ({params} : {params:{id:string}}) => {
    const session =   cookies().get("session")
    const response = await fetch(`http://localhost:8080/sensors/getPastSensorDetail/${params.id}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${session?.value}`,
        'Content-Type': 'application/json'
      }
    });
    
    const initialdata = await response.json() as  SensorDetailForWorkerPastSensor
    
  return (

    <div className='flex flex-col h-fit w-full mt-[10px]'>

    <Heading 
    title={`Sensör ${initialdata.data.sensorName} Çözüm Geçmişi`}
    description={"Çözdüğünüz veya Çözemediğiniz Tüm Geçmişteki Sensörler Bu Sayfada gözükür "}
    />
  <SingleSensorFromPastSensors 
    session = {session}
    initialdata = {initialdata}/> 
    </div>
  )
}

export default page