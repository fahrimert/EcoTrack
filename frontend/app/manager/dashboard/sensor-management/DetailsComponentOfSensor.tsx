"use client"

import React from 'react'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import SensorSheetComponentForManager from './UserDetailSheetComponentForManager';
import { SensorListForManagerUse } from './SensorManagementSensorsWrapperComponent';






const DetailsComponentOfSensor =  ({sensors,session,open ,setOpen,setOpenMainDialog} : {
  sensors:  SensorListForManagerUse ,
  session:RequestCookie | undefined,
  open:boolean,
    setOpen: (value: boolean) => void;
    setOpenMainDialog: (value: boolean) => void;

 }) => {

console.log(sensors);
  return (

    <div className='flex flex-col h-full w-full justify-start items-center'
    >

   
<SensorSheetComponentForManager
    sensors = {sensors}
    session = {session}
    open = {open}
    setOpen = {setOpen}
    setOpenMainDialog= {setOpenMainDialog}
   />

    </div>
  )
}

export default DetailsComponentOfSensor