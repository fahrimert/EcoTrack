import React from 'react'
import Image from "next/image";
import Link from "next/link";
import Autocomplete, { usePlacesWidget } from "react-google-autocomplete";

const Sensor = () => {
  return (

    <div className=" flex flex-col w-full h-fit  rounded-[30px]  p-[10px] gap-[20px] " >
  
    
  
  <div className=" bg-white rounded-[30px] flex flex-row w-full h-fit    justify-center items-center  ; ">
  <Image
              src={"/indir.jpg"}
              alt="232"
              className="w-full  object-contain  "
              width={100} 
              height={100}
            />
    <div className=" h-full w-full justify-between items-center flex flex-col ">
  
      <h2 className="text-[13px] font-normal    ml-[38px] mr-0 mt-0">Sensör 18 </h2>
      <h2 className="text-[13px] font-normal    ml-[38px] mr-0 mt-0">Adana/Çukurova </h2>
      <h2 className="text-[13px] font-normal    ml-[38px] mr-0 mt-0">Arızalı </h2>
  
  
    </div>
  
  </div>
  <div className="h-full w-full flex relative justify-betweeen items-end gap-[50px]">
  
  <button type="button" className="  h-[50px] w-full rounded-[20px] box-border text-sm font-medium   text-white bg-black ">Detaylar</button>
  <button type="button" className="  h-[50px] w-full  rounded-[20px] box-border text-sm font-medium   text-white bg-black ">Gidin</button>
  </div>
  
  
  </div>  )
}

export default Sensor