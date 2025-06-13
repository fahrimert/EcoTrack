"use client"
import { zodResolver } from '@hookform/resolvers/zod'
import React from 'react'
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
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Button } from '@/components/ui/button';
import { createSensorManager } from '@/app/actions/sensorActions/createSensorManager';
import toast from 'react-hot-toast'
const formSchema = z.object({
  sensor_name:z.string().min(5,{message:"Sensör İsmi en az 5 karakter olmalı "}),
  sensor_icon_image: z.any().optional() ,
},
)


export type AddSensorFormValues = z.infer<typeof formSchema>



const ManagerSensorAddComponent = ({onSuccess} : { onSuccess: () => void }) => {

        const form = useForm<AddSensorFormValues>({
        resolver:zodResolver(formSchema),
        defaultValues:{
            sensor_name : "",
    sensor_icon_image:""
        }
      });

      
        const onSubmit = async (data: AddSensorFormValues) => {
          try {

            const formData = new FormData();


      formData.append('sensorName', data.sensor_name);
    

      if (data. sensor_icon_image) {
        for (let i = 0; i < data.sensor_icon_image.length; i++) {
          formData.append('files', data.sensor_icon_image[i]);
        }
      }
             const returnData = await createSensorManager(formData);
             console.log(returnData);
       if (returnData.serverData != null) {
               onSuccess()
         toast.success(returnData.serverData);
              
            } 
           if (returnData.serverError  != null) {
            toast.error(returnData.serverError)
           }

         } catch (error) {
            console.log(error);
            toast.error(error);
          }
        };
  return <>

   <div className="w-full h-fit flex flex-row rounded-[30px] ">

    
      
        <Form {...form}>
   
          <form onSubmit={form.handleSubmit(onSubmit)} className="w-full p-[10px] border-[1px] gap-[20px] flex flex-col">
              <FormField
                control={form.control}
                name="sensor_name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Sensör İsmi</FormLabel>
                    <FormControl>
                      <Textarea placeholder="Sensör İsmini Ekleyin" {...field}/>
                    </FormControl>
                    <FormMessage/>
                  </FormItem>
                )}
              />
              
     {/*        <FormField
            control={form.control}
            name="sensor_location"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-black">Adres</FormLabel>
                <FormControl>
                     <Autocomplete
                                    apiKey="AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E"
                                    onPlaceSelected={(place) => {
                                      form.setValue("sensor_location", place.formatted_address || "");
                                      form.setValue("placeId", place.place_id || "");
                
                                      if (place.geometry?.location) {
                                        form.setValue("lat", place.geometry.location.lat());
                                        form.setValue("lng", place.geometry.location.lng());
                                      }
                                    }}
                                    options={{
                                      componentRestrictions: { country: "tr" },
                                    }}
                                    className="  h-[50px] w-full box-border text-sm font-medium   text-black bg-white border-[2px] shadow-md  "
                                    {...field}
                                  >
                    <Input
                      {...field}
value={form.watch("sensor_location")}
     onChange={field.onChange}           

                      placeholder="Adresinizi girin"
                      className="h-[50px] w-full box-border text-sm font-medium text-black bg-white border-[2px] shadow-md"
                    />
                                  </Autocomplete>

                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          /> */}
              <FormField
                control={form.control}
                name="sensor_icon_image"
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
              
           <Button
                variant={null}
                type="submit"
                className="h-[50px] w-fit rounded-[20px] box-border text-sm font-medium text-black bg-white border-[2px] shadow-md"
              >
                Ekleyin
              </Button> 
          </form>
        </Form>

   </div>
  </>

}

export default ManagerSensorAddComponent