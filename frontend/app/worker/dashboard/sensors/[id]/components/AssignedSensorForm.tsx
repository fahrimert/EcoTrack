"use client"

import { Button } from "@/components/ui/button";
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Separator } from "@/components/ui/separator";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect, useState } from "react";
import { useFieldArray, useForm } from "react-hook-form";
import {string, z} from "zod";

import { cn } from "@/lib/utils";
import { ScrollArea } from "@/components/ui/scroll-area";
import Heading from "./Heading";
import axios from "axios";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import Image from "next/image";
import { Textarea } from "@/components/ui/textarea";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { updateSensor } from "@/app/actions/sensorActions/updateSensor";
import toast from "react-hot-toast";
import { SensorDataDifferentOne } from "@/app/supervisor/superVizorDataTypes/types";

//not eklicez
//durumunu görmüş olucaz haritasını falan görücez 
//bitirme zamanını kaydede tıklayınca zaten halleder 
//foto eklemeyi şimdi yapmicaz 
//statusunu selectle değiştirsek kaydedince o durumu kaydetse olur aslında completed timeyi de kaydedince ayarlamış oluruz 



const formSchema = z.object({
  not:z.string().min(1,{
      message:"Not must be at least 1 character"
  }),
  statusId: z.string().min(2),
  files: z.any() // Dosyalar için validasyon
},
)
export type AssignedSensorFormValues = z.infer<typeof formSchema>


const AssignedSensorForm= ({initialData,statuses,session} : {session:RequestCookie,initialData: SensorDataDifferentOne, statuses :  [ 'ACTIVE', 'FAULTY', 'IN_REPAIR', 'SOLVED' ] }) => {
    const [loading,setLoading] = useState(false)
    const form = useForm<AssignedSensorFormValues>({
    resolver:zodResolver(formSchema),
    defaultValues:initialData ?
initialData
    : {
        not : "",
        statusId:""

    }
  });

  
  const onSubmit = async (data:AssignedSensorFormValues) => {
    try{
      const formData = new FormData();
    
      formData.append('note', data.not);
      formData.append('statusID', data.statusId);
      if (data.files) {
        for (let i = 0; i < data.files.length; i++) {
          formData.append('files', data.files[i]);
        }
      }
      try {  
        const returnData = await updateSensor(formData,initialData)
        toast.success(returnData.serverData)
        } catch (error) {
       console.log(error.message);
         
        }

    }

   catch (error) {
      console.log(error);
    }

  
  }


  return (
    <>
  <ScrollArea className="h-screen">

<div className="flex flex-col gap-[5px] w-full bg-[#050505] rounded-[5px]">

    <div className="flex flex-col items-center justify-between gap-[10px]">
        <Heading
        title={initialData.data.sensorName}
        description = {initialData.data.currentSensorSession.startTime}
        />
        
    </div>
    <Separator/>
    <div className=" w-full h-fit  flex flex-col p-[10px] rounded-[30px] gap-[5px]">
    <div className=" w-full h-fit flex flex-col justify-start items-start gap-[20px] p-[20px] bg-white rounded-[30px]">
{/*         <h2 className={`w-full h-[40px]  text-black text-[24px] leading-[24px]`}>{ilans?.user.name}</h2> */}
        <div className=" w-full h-fit flex flex-row justify-start items-start gap-[10px] p-0 bg-white">
  <Image
              src={"/indir.jpg"}
              alt="232"
             
              className={cn(` w-[200px] h-[100px]  object-fit  rounded-[30px] cursor-pointer  `)}
              width={100} 
              height={100}
            />    
        </div>
        <div className=" relative  w-full h-fit flex flex-col justify-start items-start gap-[5px]  p-[10px]  bg-white">
                    <h2 className={`w-fit h-[32px]  text-black text-[24] leading-[19.2px] flex items-center justify-center`}>Arıza Nedeni 
</h2>
                    <h2 className={`w-fit h-fit text-black text-[16px] leading-[19.2px] flex items-center justify-center`}> 
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus dignissimos maiores odit ullam officia explicabo, laboriosam alias ipsa qui. Sed eius maxime quam excepturi architecto voluptatem cupiditate asperiores reprehenderit animi vero quibusdam voluptate fuga ullam, dolor natus eos nam veniam modi! Exercitationem blanditiis aperiam incidunt, distinctio, qui inventore ad sapiente vero quaerat laudantium modi libero maiores consequatur unde dolore explicabo! Amet optio maxime quia beatae, quis iusto eos itaque laborum similique natus sapiente aut voluptate mollitia, unde, cupiditate tempora. Rerum dolor excepturi praesentium ipsum, tempora magnam distinctio hic numquam omnis nostrum tempore? Suscipit reprehenderit hic eius aliquid excepturi odio corporis?

</h2>

                        </div>

   
  
  
    </div>
   <div className="w-full h-fit flex flex-row rounded-[30px]">

  <Form {...form} >
          <form
            onSubmit={form.handleSubmit(onSubmit)}
         
            className={cn(`space-y-8 w-full   flex flex-col  rounded-[30px]` )} 
          >
       
<div className="w-full h-fit flex flex-row gap-[5px]">
<div className={cn(` w-full h-full flex flex-col gap-8 bg-white  p-[20px] justify-center transition-all duration-300 rounded-[30px] `)} >


<div className = "flex flex-col  gap-[10px]">
<h3 className=" w-full h-fit text-[24px] font-semibold">Genel Özellikler</h3>
    

                <FormField
                 control={form.control}
                 name = "not"
                 render = {({field}) => (
                    <FormItem>
                        <FormLabel>Ürün Arızası Hakkında Not</FormLabel>
                        <FormControl>
                            <Textarea disabled = {loading} placeholder="Arıza ile alakalı not ekleyin" {...field}/>
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
)}
/>
<FormField
  control={form.control}
  name="files"
  render={({ field }) => (
    <FormItem>
      <FormLabel>Resim Ekle</FormLabel>
      <FormControl>
        <Input 
          type="file" 
          multiple 
          onChange={(e) => field.onChange(e.target.files)} 
        />
      </FormControl>
      <FormMessage/>
    </FormItem>
  )}
/>
<FormField
            /* bunu category componentından aldık  */
            control={form.control}
            name="statusId"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-black">Sensörun Statüsünü Güncelleyin </FormLabel>
                <Select
                  disabled={loading}
                  onValueChange={field.onChange}
                  value={field.value}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue
                        defaultValue={field.value}
                        className="w-fit bg-black"
                        placeholder="Sensörün Durumunu Güncelleyiniz"
                      ></SelectValue>
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent className="bg-[#edecea]">
                    {statuses.map((status,b) => (
                      <SelectItem key={b} value={status} className="text-black" >
                        {status}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />

  
     
              




</div>

</div>



</div>

              
              <Button disabled={loading} variant={null} className="ml-auto text-black bg-white rounded-[15px] " type="submit" >
                Güncelle
              </Button>
          </form>
        </Form>
   
 </div>

        
      
      </div>
</div>
</ScrollArea>

    {/* bu form mevzusu da docda yazıyor böyle  */}

    
            </>
  )
}

export default AssignedSensorForm