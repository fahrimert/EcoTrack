import React from 'react'
import { cookies } from 'next/headers';
import { SensorData } from '../past-sensors/components/PastSensorList';
import AssignedSensorAndMap from '../sensors/[id]/components/AssignedSensorAndMap';

const page = async ({params} : {params:{id:string} }) => {
    const session = cookies().get('session');
    const response = await fetch(`http://localhost:8080/sensors/${params.id}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${session?.value}`,
        'Content-Type': 'application/json'
      }
    });
    const statuses = await fetch(`http://localhost:8080/sensors/getSensorStatuses`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${session?.value}`,
        'Content-Type': 'application/json'
      }
    });
    const initialData = await response.json() as SensorData
    
    const stasusesData = await statuses.json() as [ 'ACTIVE', 'FAULTY', 'IN_REPAIR', 'SOLVED' ]

  return (
    <>
  <AssignedSensorAndMap initialData = {initialData} session={session}  stasusesData = {stasusesData}/>
</>  
)
}

export default page 