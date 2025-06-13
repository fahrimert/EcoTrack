  "use client"
  import {
      Dialog,
      DialogContent,
      DialogDescription,
      DialogHeader,
      DialogTrigger,
    } from "@/components/ui/dialog"

    import {
      Carousel,
      CarouselContent,
      CarouselItem,
      CarouselNext,
      CarouselPrevious,
    } from "@/components/ui/carousel"
  
  import { Button } from "@/components/ui/button";
  import Image from "next/image";
  import { AddressComponent } from "@googlemaps/google-maps-services-js";
  import { format } from "date-fns";
  import { Separator } from "@/components/ui/separator";
import { ImageResponseDTO } from "@/app/supervisor/superVizorDataTypes/types";
import { useUserProfile } from "@/hooks/useUserProfile";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";

  const LeftStack = ({initialData,addressComponents,session} : {initialData :  {data:{
    id: number,
    sensorName: string,
    displayName: string,
    color_code: string,
    note: string,
      finalStatus: string,

    startTime: string,
    completedTime: string,
    latitude: number,
    longitude: number,
  sensorIconImage: ImageResponseDTO
    imageResponseDTO: ImageResponseDTO[]
    
  }},

  addressComponents: AddressComponent[]
  session: RequestCookie | undefined
  } , ) => {

  const { userProfile, loading, error } = useUserProfile(session);

    return (
      <div className='relative  w-full h-fit flex flex-col justify-center items-start gap-[5px]  '>
          <div className=" relative  w-full h-[400px] flex flex-row justify-center items-center gap-[10px] p-[20px] bg-white ">
              {  
                  <div className="relative  w-[60%] h-full  bg-blue-200 ">
                    {initialData.data.sensorIconImage.base64Image !== null &&  <Image 
  src={`data:image/png;base64,${initialData.data.sensorIconImage.base64Image   }`}
  fill
  alt="Sensor Image"
  objectFit="true"
  />}
 
  {/* buraya rastgele static bir image koyup alttaki yere koyacağımız imageler önemli zaten  */}
                  </div>}
                
            
          </div>
          
          <div  className='relative  w-full h-fit flex flex-row justify-center items-center gap-[10px] '>
              <div className='relative  w-full h-full flex flex-col justify-start items-start gap-[10px] p-[20px]   rounded-[30px] bg-white'>
      
              <div className='relative  w-full h-fit flex flex-col  justify-start items-start  bg-white '>
                        <div className=" relative  w-full h-fit flex flex-col justify-start items-start gap-[10px]  p-[10px]  bg-white  rounded-[30px]">
                        <h2 className={`w-fit h-fit  text-black text-[32px] leading-[24px]`}>Genel Detaylar</h2>
  <Separator/>
  <div className=" w-fit flex flex-row items-center  gap-[10px]">
  <h2 className="text-[18px]">Sensör Konum: </h2>
      <h2>
  {  addressComponents.reverse()[2].short_name + "/" +  addressComponents.reverse()[2].short_name}
    </h2>
  </div>
  <div className=" w-fit flex flex-row items-center  gap-[10px]">
  <h2 className="text-[18px]">İsmi : </h2>
      <h2>
        {initialData.data.sensorName}
    </h2>
  </div>



    

  
    
                          </div>
                          
                        <div className=" relative  w-full h-fit flex flex-col justify-start items-start gap-[10px]  px-[10px]  bg-white">
                        <div className=" w-fit flex flex-row items-center  gap-[10px]">
  <h2 className="text-[18px]">Bırakılmış Durumu : </h2>
      <h2>
        {initialData.data.finalStatus}
    </h2>
  </div>
  <div className=" w-fit flex flex-row items-center  gap-[10px]">

                        <h2 className="text-[18px]">
    Sensore Ait ID  : 
    </h2>
    
                        <h2 className="text-[18px]">
                        {initialData.data.id}
    </h2>
  </div>
    
    

    
                        </div>
                        
                    
                  
                      </div>
              </div>
              <div className='relative  w-full h-fit flex flex-col justify-start items-start gap-[10px] p-[20px] rounded-[30px] bg-white'>
              <div className=" relative  w-full h-fit flex flex-col justify-start items-start gap-[10px]  p-[10px]  bg-white  rounded-[30px]">
                      <h2 className={`w-fit h-fit  text-black text-[32px] leading-[24px]`}>İşçi Detayları:</h2>
                      <Separator/>
                          
                    <div className=" relative  w-full h-fit flex flex-col justify-start items-start gap-[10px]  bg-white">
                        <div className=" w-full  h-fit flex flex-row">
                        <div className=" w-fit flex flex-col items-start  gap-[10px]">
  <div className="flex flex-row w-fit items-center justify-center">

  <h2 className="text-[18px]">İşçi İsmi Durumu : </h2>
      <h2>
        {userProfile?.firstName}
    </h2>
    </div>
    
                        <h2 className="text-[18px]">
    Başlangıç Tarihi : 
    
    {format(new Date(initialData.data.startTime), 'dd MMMM yyyy HH:mm')}
    </h2>
    
    
    <h2  className="text-[18px]"> 
    Bitiş Tarihi :                           {format(new Date(initialData.data.completedTime), 'dd MMMM yyyy HH:mm')}

    </h2>
  
  <div className=" w-fit flex flex-row">
  <h2  className="text-[18px]">Not : </h2>
      <h2  className="text-[18px]">
        {initialData.data.note}
    </h2>
  </div>
    </div>


  </div>
    

    
                        </div>
                        
                  
                  
                      </div>
              </div>
          </div>
          <div  className='relative  w-full h-fit flex flex-col justify-center items-center  gap-[5px] '>
          
              <div className='relative  w-full h-fit flex flex-col justify-start items-start gap-[10px] p-[20px] rounded-[30px] bg-white'>
              <div className='relative  w-full h-fit flex flex-col justify-start items-start gap-[10px]   bg-white'>
                      <h2 className={`w-fit h-fit  text-black text-[32px] leading-[24px]`}>Sensör Fotoğrafları:</h2>
                          
                      <div className=" relative  w-fit h-full flex flex-row  gap-[10px] p-[10px]">
          
          <div className="relative  w-full h-full    j z-0 ">
              <div className=" w-full h-full flex  justify-center items-center z-10  bg-[##4F4F4F]">
  <Dialog>
  <DialogTrigger className="text-black">
  <Button className=" rounded-[30px] p-[20px]">

  {initialData.data.imageResponseDTO.length} tane  fotoğraf var   </Button> 
  </DialogTrigger>
  <DialogContent>
  <DialogHeader>
  <DialogDescription>
    <Carousel>
      <CarouselContent>
  {
  initialData.data.imageResponseDTO.map((d,g) =>
                          <CarouselItem key={g}>
                      <div className="relative  w-[400px] h-[400px]  bg-blue rounded-[20px] bg-blue-200">
  <Image 
  layout="fill"
  src={`data:image/png;base64,${d.base64Image}`}

  alt="212"
  />                                     

  <CarouselPrevious />
  <CarouselNext />
  </div>
  </CarouselItem>
                  )
                } 
                </CarouselContent>
  </Carousel>
  </DialogDescription>
  </DialogHeader>
  </DialogContent>
  </Dialog>
              </div>
          </div>
          </div>
                        
                  
                  
                      </div>
              </div>
          </div>
      




      

      </div>

    )
  }

  export default LeftStack