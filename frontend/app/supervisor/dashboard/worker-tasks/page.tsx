import React from 'react'
import TasksList from './TasksList'
import { cookies } from 'next/headers'
import WrapperForContext from './WrapperForContext'
import { Task } from '../../superVizorDataTypes/types'



const WorkerTasksPage = async () => {
      const session = cookies().get("session");


         const response = await fetch(`http://localhost:8080/supervizor/getTasksOfIAssigned`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${session?.value}`,
            "Content-Type": "application/json",
          },
        });
        console.log(response);
        const sensorfilterBasedStat = await response.json() as Task[]
        
        console.log(sensorfilterBasedStat);
  return (
    <div className='w-full h-fit flex flex-col'>

     <WrapperForContext session={session}/>

        <div className='w-full h-fit'>
<TasksList sensorfilterBasedStat = {sensorfilterBasedStat}/>
        </div>

    </div>
  )
}

export default WorkerTasksPage