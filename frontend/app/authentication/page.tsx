import React from 'react'
import LoginComponent from './components/LoginComponent'
import { cookies } from 'next/headers';

const AuthPage = async () => {

  const session =   cookies().get("session")?.value
  
  return <LoginComponent session = {session} />;


  
}

export default AuthPage