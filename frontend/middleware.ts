import type { NextRequest, NextResponse } from "next/server";
import { cookies } from "next/headers";
import { updateSession } from "./lib/lib";

export async function middleware(request: NextRequest, res: NextResponse) {
  const session = cookies().get("session")?.value;

  /* şöyle bi mantık olması lazım  */

  if (!session && !request.nextUrl.pathname.startsWith("/authentication") ) {
  return Response.redirect(new URL("/authentication", request.url));
} 
if (session && request.nextUrl.pathname.startsWith("/authentication")) {
  return Response.error()
 }

   
return await updateSession(request);
}

export const config = {
  matcher: ["/((?!api|_next/static|_next/image|.*\\.png$).*)"],
};
