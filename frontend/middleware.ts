import { NextRequest, NextResponse } from "next/server";
import { cookies } from "next/headers";
import { updateSession } from "./lib/lib";
import jwt from 'jsonwebtoken'

export async function middleware(request: NextRequest) {
  const session = request.cookies.get("session")?.value;
  const refreshToken = request.cookies.get("refresh")?.value;
  const pathname = request.nextUrl.pathname;
    if (!session && !request.nextUrl.pathname.startsWith("/authentication") ) {
  return Response.redirect(new URL("/authentication", request.url));
} 
if (!pathname.startsWith("/authentication")) {
  
  if (!session && !refreshToken) {
      return NextResponse.redirect(new URL("/authentication", request.url));
    }

    let currentSession = session;
    if (!session || isTokenExpired(session)) {
       if (refreshToken) {
         const response = await updateSession(request);
         if (response) return response;
       } 
       return NextResponse.redirect(new URL("/authentication", request.url));
    }
  try {

    const decoded: any = jwt.decode(currentSession!);


     const roleRouteMap = {
    ROLE_WORKER: '/worker',
    ROLE_SUPERVISOR: '/supervisor',
    ROLE_MANAGER : '/manager'
  };
      const userRole = decoded?.authorities?.[0]; 
      const allowedPath = roleRouteMap[userRole];

      if (allowedPath && !pathname.startsWith(allowedPath)) {
        return NextResponse.redirect(new URL(`${allowedPath}/dashboard`, request.url));
      }


  } catch (error) {
      console.error("Middleware decode hatası:", error);
      return NextResponse.redirect(new URL("/authentication", request.url));
    }
   }

    return NextResponse.next();
}
function isTokenExpired(token: string): boolean {
  try {
    const decoded: any = jwt.decode(token);
    if (!decoded?.exp) return true; 
    
    return Date.now() >= decoded.exp * 1000;
  } catch (e) {
    return true; 
  }
}
export const config = {
  matcher: ["/((?!api|_next/static|_next/image|.*\\.png$).*)"],
};
