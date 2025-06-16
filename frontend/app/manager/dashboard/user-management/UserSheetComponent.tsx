  "use client"
  
import { managerDeactivateUser } from "@/app/actions/userActions/managerDeactivateUser";
import { managerDeleteUserAction } from "@/app/actions/userActions/managerDeleteUserAction";
import { UserAndSupervizorsDTO } from "@/app/supervisor/superVizorDataTypes/types";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogTrigger } from "@/components/ui/dialog";
import { Separator } from "@/components/ui/separator";
import axios from "axios";
import { format } from "date-fns";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { useEffect, useState } from "react";

  const UserSheetComponent = ({
    user,
    session ,
    open,
  setOpen,
  setOpenMainDialog
  } : {
open:boolean,
  user :  UserAndSupervizorsDTO,
  session: RequestCookie | undefined,
    setOpen: (value: boolean) => void;
    setOpenMainDialog: (value: boolean) => void;

  } , ) => {

    const [userLocation,setUserLocation] = useState()
    const [address, setAddress] = useState("");
    
    useEffect(() => {
    axios.get(`http://localhost:8080/user/getUserLocationBasedOnıd/${user.id}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => {setUserLocation(res.data)}
  )
    .catch((err) => console.log(err));
  }, [user.id]);

    useEffect(() => {
    const fetchAddress = async () => {
      console.log(userLocation);
      const latitude = userLocation.latitude;
      const longitude = userLocation.longitude;

      try {
        const res = await fetch(
          `https://nominatim.openstreetmap.org/reverse?lat=${latitude}&lon=${longitude}&format=json`,
          {
            headers: {
              "User-Agent": "YourAppName/1.0 (your@email.com)",
            },
          }
        );

        const data = await res.json();
        setAddress(data.display_name);
      } catch (error) {
        console.error("Reverse geocoding failed:", error);
        setAddress("Konum alınamadı.");
      } 
    };

    fetchAddress();
  }, [user.id,userLocation]);
    console.log(address);

  const handleDelete = async (id:string) => {
        try {
          const user = await managerDeleteUserAction(id);
          setOpen(false)
          setOpenMainDialog(false)
          console.log(user);
        } catch (error) {
          console.log(error);
        }
  }
  const [deactivateOpen,setDeactivateOpen] = useState(false)
  const handleDeactivateUser = async (id: string) => {
        try {
          const userDeactivate = await managerDeactivateUser(id);
          setDeactivateOpen(false)
          console.log(userDeactivate);
        } catch (error) {
          console.log(error);
        }
  }
    return (
      <div className='relative  w-full h-full flex flex-col justify-start items-start gap-[5px] p-[10px]  '>
                      <h2 className={`w-fit h-fit  text-black text-[24px] leading-[24px]`}>Kullanıcı Raporu</h2>

    <div className="w-full  h-fit flex flex-col justify-start items-start">
     <div className=" w-full  px-[5px] flex flex-col py-[10px] gap-[10px] ">
          <div className=" w-full flex flex-row items-center  justify-between  gap-[10px]">
      
       <div  className=" w-full flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
  <h2 className="w-full text-[16px] text-black">Kullanıcı İsmi : </h2>
     {user.firstName ? (
  <h2 className="w-full text-black text-[14px]">
        {user.firstName}
    <Separator/>

    </h2>
) : (
  <div className="w-[100px] h-[100px] flex items-center justify-center bg-gray-100 text-sm text-gray-500">
İsim yok  </div>
)} 
       </div>

    
  </div>
          <div className=" w-full flex flex-row items-center  justify-between  gap-[10px]">
      
       <div  className=" w-full flex flex-row items-center  justify-between  gap-[10px] border-b-[1px]">
  <h2 className="w-full text-[16px] text-black">Kullanıcı Son Giriş Tarihi : </h2>
     {user.lastLoginTime ? (
  <h2 className="w-full text-black text-[14px]">
        {
        format(new Date(user.lastLoginTime), 'dd MMMM yyyy HH:mm')}
    <Separator/>

    </h2>
) : (
  <div className="w-[100px] h-[100px] flex items-center justify-center bg-gray-100 text-sm text-gray-500">
Tarih yok  </div>
)} 
       </div>

    
  </div>
          <div className=" flex flex-row  gap-[5px]">

              <h2 className="text-[16px] font-normal text-black   ">
              Kullanıcı son online tarihi:
              </h2>
              <h2 className="text-[13px] font-normal text-black   ">
        {user.lastOnlineTime ? (
  <h2 className="w-full text-black text-[14px]">
        {
        format(new Date(user.lastOnlineTime), 'dd MMMM yyyy HH:mm')}
    <Separator/>

    </h2>
) : (
  <div className="w-[100px] h-[100px] flex items-center justify-center bg-gray-100 text-sm text-gray-500">
Tarih yok  </div>
)} 
              </h2>
        </div>
    <div className=" w-full h-fit flex flex-row items-start  gap-[10px] border-b-[1px]">
  <h2 className="w-full  text-[14px] text-black">Kullanıcı Son Kaydedilen Konumu: </h2>
         <h2 className="w-full text-black  text-[14px]">
        {address}
    <Separator/>

    </h2> 
  </div>
    <div className=" w-full h-fit flex flex-row items-start  gap-[10px] border-b-[1px]">
  <h2 className="w-full  text-[14px] text-black">Kullanıcı Emaili: </h2>
         <h2 className="w-full text-black  text-[14px]">
        {user.email}
    <Separator/>

    </h2> 
  </div>

    <div className=" w-full h-fit flex flex-row items-start  gap-[10px] border-b-[1px]">
  <h2 className="w-full  text-[14px] text-black">Kullanıcı Rolü: </h2>
         <h2 className="w-full text-black  text-[14px]">
        {user.role}
    <Separator/>

    </h2> 
  </div>

<div className="w-full justify-end items-end h-fit flex gap-[10px] ">

<Dialog open ={deactivateOpen} onOpenChange={setDeactivateOpen}>
<DialogTrigger >
<div className=" flex justify-end items-center bg-blue-400 w-fit p-[10px] rounded-[15px]">
  <h2 className="text-white">

Kullanıcıyı Dondur 
  </h2>
</div>

</DialogTrigger>

<DialogContent className="bg-white  px-[10px] mr-[10px]">
  <div className="gap-[5px]">

  <h2 className=" text-[24px]">Kullanıcıyı dondurmaya emin misiniz? </h2>
  <h2 className=" text-[16px]">Kullanıcıyı dondurduğunuz zaman bu işlem birdaha geri alınamaz.</h2>
  </div>
  <Button variant={null} onClick={() => {handleDeactivateUser(user.id)} }  className=" flex justify-end items-center bg-blue-400 w-fit p-[10px] rounded-[15px]">
  <h2 className="text-white">

Kullanıcıyı Dondur  
  </h2>
</Button>
</DialogContent>
</Dialog>
<Dialog open ={open} onOpenChange={setOpen}>
<DialogTrigger >
<div className=" flex justify-end items-center bg-red-400 w-fit p-[10px] rounded-[15px]">
  <h2 className="text-white">

Kullanıcıyı Sil
  </h2>
</div>

</DialogTrigger>

<DialogContent className="bg-white">
  <div className="gap-[5px]">

  <h2 className=" text-[24px]">Kullanıcıyı silmeye emin misiniz? </h2>
  <h2 className=" text-[16px]">Kullanıcıyı sildiğiniz zaman bu işlem bidaha geri alınamaz.</h2>
  </div>
  <Button variant={null} onClick={() => {handleDelete(user.id)} }  className=" flex justify-end items-center bg-red-400 w-fit p-[10px] rounded-[15px]">
  <h2 className="text-white">

Kullanıcıyı Sil
  </h2>
</Button>
</DialogContent>
</Dialog>
</div>
     </div>


 
    </div>

      </div>

    )
  }

  export default UserSheetComponent