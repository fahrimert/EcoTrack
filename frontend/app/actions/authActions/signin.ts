"use server";
import { SignİnFormSchema, FormState } from "@/lib/definitions";
import axios from "axios";
import { cookies } from "next/headers";

export async function signin(state: FormState, formData: FormData) {
  const validatedFields = SignİnFormSchema.safeParse({
    email: formData.get("email"),
    password: formData.get("password"),
  });

  try {

    
    if (!validatedFields.success) {
      return {
        errors: validatedFields.error.flatten().fieldErrors,
      };
    }
  if(validatedFields.success){
      const response = await axios.post("http://localhost:8080/auth/login",{
        "email":validatedFields.data.email,
        "password":validatedFields.data.password
      })

      const { accessToken, refreshToken } = response.data.data;
      const cookieStore = cookies();

      cookieStore.set("session", accessToken, {
      httpOnly: true,
      sameSite: "lax",
      path: "/",
      maxAge: 15 * 60, 
    });

    cookieStore.set("refresh", refreshToken, {
      httpOnly: true,
      sameSite: "lax",
      path: "/",
      maxAge: 7 * 24 * 60 * 60,
    });

    return {
      serverSuccess: "Successfully Logged In",
       accessToken, refreshToken
    };
  }
  

}
 catch (error: any) {
    console.error("Login Error:", error.response?.data || error.message);

    if (error.response?.data?.error) {
       return { serverError: error.response.data.message || "Giriş başarısız." };
    }
    
    return {
      serverError: "Sunucuya bağlanılamadı veya hatalı giriş.",
    };
  }
 
 

}
