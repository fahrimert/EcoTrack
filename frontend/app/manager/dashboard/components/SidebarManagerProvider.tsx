import React from 'react'
import { cookies } from 'next/headers';
import SidebarManager from './SidebarManager';
interface SidebarProviderProps  {
  children: React.ReactNode;
}
const SidebarManagerProvider:React.FC<SidebarProviderProps> = ({children} ) => {
  const session = cookies().get("session")
  return (
      <SidebarManager children = {children} session = {session} />
  )
}

export default SidebarManagerProvider