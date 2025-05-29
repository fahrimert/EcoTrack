import React from 'react'
import TasksList from './TasksList'
import { cookies } from 'next/headers'
import WrapperForContext from './WrapperForContext'


export interface Task {
  id: number;
  superVizorDescription: string;
  superVizorDeadline: string; 
  assignedTo: {
    id: number;
    firstName: string;
    surName: string;
  };
  assignedBy: {
    id: number;
    firstName: string;
    surName: string;
  };
  sensorDTO: {
    id: number;
    sensorName: string;
    latitude: number;
    longitude: number;
  };
  workerArriving: string | null; 
  workerArrived: string | null;  
}
const WorkerTasksPage = async () => {
      const session = cookies().get("session");
         const response = await fetch(`http://localhost:8080/tasks/getTasks`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${session?.value}`,
            "Content-Type": "application/json",
          },
        });
    
        const sensorfilterBasedStat = await response.json() as Task
        
    
  return (
    <div className='w-full h-fit flex flex-col'>

     <WrapperForContext session={session?.value}/>

        <div className='w-full h-fit'>
<TasksList sensorfilterBasedStat = {sensorfilterBasedStat}/>
        </div>

    </div>
  )
}

export default WorkerTasksPage