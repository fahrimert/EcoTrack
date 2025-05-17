"use client"

import React, { useState } from 'react'
import Calendar from './Calendar'
import { DateContext } from '@/context/DateContext'
import PastSensorsForReportWrapper from './PastSensorsForReportWrapper'

const WrapperForContext = ({session} : {session: string | undefined}) => {
      const [date,setDate] = useState({
        sortDate: null
      })
  return (
    <>
           <DateContext.Provider value={{date,setDate}}>

    <div className=' flex flex-row w-full h-fit max-xl:flex  max-xl:flex-col items-center'>

<div className=' w-fit h-fit max-xl:w-fit'>

<Calendar session = {session}/>
</div> 
<div className='w-full max-xl:w-[70%]'>
    <PastSensorsForReportWrapper  session = {session}/>
</div>
    </div>

           </DateContext.Provider>

    </>

  )
}

export default WrapperForContext