import React from 'react'
import InputItem from './InputItem'
import { cookies } from 'next/headers'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'

const SearchUserLocation = ({session }: {session:RequestCookie}) => {
  
  return (
<div className='p-2 md:pd-6 border-[2px] rounded-xl'>
    <InputItem session = {session}/>
</div>

  )
}

export default SearchUserLocation