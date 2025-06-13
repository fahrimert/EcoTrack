import { UserOnlineStatusDTO } from '@/app/supervisor/superVizorDataTypes/types'
import axios from 'axios'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import  { useEffect, useState } from 'react'

export const useAllUsers = (session : RequestCookie | undefined) => {
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<any>(null)
      const [users,setUsers] = useState<UserOnlineStatusDTO[]>([])

       useEffect(() => {
    axios.get(`http://localhost:8080/superVizorSensors/getAllWorker`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setUsers(res.data))
    .catch((err) => setError(err))

    .finally(() => setLoading(false))

  }, []); 


  return {users,loading,error}
}

      
      
      
      
  