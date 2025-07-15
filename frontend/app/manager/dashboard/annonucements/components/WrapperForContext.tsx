"use client"

import React from 'react'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import MakeAnnonucementForm from './MakeAnnonucementForm'

const WrapperForContext = ({session} : {session:RequestCookie | undefined}) => {
   
  return (
    <>
  
        <div className='w-full h-fit flex flex-row'>
            <div className='w-full h-fit'>
<MakeAnnonucementForm session={session}/>

            </div>
            
        </div>


    </>

  )
}

export default WrapperForContext