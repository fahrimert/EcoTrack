import React from 'react'
import { cookies } from 'next/headers'
import WrapperForContext from './components/WrapperForContext'
import OnlineUsers from './components/OnlineUsers'



const ManagerAnnonucementPage = async () => {
      const session = cookies().get("session");
     
     
    
  return (
    <div className='w-full h-fit flex flex-row max-md:flex max-md:flex-col'>

     <WrapperForContext session={session}/>

        <div className='w-full h-fit'>
<OnlineUsers session = {session} />
        </div>

    </div>
  )
}

export default ManagerAnnonucementPage