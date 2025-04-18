"use server";
import { SignİnFormSchema, FormState } from "@/lib/definitions";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";

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
    
    console.log(validatedFields.data);
/* 
    if (pswdvalid) {
      const expires = new Date(Date.now() + 500 * 1000);
      const session = await encrypt({ user, expires });
      cookies().set("session", session, { expires, httpOnly: true });
    }
 */
  } catch (error) {
    console.log((error as Error).message)
    return {
      serverError: "Bir Sorun Oluştu   ",
    };
  }
}
