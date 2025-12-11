"use client";
import React, { startTransition, useContext, useEffect, useTransition } from "react";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
} from "@/components/ui/form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import toast, { Toaster } from "react-hot-toast";
import Autocomplete from "react-google-autocomplete";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { SourceContext } from "@/context/SourceContext";
import { Loader2 } from "lucide-react";
import { createWorkerLocationAction } from "@/app/actions/userActions/createUserLocation";
import { cn } from "@/lib/utils";
const createLocationFormSchema = z.object({
name: z.string().min(3, { message: "Konum en az 3 karakter olmalıdır." }),
  placeId: z.string().optional(),
  lat: z.number({ required_error: "Lütfen listeden bir konum seçin." }), 
  lng: z.number({ required_error: "Lütfen listeden bir konum seçin." }),
});

export type LocationFormValue = z.infer<typeof createLocationFormSchema>;

const InputItem = () => {
  const [loading, setLoading] = useState(false);
  const { source, setSource } = useContext(SourceContext);
  const [isPending, startTransition] = useTransition();


  const form = useForm<LocationFormValue>({
    resolver: zodResolver(createLocationFormSchema),
    defaultValues: {
      name: "",
      placeId: "",
    },
  });

  
const onSubmit = (data: LocationFormValue) => {

  console.log("Form gönderiliyor:", data); // Debug için
  startTransition(async () => {
      const result = await createWorkerLocationAction(data.lat, data.lng);
      
      if (result.success) {
        toast.success(result.message);
        setSource({ lat: data.lat, lng: data.lng });
      } else {
        toast.error(result.message);
      }
    });
  };

  

  useEffect(() => {
  if (!window.google) {
    const script = document.createElement("script");
    script.src = `https://maps.googleapis.com/maps/api/js?key=AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E&libraries=places`;
    script.async = true;
    document.head.appendChild(script);
    script.onload = () => {
      console.log("Google Maps script loaded");
    };
  }
}, []);

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="w-full  h-fit p-[10px]   border-[1px]  "
      >
        <div className="w-fit h-fit flex flex-col  gap-[20px]  p-[10px]  bg-white">
    
                <FormControl>
                  <Autocomplete
                    apiKey={process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY}
                    onPlaceSelected={(place) => {
                      form.setValue("name", place.formatted_address || "");
                      form.setValue("placeId", place.place_id || "");

                      if (place.geometry?.location) {
                        form.setValue("lat", place.geometry.location.lat());
                        form.setValue("lng", place.geometry.location.lng());
                        setSource({
                          lat: place.geometry.location.lat(),
                          lng: place.geometry.location.lng(),
                        });
                      }
                    }}
                    options={{
                      componentRestrictions: { country: "tr" },
                    }}
                    className="w-full h-10 bg-transparent outline-none text-gray-700 placeholder:text-gray-400 text-sm font-medium"
                  />
                </FormControl>
           

        <Button
          type="submit"
          disabled={isPending}
          className={cn(
            "h-10 px-6 rounded-md text-sm font-medium text-white transition-all shadow-md",
            isPending ? "bg-gray-400 cursor-not-allowed" : "bg-emerald-600 hover:bg-emerald-700"
          )}
        >
          {isPending ? (
            <>
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              Kaydediliyor
            </>
          ) : (
            "Konumu Kaydet"
          )}
        </Button>
        </div>
      </form>
      <Toaster />
    </Form>
  );
};

export default InputItem;
