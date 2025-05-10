import React from 'react'
import { cookies } from 'next/headers';

import UserSensorsAndMapOFAllUsers from './components/SensorAndMapOFAllUSers';
/* totalde buna 5-6 saat nasıl uğraştım ya  */

const page = async ({params} : {params:{id:string} }) => {
     const session  = cookies().get("session")
     const response = await fetch(`http://localhost:8080/getAllUserLocation`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${session?.value}`,
        "Content-Type": "application/json",
      },
    });


    const usersAndTheirSensors = await response.json() as [{  id: number;
      name: string;
      latitude: number;
      longitude: number;
      sensorlatitude: number;
      sensorlongitude: number;
      sensor: {
        id: number;
        sensorName: string;
        status: string;
        installationDate: string;
      };}];

  return (
    <>
    {/* Workers and their uncompleted sensors  */}
  <UserSensorsAndMapOFAllUsers  session={session} usersAndTheirSensors = {usersAndTheirSensors}  />
</>  
)
}

export default page 