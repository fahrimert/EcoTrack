import axios from "axios";
import { cookies } from "next/headers";
import { NextRequest, NextResponse } from "next/server";

const secretKey = process.env.TOKEN_SECRET ;

const key = new TextEncoder().encode(secretKey);




  export async function getSessionData() {
    const session = cookies().get("session")?.value;
    if (!session) return null; 
    const config = {
      headers:{Authorization:`Bearer ${session}`}
    }
    const accessTokenExtract = await axios.get(`http://localhost:8080/user/${session}`,config)
    
    return accessTokenExtract.data
  }
  
  export async function updateSession(request: NextRequest) {

    const res = NextResponse.next(); // Create a new response
    
    const refreshToken = cookies().get("refreshToken")?.value;
    const session = cookies().get("session")?.value;
    
    const config = {
      headers:{Authorization:`Bearer ${session}`}
    }
    if (session && refreshToken) {
      const newAccessToken = await axios.post(`http://localhost:8080/refreshToken/${refreshToken}`,config) as string
      const parsedNewAccess = await getSessionData()  as {
        sub:string
        authorities: string[],
        iat: number,
        exp: number
      }
      res.cookies.set({
        name: "session",
        value: newAccessToken , // Encrypt and set the updated session data
        httpOnly: true,
        expires: parsedNewAccess.exp, // Set the expiration time
      });
      return res;
    }
    else{
      return null
    }
  }
