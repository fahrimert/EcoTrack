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
const SigninComponent = () => {
  const [state, action] = useFormState(signin, undefined);
  const [valueForUniversity,setValueForUniversity] = useState("")
  const [valueForMajor,setValueForMajor] = useState("")
  const [loginState, setLoginState] = useState<boolean>(true);

    


    const [visible,setVisible] = useState(true)
    const [visibleForRegister,setVisibleForRegister] = useState(true)
  return (
    <div className=" w-full h-fit flex flex-row justify-between items-center  bg-[#191919]>">
    

      <div className=" w-full h-screen flex flex-col justify-center items-center  bg-[#191919]   ">
            <div className=" relative w-[500px] h-fit flex flex-col justify-center items-center gap-[30px] p-[30px] bg-[#03002d]  max-lg:items-center max-lg:bg-[#191919]">
            

              <h2 className={`w-fit h-fit text-[32px]   leading-[38px] text-white `}>
                Eco tracke Giriş Yapın
              </h2>
           
              <form
                className=" relative w-[300px] h-fit flex flex-col justify-center items-center   bg-[#03002d] max-lg:items-center max-lg:bg-[#191919]"
                action={action}
              >
                <div className=" relative w-full h-fit flex flex-col justify-center items-start gap-[30px]  ">
                  <div className=" relative w-full h-fit flex flex-col justify-center items-start gap-[15px]">
                
                    <input

                    
className={cn( " relative w-full h-[40px] rounded-[10px] p-[12px] bg-[#03002d] text-white  border-gray-400 border-b-[1px]",    state?.errors?.email && "border-red-400 border-[1px]  ring-1 ring-red-400")   }
type="email"
                      name="email"
                      id="email"
                      placeholder="230******@stu.thk.edu.tr"
                    />

{state?.errors?.email && (
  
                      <h2 
                      
                      
                      className=" w-full text-[#e92021] text-[12px]  ring-[#cd3e2f] rounded-[10px] animate-fade-down animate-ease-in-out animate-normal animate-duration-[400ms]  ">
                        {state.errors.email}
                      </h2>
                    )}

           <div className="w-full h-fit flex flex-row">

                    <input
className={cn( " relative w-full h-[40px] rounded-[10px] p-[12px] text-white bg-[#03002d]  border-gray-400 border-b-[1px]" ,    state?.errors?.password && "border-red-400 border-[1px]  ring-1 ring-red-400")   }
type= {visible  ? "password" : "text" } 
                      name="password"
                      id="password"
                      placeholder="Şifre"
                    />

<div className="w-fit h-full  flex items-center justify-center border-b-[1px] ">
                      {visible ? 
                      
                      <IoEyeOff className="transition-all cursor-pointer"  color= "white" size={30} onClick={() => setVisible(!visible)}/>
                    : 
                    <IoEye className="transition-all cursor-pointer" color= "white"  size={30} onClick={() => setVisible(!visible)}/>

                    }
                    </div>
           </div>
     
                <div className="w-full h-fit flex flex-row">


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
                      className="relative w-[70%] h-[40px] text-[14px] leading-[14px] bg-[#191919] border-[#3a3a3a] border-[1px] rounded-[20px] hover:bg-opacity-90 hover:bg-red-400 transition-all hover:text-white "
                    >
                      <h2 className=" flex items-center justify-center text-[#757375] hover:text-white transition-all w-full h-full ">Giriş Yapın </h2>
                    </button>
                  </div>
                </div>
              </form>
              <div className="relative w-[300px] h-fit flex flex-row justify-between items-center">
                <div className="relative w-fit h-fit  text-white text-[16px] tracking-[-0.032px] leading-[38.4px]  ">
              
                <Link
                      className="relative w-[70%] h-[40px] text-[14px] leading-[14px] bg-[#191919] border-[#3a3a3a] border-[1px] rounded-[20px] hover:bg-opacity-90 hover:bg-red-400 transition-all hover:text-white "
                      href={"/auth/reset"}
                >
                      <h2 className=" flex items-center justify-center text-[#757375] hover:text-white transition-all w-full h-full ">  Şifrenizi Mi Unuttunuz? </h2>
                      </Link>
           
                </div>

                <Button
                  className="relative w-full h-fit text-white text-[16px] tracking-[-0.032px] leading-[38.4px] hover:text-red-400 transition-all  "
                  variant={null}
                  onClick={() => setLoginState(!loginState)}
                >
                  Hesap Oluşturun
                </Button>
              </div>
            </div>
      </div>
    </div>
  );
};

export default SigninComponent;
