"use server";
import { SignİnFormSchema, FormState } from "@/lib/definitions";
import axios from "axios";
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
try {
  if(validatedFields.success){
    try {
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

      const accessTokenExtract = await axios.get(`http://localhost:8080/user/${session}`,config)
      console.log(accessTokenExtract.data);
      return {
        serverSuccess: 'Başarıyla giriş yapıldı',
      };
    } catch (error : any) {
      return {
        serverError: error.response.data.errors,
      };
    }
  }
} catch (error : any) {

  return {
    serverError: 'Sunucuya ulaşılamıyor',
  };
}
    


  } catch (error) {
    console.log((error as Error).message)
    return {
      serverError: "Bir Sorun Oluştu   ",
    };
  }
  redirect("/");

}
