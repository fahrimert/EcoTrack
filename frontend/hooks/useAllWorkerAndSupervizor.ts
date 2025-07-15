import { UserAndSupervizorsDTO } from '@/app/supervisor/superVizorDataTypes/types'
import axios from 'axios'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import  { useEffect, useState } from 'react'

export const useAllWorkerAndSupervizor = (session : RequestCookie | undefined) => {
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<any>(null)
      const [userAndSupervizor,setUserAndSupervizor] = useState<UserAndSupervizorsDTO[]>([])

       useEffect(() => {
    axios.get(`http://localhost:8080/manager/getAllSupervizorsAndUsers`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setUserAndSupervizor(res.data))
    .catch((err) => setError(err))

    .finally(() => setLoading(false))

  }, []); 


  return {userAndSupervizor,loading,error}
}

      
      
      
      
  