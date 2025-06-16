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
import Autocomplete from "react-google-autocomplete"; 

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Button } from '@/components/ui/button';
import { createSensorManagerLocation } from '@/app/actions/sensorActions/createSensorManagerLocation'
import toast from 'react-hot-toast'
import { SensorListForManagerUse } from './SensorManagementSensorsWrapperComponent'
import { ScrollArea } from '@/components/ui/scroll-area'
const formSchema = z.object({
 sensor_location: z.string().min(3, {
    message: "Konum  en az 3 karakter olmalıdır.",
  }),
  sensorId: z.string().optional(),
  placeId: z.string().optional(),
    lat: z.number().optional(),
  lng: z.number().optional(),
},
)


export type AddSensorFormValues = z.infer<typeof formSchema>



const ManagerSensorAddLocationComponent = ({sensorListData,onSuccess} : {sensorListData: SensorListForManagerUse[] | undefined, onSuccess: () => void }) => {


        const form = useForm<AddSensorFormValues>({
        resolver:zodResolver(formSchema),
        defaultValues:{
            sensor_location:"",
          placeId: "",
    lat: 0,
    lng:0,
        }
      });


        const [locationInput, setLocationInput] = useState("");

        const [sensorId,setSensorId] = useState("")
        const onSubmit = async (data: AddSensorFormValues) => {
          try {
          
            console.log(data);
            console.log(data.lat);
      const returnData =await createSensorManagerLocation(data);
          console.log(returnData);
      if (returnData.serverData != null) {
               onSuccess()
         toast.success(returnData.serverData);
              
            } 
           if (returnData.serverError  != null) {
            toast.error(returnData.serverError)
           } 
            }  catch (error) {
            console.log(error);
          }
        };
const [isScriptLoaded, setIsScriptLoaded] = useState(false);

     useEffect(() => {
  if (typeof window !== "undefined" && !window.google) {
    const script = document.createElement("script");
    script.src = `https://maps.googleapis.com/maps/api/js?key=AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E&libraries=places`;
    script.async = true;
    script.onload = () => {
      console.log("Google Maps script loaded");
      setIsScriptLoaded(true);
    };
    document.head.appendChild(script);
  } else {
    setIsScriptLoaded(true); 
  }
}, []);
  return (
       <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="w-full p-[10px] border-[1px] gap-[20px] flex flex-col"
      >
        <div className="w-fit h-fit flex flex-col  gap-[20px]  p-[10px]  bg-white">

                 <FormField
                control={form.control}
                name = "sensorId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Resim Ekle</FormLabel>
                    <FormControl>
                      <Select  
                          onValueChange={(e) => {
            field.onChange(e) 
            setSensorId(e) 
          }}
          value={field.value?.toString()}
                     >
      <h2 className=" text-black mb-[10px]">Sensor Seçiniz</h2>
      <SelectTrigger className="text-black">
        <SelectValue  placeholder={`Sensor Status Filtresi seçiniz`} />
      </SelectTrigger>

      <SelectContent className="bg-white">
                      <ScrollArea className='h-[400px] '>

     {sensorListData?.map((sensor) => (
  <SelectItem key={sensor.id} value={sensor.id.toString()} className="text-black">
    {`${sensor.sensorName} - Lat: ${sensor.latitude}, Lng: ${sensor.longitude}`}
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





            <FormControl>
    {isScriptLoaded && (
    <Autocomplete
      apiKey="AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E"
      onPlaceSelected={(place) => {
        console.log(place);
        const address = place.formatted_address || "";
        setLocationInput(address);
        form.setValue("sensor_location", address);
        form.setValue("placeId", place.place_id || "");
        if (place.geometry?.location) {
          form.setValue("lat", place.geometry.location.lat());
          form.setValue("lng", place.geometry.location.lng());
        }
      }}
      options={{
        componentRestrictions: { country: "tr" },
      }}
      defaultValue={locationInput}
      className="h-[50px] w-full box-border text-sm font-medium text-black bg-white border-[2px] shadow-md"
      {...form}
 />
)}


            </FormControl>
     

          <Button
            variant={null}
            type="submit"
            className="h-[50px] w-fit rounded-[20px] box-border text-sm font-medium text-black bg-white border-[2px] shadow-md"
          >
            Ekleyin
          </Button>
        </div>
      </form>
    </Form>

                )

}

export default ManagerSensorAddLocationComponent