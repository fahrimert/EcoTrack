import { DifferentUserProfileType } from '@/app/sharedTypes'
import axios from 'axios'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import  { useEffect, useState } from 'react'

export const useUserProfile = (session : RequestCookie | undefined) => {
  const [userProfile,setUserProfile] = useState<DifferentUserProfileType | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<any>(null)

    useEffect(() => {
    if (!session) return    
    
    axios.get(`http://localhost:8080/user/me ` , {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setUserProfile(res.data))
    .catch((err) => setError(err))
    .finally(() => setLoading(false))
  }, [session?.value]);

  return {userProfile,loading,error}
}
