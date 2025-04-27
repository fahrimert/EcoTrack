import axios from 'axios';
import { cookies } from 'next/headers';
import React from 'react'
import Sidebar from '../components/Sidebar';
import SensorsAndMap from '../components/SensorsAndMap';

const page =  async() => {
  const session  = cookies().get("session")
  
/*   const accessToken = cookies().get("session")?.value;
 
 const config = {
    headers:{Authorization:`Bearer ${accessToken}`}
  }

  const session =await axios.get((`http://localhost:8080/user/profile/${accessToken}`),config) */
 

  return (
    <>


    <SensorsAndMap session = {session}/>

    </>
  )
}

export default page