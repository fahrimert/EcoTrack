import React from 'react'
import Heading from '../components/Heading'
import SingleSensorFromPastSensors from './components/SingleSensorFromPastSensors'
import { userService } from '@/app/services/userService';




const page = async ({params} : {params:{id:string}}) => {
      const { userProfile } = await userService.getDashboardData();
  
const response = await userService.getWorkerPastSensorDetail(params.id);

  console.log("WORKER PAST SENSOR DETAIL RESPONSE:", response);

  if (!response.success || !response.data) {
    const errorMessage = response.error?.message || "Veri çekilemedi.";

    return (
      <div className='flex flex-col h-fit w-full p-4'>
        <Heading
          title={"Hata"}
          description={"Bir sorun oluştu."}
        />
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative">
          <strong className="font-bold">Hata: </strong>
          <span className="block sm:inline">{errorMessage}</span>
          <p className="text-sm mt-2">Sunucu yanıtı: 500 (Internal Server Error)</p>
        </div>
      </div>
    );
  }

  const sensorData = response.data.data || response.data; 

  return (
    <div className='flex flex-col h-fit w-full'>
      <Heading
        title={"Geçmişte Uğraştığınız Sensörler"}
        description={"Çözdüğünüz veya Çözemediğiniz Tüm Geçmişteki Sensörler Bu Sayfada gözükür"}
      />
      <SingleSensorFromPastSensors
        userProfileDto={userProfile}
        initialData={sensorData} 
      />
    </div>
  )
}

export default page