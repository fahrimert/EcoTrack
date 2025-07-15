"use client"
import { zodResolver } from '@hookform/resolvers/zod'
import React, { useEffect, useState } from 'react'
import {  useForm } from 'react-hook-form'
import { z } from 'zod'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Button } from '@/components/ui/button';
import toast from 'react-hot-toast'
import { ScrollArea } from '@/components/ui/scroll-area'
import { updateSensorManager } from '@/app/actions/sensorActions/updateSensorForManager'
import Image from 'next/image'
import { supervizorCreatePdfSensor } from '@/app/actions/userActions/supervizorCreatePdfSensor'
import { useUserProfile } from '@/hooks/useUserProfile'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import { hooksUserTypes } from '@/app/sharedTypes'
import axios from 'axios'

const formSchema = z.object({
  managerId: z.string().optional(),

},
)


export type SendToManagerFormValues = z.infer<typeof formSchema>



const SendToManagerComponent = ({data,session,onSuccess} : {data: {
    data: {
        id: number;
        sensorName: string;
        displayName: string;
        color_code: string;
        note: string;
        status: string;
        startTime: string;
        completedTime: string;
        latitude: number;
        longitude: number;
        imageResponseDTO: {
            name: string;
            type: string;
            base64Image: string;
        }[];
    };
} | undefined,session: RequestCookie | undefined ,  onSuccess: () => void }) => {


  console.log(data?.data.id);
        const form = useForm<SendToManagerFormValues>({
        resolver:zodResolver(formSchema),
        defaultValues:{
    managerId : "",
        }
      });

        
  const { userProfile, loading, error } = useUserProfile(session);
   const [managerData,setManagerData] = useState<hooksUserTypes[]>()
  
      useEffect(() => {
        axios.get(`http://localhost:8080/supervizor/getAllManager`, {
          headers: { Authorization: `Bearer ${session?.value}` },
          withCredentials: true,
        })
        .then((res) => {setManagerData(res.data)}
      )
        .catch((err) => console.log(err));
      }, []); 

      console.log(managerData);
       const onSubmit = async ( managerId: SendToManagerFormValues) => {
        try {
           const formData = new FormData();


      formData.append('originalSensorId',String(data?.data.id))
      formData.append('sensorName',data?.data.sensorName!)
      formData.append('note',data?.data.note!)
      formData.append('startTime',data?.data.startTime!)
      formData.append('completedTime',data?.data.completedTime!)
      formData.append('latitude', String(data?.data.latitude!))
      formData.append('longitude',String(data?.data.longitude!))
   formData.append('managerId', managerId.managerId!);
      formData.append('supervizorId', String(userProfile?.id));

   console.log(formData);

   
     const returnData = await supervizorCreatePdfSensor(formData,session);

        if (returnData.serverData != null) {
               onSuccess()
         toast.success(returnData.serverData);
              
            } 
           if (returnData.serverError  != null) {
            toast.error(returnData.serverError)
           }
          console.log(returnData);

        } catch (error) {
          console.log(error);
            toast.error(error);

        }
  }
     
        
  return <>

   <div className="w-full h-fit flex flex-row rounded-[30px] ">

    
      
        <Form {...form}>
   
          <form onSubmit={form.handleSubmit(onSubmit)} className="w-full p-[10px] border-[1px] gap-[20px] flex flex-col">
             
                <FormField
                control={form.control}
                name = "managerId"
                render={({ field }) => (
                  <FormItem>
                    <FormControl>
                      <Select  
                          onValueChange={(e) => {
            field.onChange(e) 
            console.log(e);
          }}
          value={field.value?.toString()}
                     >
      <h2 className=" text-black mb-[10px]">Gönderilecek Müdürü Seçiniz</h2>
      <SelectTrigger className="text-black">
        <SelectValue  placeholder={`Sensor Seçiniz`} />
      </SelectTrigger>

      <SelectContent className="bg-white">
                      <ScrollArea className='h-[400px] '>

     {managerData?.map((a) => (
  <SelectItem key={a.id} value={a.id.toString()} className="text-black">
    {`${a.firstName} `}
  </SelectItem>
))}
                      </ScrollArea>

      </SelectContent>

    </Select>

                    </FormControl>
                    <FormMessage/>
                  </FormItem>
                )}
              />


           <Button
                variant={null}
                type="submit"
                className="h-[50px] w-fit rounded-[20px] box-border text-sm font-medium text-black bg-white border-[2px] shadow-md"
              >
                Müdüre Gönderin
              </Button> 
          </form>
        </Form>

   </div>
  </>

}

export default SendToManagerComponent