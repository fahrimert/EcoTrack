"use client"

import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
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
import toast from "react-hot-toast";
import axios from "axios";
import { UserOnlineStatusDTO } from "../components/OnlineUsers";

import * as React from "react"
import { Calendar as CalendarIcon } from "lucide-react"

import { Calendar } from "@/components/ui/Calendar "
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"
import DatePickerDemo  from "./DatePicker";
import { WarningIcon } from "@chakra-ui/icons";
import { WorkerLocationContext } from "@/context/WorkerLocationContext";
import { SensorDestinationContext } from "@/context/SensorDestinationContext";
import { createTask } from "@/app/actions/sensorActions/createTask";
 interface Sensor {
  id: number;
  sensorName: string;
  status: string;
  color_code: string;
  latitude: number;
  longitude: number;
  currentSensorSession: {
    id: number | null;
    sensorName: string;
    displayName: string;
    color_code: string;
    note: string | null;
    startTime: string | null;
    completedTime: string | null;
    latitude: number;
    longitude: number;
  };
}
 
const formSchema = z.object({
  description:z.string().min(10,{message:"Açıklama en az 10 karakter olmalı"}),
  deadline:z.date({message:"Bir Tarih Seçiniz"}),
  userId: z.string().min(1,{message:"Bir İşçi Seçiniz"}),
  sensorId: z.string().min(1,{message:"Bir Sensör Seçiniz"}),
},
)
export type AttachTaskFormValues = z.infer<typeof formSchema>
//güncelleme yapmıyon createleme yapıyon


