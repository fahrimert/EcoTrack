"use server";
import { SignİnFormSchema, FormState } from "@/lib/definitions";
import axios from "axios";
import jwt from "jsonwebtoken";
import { cookies, headers } from "next/headers";
import { redirect } from "next/navigation";

export async function signin(state: FormState, formData: FormData) {
  const validatedFields = SignİnFormSchema.safeParse({
    email: formData.get("email"),
    name:formData.get("name"),
    password: formData.get("password"),
  });

  try {

    
    if (!validatedFields.success) {
      return {
        errors: validatedFields.error.flatten().fieldErrors,
      };
    }
  if(validatedFields.success){
      const data = await axios.post("http://localhost:8080/login",{
        "email":validatedFields.data.email,
        "firstName":validatedFields.data.name,
        "password":validatedFields.data.password
      })

      const session = data.data.data.accessToken
      const refresh = data.data.data.refreshToken

      
      cookies().set("session",session,{httpOnly:true,})
      cookies().set("refresh",refresh,{httpOnly:true,})

      const config = {
        headers:{Authorization:`Bearer ${session}`}
      }
        const sessionData = cookies().get("session")?.value;

   const roleRouteMap = {
    ROLE_WORKER: '/worker',
    ROLE_SUPERVISOR: '/supervisor',
  };
   const decoded = jwt.decode(session);

  const allowedPath = roleRouteMap[decoded.authorities[0]];


  redirect(`/${allowedPath}/dashboard`)
    
  }
  

}
 catch (error : any) {
   
    return {
      serverError: error.response.data.errors,
    }; 
  }
 
 

}
