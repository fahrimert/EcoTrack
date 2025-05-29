"use client"

import React, { useState } from 'react'
import { WorkerLocationContext } from '@/context/WorkerLocationContext'
import { SensorDestinationContext } from '@/context/SensorDestinationContext'
import AttachTaskForm from './AttachTask'
import WorkerMap from './WorkerMap'

const WrapperForContext = ({session} : {session: string | undefined}) => {
       const [source,setSource] = useState({
         lat:39.9334,
         lng: 32.8597
       })
       const [destination,setDestination] = useState({
         lat:null,
         lng: null
       })
  return (
    <>
           <WorkerLocationContext.Provider value={{source,setSource}}>
         <SensorDestinationContext.Provider  value={{destination,setDestination}}>
        <div className='w-full h-fit flex flex-row'>
            {/* ilk formu yapalım  */}
            <div className='w-full h-fit'>
<AttachTaskForm session={session}/>

            </div>
               <div className='w-full h-fit'>
<WorkerMap session = {session}/>
            </div>
        </div>
         </SensorDestinationContext.Provider>

           </WorkerLocationContext.Provider>

    </>

  )
}

export default WrapperForContext