const AttachTaskForm= ({session} : {session: string | undefined  }) => {

 const { source ,setSource} = React.useContext(WorkerLocationContext);
  const { destination ,setDestination} = React.useContext(SensorDestinationContext);

      const [users,setUsers] = useState<UserOnlineStatusDTO[]>([])
   useEffect(() => {
    axios.get(`http://localhost:8080/superVizorSensors/getAllUser`, {
      headers: { Authorization: `Bearer ${session}` },
      withCredentials: true,
    })
    .then((res) => setUsers(res.data))
    .catch((err) => console.log(err));
  }, []);

  function handleTimeChange(type:"hour" | "minute" | "ampm",value:string){
    const currentDate = form.getValues("deadline") || new Date();

    let newDate = new Date(currentDate)

    if (type === "hour") {
      const hour = parseInt(value,10)
      newDate.setHours(newDate.getHours() >= 12 ? hour + 12 : hour)
    }
    else if (type === "minute"){
      newDate.setMinutes(parseInt(value,10))
    }
    else if (type === "ampm"){
      const hours = newDate.getHours()
      if (value === "AM" && hours >= 12) {
        newDate.setHours(hours - 12)
      } else if (value === "PM" && hours < 12){
        newDate.setHours(hours + 12)
      }
    }
    form.setValue("deadline",newDate)
  }


  
  const [sensorListData,setSensorListData] = useState<Sensor[]>()

   useEffect(() => {
    axios.get(`http://localhost:8080/superVizorSensors/getAllAvailableSensors`, {
      headers: { Authorization: `Bearer ${session}` },
      withCredentials: true,
    })
    .then((res) => setSensorListData(res.data))
    .catch((err) => console.log(err));
  }, []);




          var today = new Date();
   
    const [loading,setLoading] = useState(false)
    const form = useForm<AttachTaskFormValues>({
    resolver:zodResolver(formSchema),
    defaultValues:{
        description : "",
        deadline:today,
        userId:"",
        sensorId:"",

    }
  });
    const userId = form.watch("userId");

    useEffect(() => {
    axios.get(`http://localhost:8080/getUserLocationBasedOnİd/${userId}`, {
      headers: { Authorization: `Bearer ${session}` },
      withCredentials: true,
    })
    .then((res) => {setSource({lat:res.data.latitude,lng:res.data.longitude})
  console.log(source);
  })
    .catch((err) => console.log(err));
  },[userId]);


  const sensorId = form.watch("sensorId");

    useEffect(() => {
    axios.get(`http://localhost:8080/getUserLocationBasedOnİd/${sensorId}`, {
      headers: { Authorization: `Bearer ${session}` },
      withCredentials: true,
    })
    .then((res) => {setDestination({lat:res.data.latitude,lng:res.data.longitude})
  console.log(destination);
  })
    .catch((err) => console.log(err));
  },[sensorId]);


//her userId değiştiği zaman locationunu alıp setsource yapmamız lazım 


  //seçtiğim kullanıcıya göre o kullanıcıyı alıp centeri o kullanıcının konumu 
  // sensörü de seçtiğim sensör id ye göre alıp o sensörün konumu yapması lazım

  // onchangede yapması lazım


  const onSubmit = async (data:AttachTaskFormValues) => {
    try{
    /*   const formData = new FormData();
    
      formData.append('note', data.not);
      formData.append('statusID', data.statusId);
      console.log(data.statusId);
      if (data.files) {
        for (let i = 0; i < data.files.length; i++) {
          formData.append('files', data.files[i]);
        }
      } */
      try {  
        await createTask(data)

        /* const returnData = await updateSensor(formData,initialData) */
        } catch (error) {
       console.log(error.message);
         
        }

    }

   catch (error) {
      console.log(error);
    }

  
  }
  const [date, setDate] = React.useState<Date>()
 
  function handleDateSelect(date: Date | undefined) {
    if (date) {
      form.setValue("deadline", date);
    }
  }

  return (
    <>
  <ScrollArea className="h-screen">

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
<h3 className=" w-full h-fit text-[24px] font-semibold">İşçiye Görev Atayın</h3>
    

                <FormField
                 control={form.control}
                 name = "description"
                 render = {({field}) => (
                    <FormItem>
                        <FormLabel>İşçiye Gönderilecek Açıklama</FormLabel>
                        <FormControl>
                            <Textarea disabled = {loading} placeholder="İşçiye Görev İle İlgili Gönderilecek Açıklama" {...field}/>
                        </FormControl>
                        <FormMessage className="text-white bg-red-400  w-fit p-[5px] rounded-[3px]" />
                    </FormItem>
)}
/>
                <FormField 
                
                 control={form.control}
                 name = "deadline"
                 render = {({field}) => (
                        <FormItem className="flex flex-col">
              <FormLabel>Enter your date & time (12h)</FormLabel>
              <Popover>
                <PopoverTrigger >
                  <FormControl>
                    <Button
                      variant={"outline"}
                      className={cn(
                        "w-full pl-3 text-left font-normal",
                        !field.value && "text-muted-foreground"
                      )}
                    >
                      {field.value ? (
                        format(field.value, "MM/dd/yyyy hh:mm aa")
                      ) : (
                        <span>MM/DD/YYYY hh:mm aa</span>
                      )}
                      <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0 bg-white">
                  <div className="sm:flex">
                    <Calendar
                      mode="single"
                      selected={field.value}
                      onSelect={handleDateSelect}
                      initialFocus
                    />
                    <div className="flex flex-col sm:flex-row sm:h-[300px] divide-y sm:divide-y-0 sm:divide-x bg-white">
                        
                      <ScrollArea className="w-64 sm:w-auto border">
                        <div className="flex sm:flex-col p-2">
                          {Array.from({ length: 12 }, (_, i) => i + 1)
                            .reverse()
                            .map((hour) => (
                              <Button
                                key={hour}
                                size="icon"
                                variant={
                                  field.value &&
                                  field.value.getHours() % 12 === hour % 12
                                    ? "default"
                                    : "ghost"
                                }
                                className="sm:w-full shrink-0 aspect-square"
                                onClick={() =>
                                  handleTimeChange("hour", hour.toString())
                                }
                              >
                                {hour}
                              </Button>
                            ))}
                        </div>
                      </ScrollArea>
                      <ScrollArea className="w-64 sm:w-auto border">
                        <div className="flex sm:flex-col p-2">
                          {Array.from({ length: 12 }, (_, i) => i * 5).map(
                            (minute) => (
                              <Button
                                key={minute}
                                size="icon"
                                variant={
                                  field.value &&
                                  field.value.getMinutes() === minute
                                    ? "default"
                                    : "ghost"
                                }
                                className="sm:w-full shrink-0 aspect-square"
                                onClick={() =>
                                  handleTimeChange("minute", minute.toString())
                                }
                              >
                                {minute.toString().padStart(2, "0")}
                              </Button>
                            )
                          )}
                        </div>
                      </ScrollArea>
                      
                      <ScrollArea className="">
                        <div className="flex sm:flex-col p-2">
                          {["AM", "PM"].map((ampm) => (
                            <Button
                              key={ampm}
                              size="icon"
                              variant={
                                field.value &&
                                ((ampm === "AM" &&
                                  field.value.getHours() < 12) ||
                                  (ampm === "PM" &&
                                    field.value.getHours() >= 12))
                                  ? "default"
                                  : "ghost"
                              }
                              className="sm:w-full shrink-0 aspect-square"
                              onClick={() => handleTimeChange("ampm", ampm)}
                            >
                              {ampm}
                            </Button>
                          ))}
                        </div>
                      </ScrollArea>
                    </div>
                  </div>
                </PopoverContent>
              </Popover>
              <FormMessage />
            </FormItem>
          )}
/>

<FormField
            /* bunu category componentından aldık  */
            control={form.control}
            name="userId"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-black">Sensörü Atayacağınız İşçiyi Seçiniz </FormLabel>
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
                        placeholder="İşçi Seçiniz"
                      ></SelectValue>
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent className="bg-[#edecea]">
                    {users.map((status,b) => (
                      <SelectItem key={b} value={status.id} className="text-black" >
                        {status.firstName}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                <FormMessage className="text-white bg-red-400  w-fit p-[5px] rounded-[3px]" />
              </FormItem>
            )}
          />

  <FormField
            /* bunu category componentından aldık  */
            control={form.control}
            name="sensorId"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-black">Müsait Sensörü Seçiniz </FormLabel>
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
                        placeholder="Sensör Seçiniz"
                      ></SelectValue>
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent className="bg-[#edecea]">
                    {sensorListData?.map((status,b) => (
                      <SelectItem key={b} value={status.id} className="text-black" >
                        {status.sensorName}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                <FormMessage className="text-white bg-red-400  w-fit p-[5px] rounded-[3px]"  />
              </FormItem>
            )}
          />

     
              




</div>

</div>



</div>

              
              <Button disabled={loading} variant={null} className="ml-auto text-black bg-white rounded-[15px] " type="submit" >
                Emir Ver
              </Button>
          </form>
        </Form>
   
 </div>

        
      
      </div>
</div>
</ScrollArea>

            </>
  )
}

export default AttachTaskForm