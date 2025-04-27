"use client"
import React, { useContext } from 'react'
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Separator } from "@/components/ui/separator";
import { zodResolver } from "@hookform/resolvers/zod";
import { redirect, useParams, useRouter } from "next/navigation";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { string, z } from "zod";
import { Toaster } from 'react-hot-toast';
import Autocomplete, { usePlacesWidget } from "react-google-autocomplete";
import { createLocation } from '../actions/userActions/createUserLocation';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import { SourceContext } from '@/context/SourceContext';
import { DestinationContext } from '@/context/DestinationContext';
import { Wrapper } from '@googlemaps/react-wrapper';
import  { useEffect, useRef } from 'react';

import {
  APIProvider,
  ControlPosition,
  MapControl,
  AdvancedMarker,
  Map,
  useMap,
  useMapsLibrary,
  useAdvancedMarkerRef,
  AdvancedMarkerRef
} from '@vis.gl/react-google-maps';

const createLocationFormSchema = z.object({
  name: z.string().min(3, {
    message: "Konum  en az 3 karakter olmalıdır.",
      }),
    placeId: z.string().optional(), // Google Places placeId'si
    lat: z.number().optional(), // Enlem
    lng: z.number().optional(), // Boylam


});

export type LocationFormValue = z.infer<typeof createLocationFormSchema>;

const InputItem = ({session } : {session:RequestCookie}) => {
  const [loading, setLoading] = useState(false);
  const [value, setValue] = useState(null)
  const {source,setSource} = useContext(SourceContext)
  const form = useForm<LocationFormValue>({
    resolver: zodResolver(createLocationFormSchema),
    defaultValues: 
       {
        name: "",
        placeId: "",
        },
  });



  const onSubmit = async (data: LocationFormValue) => {
    try {
      createLocation(data.lat!,data.lng!,session)
    } catch (error) {
      console.log(error);
    }
  };
  return (
<Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full  h-fit p-[15px] rounded-[10px]  shadow-lg border-[1px] "
        >
          <div className="w-fit h-fit flex flex-col  gap-[20px]  p-[10px] ">
      

<FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Adres</FormLabel>
                <FormControl>
                  
                  <Autocomplete
                   apiKey="AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E"
                  
                                  onPlaceSelected={(place) => {
                      form.setValue("name", place.formatted_address || "");
                      form.setValue("placeId", place.place_id || "");
                      
                      if (place.geometry?.location) {
                        form.setValue("lat", place.geometry.location.lat());
                        form.setValue("lng", place.geometry.location.lng());
                        setSource({
                          lat:place.geometry.location.lat(),
                          lng:place.geometry.location.lng()
                        })
                      }
                    }}
                    options={{
                      componentRestrictions: { country: "tr" }, 
                    }}
                    className="shadow-lg border-b-[1px] border-r-0 border-t-0 border-l-0 "
                    {...field}
                  />

                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />


         
          </div>

 


          <Button  variant={null}   disabled={loading} className=" w-fit border-[2px]  my-[10px] rounded-xl  text-white h-[35px] bg-black" type="submit">
            Yarat
          </Button>
        </form>
        <Toaster />

      </Form>
  )
}

export default InputItem