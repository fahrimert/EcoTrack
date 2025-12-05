
import { NextRequest, NextResponse } from "next/server";


  export async function updateSession(request: NextRequest) {
    const refreshToken = request.cookies.get("refresh")?.value;
  
    if (!refreshToken) {
    return null;
  }
  
    try {
      const response = await fetch("http://localhost:8080/refreshToken", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ refreshToken: refreshToken }),
    });
  
      if (!response.ok) {
        throw new Error('Refresh Token Başarısız Oldu');
      }
  
      const data = await response.json();
      const newAccessToken = data.accessToken;
      
      
      const responsee = NextResponse.redirect(request.url);


      responsee.cookies.set({
        name: 'session',
        value: newAccessToken,
      httpOnly: true,
      path: "/",
      maxAge: 15 * 60, 
      sameSite: "lax",
      });
  
      return responsee;
    } catch (error) {
      console.error('Token refresh error:', error);
      const res = NextResponse.redirect(new URL('/authentication', request.url));
      res.cookies.delete('session');
      res.cookies.delete('refresh');
      return res;
    }
  }