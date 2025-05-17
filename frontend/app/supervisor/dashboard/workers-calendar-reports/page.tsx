import React, { useState } from 'react'
import Calendar from './components/Calendar'
import { cookies } from 'next/headers';
import WorkerPastSensorWrapper from './components/PastSensorsForReportWrapper';
import WrapperForContext from './components/WrapperForContext';

const page = async () => {
    const session = cookies().get('session')?.value;


  return (

    <div className=' w-full h-fit flex flex-row '> 
    <WrapperForContext session = {session}/>

    </div>

  )
}

export default page