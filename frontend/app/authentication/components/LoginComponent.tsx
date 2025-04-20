"use client";
import { useFormState } from "react-dom";
import { signin } from "@/app/actions/authActions/signin";
import toast from "react-hot-toast";
import { Button } from "@/components/ui/button";
import { useEffect, useState } from "react";
import Link from "next/link";
import { IoEyeOff,IoEye  } from "react-icons/io5";

import { cn } from "@/lib/utils";
import { EyeOff } from "lucide-react";
import { Separator } from "@radix-ui/react-separator";
const SigninComponent = () => {
  const [state, action] = useFormState(signin, undefined);
  const [valueForUniversity,setValueForUniversity] = useState("")
  const [valueForMajor,setValueForMajor] = useState("")
  const [loginState, setLoginState] = useState<boolean>(true);

    

  useEffect(() => {
    if (state?.serverError) {
      console.log(state.serverError)
      toast.error(state?.serverError);
    }
    if(state?.serverSuccess){
      console.log(state.serverSuccess)
      toast.success(state?.serverSuccess);
    }
  }, [state?.serverError ]);


    const [visible,setVisible] = useState(true)
    const [visibleForRegister,setVisibleForRegister] = useState(true)

  return (
    <div className=" w-full h-fit flex flex-row justify-center items-center  bg-[#0d0d1f]   >">
    

      <div className=" w-fit h-screen flex flex-col justify-center items-center    pt-[50px]   ">
            

              <h2 className={`w-fit h-fit text-[32px]   leading-[38px] text-white `}>
                Eco Track 
              </h2>
            <div className=" relative h-[700px] w-[450px] shadow-[0px_14px_32px_0px_rgba(250, 250, 250, 0.795)] bg-[#1a1a2e] mx-auto my-[50px] rounded-[7px_7px_7px_7px]; /* VIA CSS MATIC https://goo.gl/cIbnS */ flex flex-col justify-center items-center gap-[30px]  max-lg:items-center  rounded-[20px]">
            <h2 className={`w-fit h-fit text-[32px]   leading-[38px] text-white `}>
                Giriş Yapın 
              </h2>
              <form
                className=" relative w-fit h-fit flex flex-col justify-center items-center   max-lg:items-center "
                action={action}
              >
                <div className=" relative w-full h-fit flex flex-col justify-center items-start gap-[30px]  ">
                  <div className=" relative h-fit flex flex-col justify-center items-start gap-[15px]">
                
                    <input

                    
className={cn( " relative w-[300px] h-[40px] rounded-[10px] outline-none p-[12px] bg-[#1a1a2e] text-white  border-gray-400   hover:none",    state?.errors?.email && "border-red-400 border-[1px]  ring-1 ring-red-400")   }
type="email"
                      name="email"
                      id="email"
                      placeholder="Email"
                    />
                <div className="  w-[300px] h-[1px]  bg-white"/>
{state?.errors?.email && (
  
                      <h2 
                      
                      
                      className=" w-full text-[#e92021] text-[12px]  ring-[#cd3e2f] rounded-[10px] animate-fade-down animate-ease-in-out animate-normal animate-duration-[400ms]  ">
                        {state.errors.email}
                      </h2>
                    )}

<input

                    
className={cn( " relative w-[300px] h-[40px] rounded-[10px] outline-none p-[12px] bg-[#1a1a2e] text-white  border-gray-400   hover:none",    state?.errors?.name && "border-red-400 border-[1px]  ring-1 ring-red-400")   }
type="name"
                      name="name"
                      id="name"
                      placeholder="Soyadınız olmadan sadece Adınızı Yazınız"
                    />
                <div className="  w-[300px] h-[1px]  bg-white"/>
{state?.errors?.name && (
  
                      <h2 
                      
                      
                      className=" w-full text-[#e92021] text-[12px]  ring-[#cd3e2f] rounded-[10px] animate-fade-down animate-ease-in-out animate-normal animate-duration-[400ms]  ">
                        {state.errors.name}
                      </h2>
                    )}



           <div className="w-full h-fit flex flex-row">

                    <input
className={cn( " relative  w-[300px] h-[40px] rounded-[10px] outline-none p-[12px] bg-[#1a1a2e] text-white  border-gray-400   hover:none",    state?.errors?.email && "border-red-400 border-[1px]  ring-1 ring-red-400")   }
type= {visible  ? "password" : "text" } 
                      name="password"
                      id="password"
                      placeholder="Şifre"
                    />

<div className="w-fit h-full  flex items-center justify-center  ">
                      {visible ? 
                      
                      <IoEyeOff className="transition-all cursor-pointer"  color= "white" size={30} onClick={() => setVisible(!visible)}/>
                    : 
                    <IoEye className="transition-all cursor-pointer" color= "white"  size={30} onClick={() => setVisible(!visible)}/>

                    }
                    </div>
           </div>
           <div className=" h-[1px]  w-[300px] bg-white"/>
     
                <div className="w-fit h-fit flex flex-row">


                    {state?.errors?.password &&
                      state?.errors?.password.map((b,index) => (

                        <h2 key={index} 
                      className=" w-fit text-[#e92021] text-[12px]  ring-[#cd3e2f] rounded-[10px] animate-fade-down animate-ease-in-out animate-normal animate-duration-[400ms]  ">
                        
                            {b}
                        </h2>

                      ))}
                </div>
                      
                        </div>


                  <div className="relative w-full h-fit flex flex-col justify-center items-center gap-[10px]">
                    <button
                      type="submit"
                      className="relative w-full h-[40px] text-[14px] leading-[14px] bg-white border-[#3a3a3a] border-[1px] rounded-[20px] hover:bg-opacity-90 hover:bg-[#0d0d1f] transition-all hover:text-white "
                    >
                      <h2 className=" flex items-center justify-center text-black hover:text-white transition-all w-full h-full ">Giriş Yapın </h2>
                    </button>
                  </div>
                </div>
              </form>
              <div className="relative w-[300px] h-fit flex flex-row justify-between items-center">
                <div className="relative w-fit h-fit  text-white text-[16px] tracking-[-0.032px] leading-[38.4px]  ">
              
                <Link
                  className="relative w-full h-fit text-white text-[16px] tracking-[-0.032px] leading-[38.4px] hover:text-red-400 transition-all  "
                  href={"/auth/reset"}
                >
                      <h2 className=" flex items-center justify-center text-white hover:underline transition-all w-full h-full ">  Şifrenizi Mi Unuttunuz? </h2>
                      </Link>
           
                </div>


        
              </div>
            </div>
      </div>
    </div>
  );
};

export default SigninComponent;
