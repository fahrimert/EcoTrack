"use server";
import axios from "axios";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import { getServerApi } from "../../util/api";

export async function logOut() {
  const cookieStore = cookies();
  const api =  getServerApi()
        const session = cookieStore.get("session")?.value;
        const refreshToken = cookieStore.get("refresh")?.value;

        if (refreshToken) { 
try {
        await api.post(
        "/auth/customLogout",
        { refreshToken: refreshToken },    
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${session}`,
          },
        }
      );
        
    } catch (error: any) {
      console.error("Backend logout hatası (ama işlem devam ediyor):", error.message);
    }
  }
          cookies().delete("session")
          cookies().delete("refresh")
          redirect('/authentication')

}
