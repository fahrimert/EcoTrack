"use client"
import React from 'react'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import SensorSheetComponent from './SensorSheetComponent';
import { SensorListForManagerUse } from './SensorManagementSensorsWrapperComponent';


const SensorSheetComponentForManager =  ({sensors,
  open,
  session,setOpen,
setOpenMainDialog} : {
  sensors:  SensorListForManagerUse,
open:boolean,  
setOpen: (value: boolean) => void,
setOpenMainDialog: (value: boolean) => void,
session:RequestCookie | undefined,
},
) => {
  return (
    <div className='w-full h-full flex flex-col justify-center items-center gap-[10px] bg-white'>
    <div className='relative  w-full h-full flex flex-row justify-center items-start gap-[20px] p-[5px] '>
   <SensorSheetComponent
   open = {open}
   setOpen = {setOpen} 
   setOpenMainDialog = {setOpenMainDialog}
   sensors = {sensors} session = {session}   />
  </div>

</div>
  )
}

export default SensorSheetComponentForManager