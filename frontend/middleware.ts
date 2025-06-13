import { NextRequest, NextResponse } from "next/server";
import { cookies } from "next/headers";
import { updateSession } from "./lib/lib";
import jwt from 'jsonwebtoken'

export async function middleware(request: NextRequest, res: NextResponse) {
  const session = cookies().get("session")?.value;
  const refreshToken = cookies().get("refresh")?.value;

    if (!session && !request.nextUrl.pathname.startsWith("/authentication") ) {
  return Response.redirect(new URL("/authentication", request.url));
} 
  const pathname = request.nextUrl.pathname;

  try {
     const roleRouteMap = {
    ROLE_WORKER: '/worker',
    ROLE_SUPERVISOR: '/supervisor',
    ROLE_MANAGER : '/manager'
  };
   const decoded = jwt.decode(session);

  const allowedPath = roleRouteMap[decoded.authorities[0]];



  if (!pathname.startsWith(allowedPath)) {
    return NextResponse.redirect(new URL(`${allowedPath}/dashboard`, request.url));
  }
  } catch (error) {
    console.log(error);
  }
   
  /* eğer role supervizorse supervizorde dashboarda gitmesi lazım  */

    
    if ( refreshToken == null) {
      const response =  await updateSession(request);
      if (request.method === 'POST') {
        return response;
      }
    }
    

}

export const config = {
  matcher: ["/((?!api|_next/static|_next/image|.*\\.png$).*)"],
};
