"use client";
import React, { useContext } from "react";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Toaster } from "react-hot-toast";
import Autocomplete from "react-google-autocomplete";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { SourceContext } from "@/context/SourceContext";
import { createLocation } from "@/app/actions/userActions/createUserLocation";

const createLocationFormSchema = z.object({
  name: z.string().min(3, {
    message: "Konum  en az 3 karakter olmalıdır.",
  }),
  placeId: z.string().optional(),
  lat: z.number().optional(),
  lng: z.number().optional(),
});

export type LocationFormValue = z.infer<typeof createLocationFormSchema>;

const InputItem = ({ session }: { session: RequestCookie }) => {
  const [loading, setLoading] = useState(false);
  const { source, setSource } = useContext(SourceContext);

  const form = useForm<LocationFormValue>({
    resolver: zodResolver(createLocationFormSchema),
    defaultValues: {
      name: "",
      placeId: "",
    },
  });

  const onSubmit = async (data: LocationFormValue) => {
    try {
      createLocation(data.lat!, data.lng!, session);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="w-full  h-fit p-[10px]   border-[1px]  "
      >
        <div className="w-fit h-fit flex flex-col  gap-[20px]  p-[10px]  bg-white">
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-black">Adres</FormLabel>
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
                          lat: place.geometry.location.lat(),
                          lng: place.geometry.location.lng(),
                        });
                      }
                    }}
                    options={{
                      componentRestrictions: { country: "tr" },
                    }}
                    className="  h-[50px] w-full box-border text-sm font-medium   text-black bg-white border-[2px] shadow-md  "
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <Button
            variant={null}
            disabled={loading}
            type="submit"
            className="  h-[50px] w-full  rounded-[20px] box-border text-sm font-medium   text-black bg-white border-[2px] shadow-md  "
          >
            Gidin
          </Button>
        </div>
      </form>
      <Toaster />
    </Form>
  );
};

export default InputItem;
