"use server";
import axios from "axios";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export async function logOut() {
 
  try {
        const session = cookies().get("session")?.value
        
        ;
        const response = axios.post(
            'http://localhost:8080/auth/customLogout',
            {}, 
            {
              headers: {
                'Authorization': `Bearer ${session}`,
                'Content-Type': 'application/json',
              },
            }
          )
          .then(response => {
            console.log("Logout başarılı:", response.data);
          })
          .catch(error => {
            console.error("Logout hatası:", error);
          });
        
        
          cookies().delete("session")
          cookies().delete("refresh")

  
        }
 catch (error : any) {
   if (error.response?.data?.error) {
    return {
      serverError: error.response.data.errors,
    };
   }
   if (error.response) {
       console.error("Unexpected error response:", error.response.data);
    return {
      serverError: ["Unexpected server error occurred."],
    };
   }
     console.error("Network or unknown error:", error.message);

    return {
      serverError: "Network or unknown error:",
    }; 
  }
 
  finally{
          redirect('/authentication')

  }

}
