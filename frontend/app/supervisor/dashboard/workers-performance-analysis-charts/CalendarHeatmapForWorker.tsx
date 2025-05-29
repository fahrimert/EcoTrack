"use client"

import React, { useState } from 'react'
import CalendarHeatmapForWorker from './Calendar'
import { DateTypeCount } from './ChartComponent'

const WrapperForContext = ({session,
    dataForCalendar
} : {session: string | undefined , dataForCalendar : DateTypeCount[]} ,) => {
  return (
    <>

    <div className=' flex flex-row w-fit h-fit max-xl:flex  max-xl:flex-col items-center'>

<div className=' w-fit h-fit max-xl:w-fit'>

<CalendarHeatmapForWorker session = {session} dataForCalendar = {dataForCalendar} />
</div> 

    </div>


    </>

  )
}

export default WrapperForContext