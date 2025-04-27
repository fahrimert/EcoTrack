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
    const refreshToken = request.cookies.get('refresh')?.value;
    const session = request.cookies.get('session')?.value;
  
    if (!refreshToken || !session) {
      return NextResponse.next();
    }
  
    try {
      const response = await fetch(`http://localhost:8080/refreshToken/${refreshToken}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${session}`,
          'Content-Type': 'application/json'
        }
      });
  
      if (!response.ok) {
        throw new Error('Token refresh failed');
      }
  
      const newAccessToken = await response.text();
      const res = NextResponse.next();
      
      res.cookies.set({
        name: 'session',
        value: newAccessToken,
        httpOnly: true,
          maxAge: 15 * 60 // 15 dakika
      });
  
      return res;
    } catch (error) {
      console.error('Token refresh error:', error);
      const res = NextResponse.redirect(new URL('/authentication', request.url));
      res.cookies.delete('session');
      res.cookies.delete('refresh');
      return res;
    }
  }