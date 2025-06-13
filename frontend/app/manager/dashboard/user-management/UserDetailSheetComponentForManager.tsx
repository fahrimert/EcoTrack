"use client"
import React from 'react'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import UserSheetComponent from './UserSheetComponent';
import { UserAndSupervizorsDTO} from '@/app/supervisor/superVizorDataTypes/types';


const UserDetailSheetComponentForManager =  ({user,
  open,
  session,sessionId,setOpen,
setOpenMainDialog} : {
user :  UserAndSupervizorsDTO,
open:boolean,  
setOpen: (value: boolean) => void,
setOpenMainDialog: (value: boolean) => void,
session:RequestCookie | undefined,
sessionId: string},
) => {
  console.log(user);
 
  return (
    <div className='w-full h-full flex flex-col justify-center items-center gap-[10px] bg-white'>
    <div className='relative  w-full h-full flex flex-row justify-center items-start gap-[20px] p-[5px] '>
   <UserSheetComponent 
   open = {open}
   setOpen = {setOpen} 
   setOpenMainDialog = {setOpenMainDialog}
   user = {user} session = {session}  sessionId = {sessionId}  />
  </div>

</div>
  )
}

export default UserDetailSheetComponentForManager