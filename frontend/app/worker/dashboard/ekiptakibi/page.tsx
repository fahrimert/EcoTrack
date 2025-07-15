import React from 'react'
import { cookies } from 'next/headers';

import UserSensorsAndMapOFAllUsers from './components/SensorAndMapOFAllUSers';

const page = async ({params} : {params:{id:string} }) => {
     const session  = cookies().get(  "session")
     const response = await fetch(`http://localhost:8080/workers/getAllWorkersSessionSensorAndTheirLocation`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${session?.value}`,
        "Content-Type": "application/json",
      },
    });

    const usersAndTheirSensors = await response.json()


  return (
    <>
  <UserSensorsAndMapOFAllUsers  session={session} usersAndTheirSensors = {usersAndTheirSensors}  />
</>  
)
}

export default page 