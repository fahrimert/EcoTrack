"use client"

import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Separator } from "@/components/ui/separator";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import {z} from "zod";
import { cn } from "@/lib/utils";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { format } from "date-fns";
import axios from "axios";
import * as React from "react"
import { Calendar as CalendarIcon } from "lucide-react"
import { Calendar } from "@/components/ui/Calendar "
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"
import { WorkerLocationContext } from "@/context/WorkerLocationContext";
import { SensorDestinationContext } from "@/context/SensorDestinationContext";
import { createTask } from "@/app/actions/sensorActions/createTask";
import { useFetchAllWorkers } from "@/hooks/useFetchAllWorkers";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { useFetchAllSuperVizors } from "@/hooks/useFetchAllSuperVizors";
import { useAllWorkerAndSupervizor } from "@/hooks/useAllWorkerAndSupervizor";

 
const formSchema = z.object({
  title:z.string().min(5,{message:"Duyuru Açıklaması en az 5 karakter olmalı"}),
  content:z.string().min(10,{message:"Duyurunun İçeriğini Seçiniz"}),
  receiverId: z.string().min(1,{message:"İşçi Veya Supervizor Seçiniz"}),
},
)
export type MakeAnnonucementFormValues = z.infer<typeof formSchema>
//güncelleme yapmıyon createleme yapıyon


const MakeAnnonucementForm= ({session} : {session: RequestCookie | undefined }) => {

  


  
  const [sensorListData,setSensorListData] = useState<Sensor[]>()
  const { userAndSupervizor,  error } = useAllWorkerAndSupervizor(session);

  
   useEffect(() => {
    axios.get(`http://localhost:8080/supervizor/getAllAvailableSensorsForAssigningTaskSelectComponent`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setSensorListData(res.data))
    .catch((err) => console.log(err));
  }, []);



          var today = new Date();
   
    const [loading,setLoading] = useState(false)
    const form = useForm<MakeAnnonucementFormValues>({
    resolver:zodResolver(formSchema),
    defaultValues:{
        title : "",
        content: "",
        receiverId : "",

    }
  });







  const onSubmit = async (data:MakeAnnonucementFormValues) => {
      try {  
        console.log(data);
/*         await createTask(data) */

        /* const returnData = await updateSensor(formData,initialData) */
        } catch (error) {
       console.log(error.message);
         
        }



  
  }
 

  return (
    <>

<div className="flex flex-col gap-[5px] w-full  rounded-[5px]">

    <div className="flex flex-col items-center justify-between gap-[10px]">

        
    </div>
    <Separator/>
    <div className=" w-full h-fit  flex flex-col p-[10px] rounded-[30px] gap-[5px]">

   <div className="w-full h-fit flex flex-row rounded-[30px]">
  <Form {...form} >
          <form
            onSubmit={form.handleSubmit(onSubmit)}
         
            className={cn(`space-y-8 w-full   flex flex-col  rounded-[30px]` )} 
          >
       
<div className="w-full h-fit flex flex-row gap-[5px]">
<div className={cn(` w-full h-full flex flex-col gap-8 bg-white  p-[20px] justify-center transition-all duration-300 rounded-[30px] `)} >


<div className = "flex flex-col  gap-[10px]">
<h3 className=" w-full h-fit text-[24px] font-semibold">Duyuru Yapın </h3>
    

                <FormField
                 control={form.control}
                 name = "title"
                 render = {({field}) => (
                    <FormItem>
                        <FormLabel>İşçiye Gönderilecek Duyurunun Başlığı</FormLabel>
                        <FormControl>
                            <Textarea disabled = {loading} placeholder="İşçiye Gönderilecek Duyurunun Başlığı" {...field}/>
                        </FormControl>
                        <FormMessage className="text-white bg-red-400  w-fit p-[5px] rounded-[3px]" />
                    </FormItem>
)}
/>
                <FormField
                 control={form.control}
                 name = "content"
                 render = {({field}) => (
                    <FormItem>
                        <FormLabel>İşçiye Gönderilecek Duyurunun İçeriği</FormLabel>
                        <FormControl>
                            <Textarea disabled = {loading} placeholder="İşçiye Gönderilecek Duyurunun İçeriği" {...field}/>
                        </FormControl>
                        <FormMessage className="text-white bg-red-400  w-fit p-[5px] rounded-[3px]" />
                    </FormItem>
)}
/>

<FormField
            control={form.control}
            name="receiverId"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-black">Duyuru Yapacağınız İşçi Veya Supervizorü Seçiniz</FormLabel>
                <Select
                  disabled={loading}
                  onValueChange={
                    field.onChange
                  }
                  value={field.value}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue
                        defaultValue={field.value}  
                        className="w-fit bg-black"
                        placeholder="İşçi Veya Supervizor Seçiniz"
                      ></SelectValue>
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent className="bg-[#edecea]">
                    {userAndSupervizor.map((users,b) => (
                      <SelectItem key={b} value={users.id} className="text-black" >
                        {users.firstName}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                <FormMessage className="text-white bg-red-400  w-fit p-[5px] rounded-[3px]" />
              </FormItem>
            )}
          />



     
              




</div>

</div>



</div>

              
              <Button disabled={loading} variant={null} className="ml-auto text-black bg-white rounded-[15px] " type="submit" >
                Duyuru Yap
              </Button>
          </form>
        </Form>
   
 </div>

        
      
      </div>
</div>

            </>
  )
}

export default MakeAnnonucementForm