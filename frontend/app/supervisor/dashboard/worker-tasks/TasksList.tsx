"use client"
import React, { useEffect, useState } from 'react'
import { Task } from './page'
import { DataTable } from '@/components/ui/DataTable'
import { columns } from './TaskListDataTableComponents/columns'
import SockJS from 'sockjs-client'
import { Client, over } from 'stompjs'

const TasksList = ({sensorfilterBasedStat} : {sensorfilterBasedStat:Task[]}) => {
  const [tasks, setTasks] = useState<Task[]>(sensorfilterBasedStat);

  
    let stompClient: Client;
  
    useEffect(() => {
      const socket = new SockJS('http://localhost:8080/ws'); 
      stompClient = over(socket);
      stompClient.connect({}, (frame) => {
        console.log("Connected: " + frame); 
        stompClient.subscribe('/topic/tasks', (message) => {
          const updatedSensor = JSON.parse(message.body);
          console.log(updatedSensor);
       setTasks(prevTasks =>
  prevTasks.map(task =>
    task.id === updatedSensor.id ? updatedSensor : task
  )
);
        });
      }, (error) => {
        console.error("WebSocket bağlantı hatası:", error);
      });
  
     
    }, []);

    console.log(tasks);
  return (
    <div className=" w-full h-fit items-start justify-start   p-[10px]   gap-[5px] rounded-[30px]">

                                
         <DataTable
             
             searchKey="sensorName"
             columns={columns}
             data={tasks }
       
        />  

  </div>
  )
}

export default TasksList