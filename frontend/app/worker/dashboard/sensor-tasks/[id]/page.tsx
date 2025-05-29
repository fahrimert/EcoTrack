import React from 'react'
import { cookies } from 'next/headers';
import AssignedTaskAndMap from './components/AssignedTaskAndMap';
import { TaskDetail } from './components/AssignedTaskFormForSolving';
import { notFound } from 'next/navigation';

const page = async ({params} : {params:{id:string} }) => {
    const session = cookies().get('session');
    const response = await fetch(`http://localhost:8080/sensors/${params.id}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${session?.value}`,
        'Content-Type': 'application/json'
      }
    });
    const statuses = await fetch(`http://localhost:8080/sensors/getSensorStatuses`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${session?.value}`,
        'Content-Type': 'application/json'
      }
    });

        const user  = await fetch(`http://localhost:8080/user/profile/${session?.value}`, {
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




        const tasks  = await fetch(`http://localhost:8080/tasks/getTasksOfMe/${responseOfUser?.id}`, {
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
  
  <AssignedTaskAndMap initialData = {filteredSensorBasedOnParam} session={session} />
</>  
)
}

export default page 