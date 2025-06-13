"use client"

import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Separator } from "@/components/ui/separator";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z} from "zod";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { cn } from "@/lib/utils";
import { ScrollArea } from "@/components/ui/scroll-area";
import Heading from "./Heading";
import Image from "next/image";
import { Textarea } from "@/components/ui/textarea";
import { finishTask } from "@/app/actions/taskActions/finishTask";
import { TaskSensorWithTask } from "../../../components/SensorComponents/SensorsAndMap";




export interface TaskDetail {
  id: number;
  taskSensors: {
    id: number;
    sensorName: string;
    status: string;
    color_code: string;
    latitude: number;
    longitude: number;
    currentSensorSession: {
      id: number;
      sensorName: string;
      displayName: string;
      color_code: string;
      note: string | null;
      startTime: string; // ISO date format
      completedTime: string | null;
      latitude: number;
      longitude: number;
    };
  };
  superVizorDescription: string;
  superVizorDeadline: string; // ISO date format
  assignedBy: {
    id: number;
    firstName: string;
    surName: string;
  };
  workerArriving: boolean;
  workerArrived: null | boolean; // JSON'da "workerArrived" ama burada "workerArrived" yazım hatası olabilir
  workerNote: string | null;
  solvingNote: string | null;
  taskImages: any[]; // Daha spesifik bir tip verebilirsiniz
  taskCompletedTime: string | null; // ISO date format
}
const formSchema = z.object({
  solvingNote:z.string().min(1,{
      message:"Not must be at least 1 character"
  }),
  statusId: z.string().min(2),
  files: z.any() // Dosyalar için validasyon
},
)
export type AssignedSensorFormForSolvingValues = z.infer<typeof formSchema>


const AssignedTaskFormForSolving= ({initialData,stasusesData} : {initialData: TaskDetail
  ,stasusesData : [ 'ACTIVE', 'FAULTY', 'IN_REPAIR', 'SOLVED' ]}) => {
    const [loading,setLoading] = useState(false)
    const form = useForm<AssignedSensorFormForSolvingValues>({
    resolver:zodResolver(formSchema),
    defaultValues:initialData ?
initialData
    : {
        solvingNote : "",
        statusId:""


    }
  });

  console.log(stasusesData);
  const onSubmit = async (data:AssignedSensorFormForSolvingValues) => {
    try{
      const formData = new FormData();
    
      formData.append('solvingNote', data.solvingNote);

      if (data.files) {
        for (let i = 0; i < data.files.length; i++) {
          formData.append('files', data.files[i]);
        }
      }
      try {  
      formData.append('statusID', data.statusId);

        finishTask(formData,initialData)
        console.log(data);
        } catch (error) {
       console.log(error.message);
         
        }

    }

   catch (error) {
      console.log(error);
    }

  
  }
/* burda ise eğer worker arriving olmamışsa yeterli çünkü workerarriving olmamışsa worker arrived da olamaz  */
  return (
    <>
     <ScrollArea className="h-screen">
      {!initialData.workerArriving   &&
<div className=" w-full h-full absolute inset-0 z-10 flex items-center justify-center bg-black text-lg font-bold text-white bg-opacity-50 ">
    Yoldayım Notu Girilmeden Göreve Gidiş Yeri Kullanılamaz.

  </div>
    }
<div className={cn(`flex flex-col gap-[5px] w-full  rounded-[5px] ${!initialData.workerArriving && " blur-sm" }`)}>
 
    <div className="flex flex-col items-center justify-between gap-[10px]  px-[10px]">
        <Heading
        title={initialData.taskSensors.sensorName}
        description = {initialData.superVizorDeadline}
        taskSender={initialData.assignedBy.firstName}
        superVizorDescription = {initialData.superVizorDescription}
         
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
                 name = "solvingNote"
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
            /* bunu category componentından aldık  */
            control={form.control}
            name="statusId"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-black">Taskın Statusunu  Güncelleyin </FormLabel>
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
                    {stasusesData.map((status,b) => (
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


  
     
              




</div>

</div>



</div>

              
              <Button disabled={loading} variant={null} className="ml-auto text-black bg-white rounded-[15px] " type="submit" >
                Taskı Onarım Notunu Gir
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

export default AssignedTaskFormForSolving