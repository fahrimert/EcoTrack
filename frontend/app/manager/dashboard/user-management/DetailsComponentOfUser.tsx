"use client"

import React from 'react'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import UserDetailSheetComponentForManager from './UserDetailSheetComponentForManager';
import { UserAndSupervizorsDTO } from '@/app/supervisor/superVizorDataTypes/types';






const DetailsComponentOfUser =  ({user,session,sessionId,open ,setOpen,setOpenMainDialog} : {
  user :  UserAndSupervizorsDTO,
  session:RequestCookie | undefined,
  open:boolean,
    setOpen: (value: boolean) => void;
    setOpenMainDialog: (value: boolean) => void;

  sessionId: string}) => {

console.log(session);
  return (

    <div className='flex flex-col h-full w-full justify-start items-center'
    >

   
<UserDetailSheetComponentForManager
    user = {user}
    session = {session}
    sessionId = {sessionId}
    open = {open}
    setOpen = {setOpen}
    setOpenMainDialog= {setOpenMainDialog}
   />

    </div>
  )
}

export default DetailsComponentOfUser