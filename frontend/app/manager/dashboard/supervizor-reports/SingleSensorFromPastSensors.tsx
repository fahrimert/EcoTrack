"use client"
import React, { useEffect, useState } from 'react'
import LeftStack from './LeftStack';
import { PdfReport, PdfReportInduvual } from '../../types/types';


const SingleSensorFromPastSensors =  ({sensor,sensorData} : {sensor:PdfReport,sensorData:PdfReportInduvual}) => {

  return (
    <div className='w-full h-full flex flex-col justify-center items-center gap-[10px] bg-white'>
    <div className='relative  w-full h-full flex flex-row justify-center items-start gap-[20px] p-[5px] '>
<LeftStack sensor = {sensor} sensorData={ sensorData}/> </div>

</div>
  )
}

export default SingleSensorFromPastSensors