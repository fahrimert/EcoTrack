"use server"
import axios from "axios";
import { cookies } from "next/headers";

export async function managerDeleteSensorAction(id:string ){
try {
    const session = cookies().get("session");
  
         await axios.delete(`/manager/deleteSensorById/${id}`,
            {        headers:{Authorization:`Bearer ${session?.value}`}
            ,  withCredentials: true,}      )
    console.log(id + "user");

            
      

} catch (error  : any) {
  console.log(error.message);
}

}