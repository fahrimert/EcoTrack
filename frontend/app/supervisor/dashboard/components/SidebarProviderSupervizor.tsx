import React from 'react'
import { cookies } from 'next/headers';
import SidebarSupervizor from './SidebarSuperVizor';
interface SidebarProviderProps  {
  children: React.ReactNode;
}
const SidebarProviderSupervizor:React.FC<SidebarProviderProps> = ({children} ) => {
  const session = cookies().get("session")
  return (
      <SidebarSupervizor children = {children} session = {session} />
  )
}

export default SidebarProviderSupervizor