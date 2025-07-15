"use server"
import axios from "axios";
import { cookies } from "next/headers";

export async function managerDeactivateUser(id:string){
try {
    const session = cookies().get("session");
  
         await axios.delete(`http://localhost:8080/manager/deactivateUser/${id}`,
            {        headers:{Authorization:`Bearer ${session?.value}`}
            ,  withCredentials: true,}      )
    console.log(id + "user");

            
      

} catch (error  : any) {
  console.log(error.message);
}

}