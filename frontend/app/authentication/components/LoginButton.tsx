"use client"
import { cn } from '@/lib/utils';
import React from 'react'
import { useFormStatus } from 'react-dom';

const LoginButton = ({ session }: { session: any }) => {
    const { pending } = useFormStatus(); 
  return (
    <div className="relative w-full h-fit flex flex-col justify-center items-center gap-[10px]">
    <button
disabled={pending || !!session}
      type="submit"
      className={cn(`relative w-full h-[40px] text-[14px] leading-[14px] bg-white border-[#3a3a3a] border-[1px] rounded-[20px] hover:bg-opacity-90 hover:bg-[#0d0d1f] transition-all hover:text-white ${pending ? "blur-lg" : null}`)}  
    >
<span className="flex items-center justify-center w-full h-full">
          {pending ? "Giriş Yapılıyor..." : "Giriş Yapın"}
        </span>    </button>
  </div>  )
}

export default LoginButton