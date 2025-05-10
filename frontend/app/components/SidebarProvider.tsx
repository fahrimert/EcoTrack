import React from 'react'
import NewSidebar from './Sidebar'
import { cookies } from 'next/headers';
interface SidebarProviderProps  {
  children: React.ReactNode;
}
const SidebarProvider:React.FC<SidebarProviderProps> = ({children} ) => {
  const session = cookies().get("session")?.value;
  return (
      <NewSidebar children = {children} session = {session} />
  )
}

export default SidebarProvider