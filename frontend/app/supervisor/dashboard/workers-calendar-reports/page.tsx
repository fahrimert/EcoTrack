import React from 'react'
import { cookies } from 'next/headers';
import WrapperForContext from './components/WrapperForContext';

const page = async () => {
    const session = cookies().get('session')
  return (
    <div className=' w-full h-screen max-xl:h-fit flex flex-row mt-[10px] items-center justify-center '> 
    <WrapperForContext session = {session}/>
    </div>

  )
}

export default page