import React from 'react'
import { cookies } from 'next/headers';
import AssignedTaskAndMap from './components/AssignedTaskAndMap';
import { TaskDetail } from './components/AssignedTaskFormForSolving';
import { notFound } from 'next/navigation';

const page = async ({params} : {params:{id:string} }) => {
    const session = cookies().get('session');

    const statuses = await fetch(`http://localhost:8080/sensors/getSensorStatuses`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${session?.value}`,
        'Content-Type': 'application/json'
      }
    });
    
    const stasusesData = await statuses.json() as [ 'ACTIVE', 'FAULTY', 'IN_REPAIR', 'SOLVED' ]
        const user  = await fetch(`http://localhost:8080/worker/getTheDetailOfLoggedWorker/${session?.value}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${session?.value}`,
        'Content-Type': 'application/json'
      }
     })
     
     const responseOfUser =await  user.json() as {
   id: number;
  firstName: string;
  surName: string;
  role: string;
  userOnlineStatus: {
  id: number;
  isOnline: boolean;
  createdAt: string | null;
}

}




        const tasks  = await fetch(`http://localhost:8080/worker/getTasksOfMe/${responseOfUser?.id}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${session?.value}`,
        'Content-Type': 'application/json'
      }
     }) 




      const responseOfTasks =await  tasks.json() as  TaskDetail[] 

     const filteredSensorBasedOnParam = responseOfTasks.filter((a) => a.taskSensors.id == params.id)[0]
     
  if (filteredSensorBasedOnParam == null || responseOfTasks.length == 0 ) {
    return notFound()
  }  

  return (
    <>
  
  <AssignedTaskAndMap stasusesData = {stasusesData} initialData = {filteredSensorBasedOnParam} session={session} />
</>  
)
}

export default page 