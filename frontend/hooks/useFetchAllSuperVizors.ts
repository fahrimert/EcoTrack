import { UserOnlineStatusDTO } from '@/app/sharedTypes'
import axios from 'axios'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import  { useEffect, useState } from 'react'

export const useFetchAllSuperVizors = (session : RequestCookie | undefined) => {
  const [loading, setLoading] = useState(true)
  const [errorForSupervizor, setErrorForSupervizor] = useState<any>(null)
      const [superVizorList,setSuperVizorList] = useState<UserOnlineStatusDTO[]>([])

       useEffect(() => {
    axios.get(`http://localhost:8080/manager/getAllSupervizors`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setSuperVizorList(res.data))
    .catch((err) => setErrorForSupervizor(err))

    .finally(() => setLoading(false))

  }, []); 


  return {superVizorList,loading,errorForSupervizor}
}

      
      
      
      
  