"use client";

import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Separator } from "@/components/ui/separator";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { cn } from "@/lib/utils";
import { ScrollArea } from "@/components/ui/scroll-area";
import Heading from "./Heading";
import Image from "next/image";
import { Textarea } from "@/components/ui/textarea";

import { updateTask } from "@/app/actions/taskActions/updateTask";
import { TaskSensorWithTask } from "../../../components/SensorComponents/SensorsAndMap";

const formSchema = z.object({
  worker_on_road_note: z.string().min(1, {
    message: "İşçi Gitme Notu must be at least 1 character",
  }),
});
export type AssignedSensorFormForOnRoadValues = z.infer<typeof formSchema>;

const AssignedSensorFormForOnRoad = ({
  initialData,
}: {
  initialData: TaskSensorWithTask;
}) => {
  const [loading, setLoading] = useState(false);
  const form = useForm<AssignedSensorFormForOnRoadValues>({
    resolver: zodResolver(formSchema),
    defaultValues: initialData.worker_on_road_note
      ? initialData.worker_on_road_note
      : {
          worker_on_road_note: "",
        },
  });

  const onSubmit = async (data: AssignedSensorFormForOnRoadValues) => {
    try {
      const formData = new FormData();
      formData.append("worker_on_road_note", data.worker_on_road_note);
      try {
        updateTask(data.worker_on_road_note, initialData);
      } catch (error) {
        console.log(error.message);
      }
    } catch (error) {
      console.log(error);
    }
  };

  console.log(initialData.workerArrived);

  return (
    <>
      <ScrollArea className="h-screen ">
        {!initialData.workerArriving && initialData.workerArriving && (
          <div
            className={cn(
              `w-full h-full absolute inset-0 z-10 flex items-center justify-center bg-black text-lg font-bold text-white bg-opacity-50 ${
                initialData.workerArrived && "pointer-events-none "
              }`
            )}
          >
            Yoldayım notu zaten girildi
          </div>
        )}

        {/* ya mantık eğer work arriving true olmuşsa ve work arrived olmamışsa bunu blurla */}
        <div
          className={cn(
            `flex flex-col gap-[5px] w-full  rounded-[5px]  ${
              initialData.workerArriving &&
              !initialData.workerArrived &&
              "blur-sm  pointer-events-none"
            }`
          )}
        >
          <div className="flex flex-col items-center justify-between gap-[10px]  px-[10px]">
            <Heading
              title={initialData.taskSensors.sensorName}
              description={initialData.superVizorDeadline}
              taskSender={initialData.assignedBy.firstName}
              superVizorDescription={initialData.superVizorDescription}
            />
          </div>
          <Separator />
          <div className=" w-full h-fit  flex flex-col p-[10px] rounded-[30px] gap-[5px]">
            <div className=" w-full h-fit flex flex-col justify-start items-start gap-[20px] p-[20px] bg-white rounded-[30px]">
              <div className=" w-full h-fit flex flex-row justify-start items-start gap-[10px] p-0 bg-white">
                <Image
                  src={"/indir.jpg"}
                  alt="232"
                  className={cn(
                    ` w-[200px] h-[100px]  object-fit  rounded-[30px] cursor-pointer  `
                  )}
                  width={100}
                  height={100}
                />
              </div>
              <div className=" relative  w-full h-fit flex flex-col justify-start items-start gap-[5px]  p-[10px]  bg-white">
                <h2
                  className={`w-fit h-[32px]  text-black text-[24] leading-[19.2px] flex items-center justify-center`}
                >
                  Arıza Nedeni
                </h2>
                <h2
                  className={`w-fit h-fit text-black text-[16px] leading-[19.2px] flex items-center justify-center`}
                >
                  Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus
                  dignissimos maiores odit ullam officia explicabo, laboriosam
                  alias ipsa qui. Sed eius maxime quam excepturi architecto
                  voluptatem cupiditate asperiores reprehenderit animi vero
                  quibusdam voluptate fuga ullam, dolor natus eos nam veniam
                  modi! Exercitationem blanditiis aperiam incidunt, distinctio,
                  qui inventore ad sapiente vero quaerat laudantium modi libero
                  maiores consequatur unde dolore explicabo! Amet optio maxime
                  quia beatae, quis iusto eos itaque laborum similique natus
                  sapiente aut voluptate mollitia, unde, cupiditate tempora.
                  Rerum dolor excepturi praesentium ipsum, tempora magnam
                  distinctio hic numquam omnis nostrum tempore? Suscipit
                  reprehenderit hic eius aliquid excepturi odio corporis?
                </h2>
              </div>
            </div>
            <div className="w-full h-fit flex flex-row rounded-[30px]">
              <Form {...form}>
                <form
                  onSubmit={form.handleSubmit(onSubmit)}
                  className={cn(
                    `space-y-8 w-full   flex flex-col  rounded-[30px]`
                  )}
                >
                  <div className="w-full h-fit flex flex-row gap-[5px]">
                    <div
                      className={cn(
                        ` w-full h-full flex flex-col gap-8 bg-white  p-[20px] justify-center transition-all duration-300 rounded-[30px] `
                      )}
                    >
                      <div className="flex flex-col  gap-[10px]">
                        <h3 className=" w-full h-fit text-[24px] font-semibold">
                          Yoldayım Notu
                        </h3>

                        <FormField
                          control={form.control}
                          name="worker_on_road_note"
                          render={({ field }) => (
                            <FormItem>
                              <FormLabel>Yoldayım Notu</FormLabel>
                              <FormControl>
                                <Textarea
                                  disabled={loading}
                                  placeholder="Arıza ile alakalı yoldayım notu ekleyin"
                                  {...field}
                                />
                              </FormControl>
                              <FormMessage />
                            </FormItem>
                          )}
                        />
                      </div>
                    </div>
                  </div>

                  <Button
                    disabled={loading}
                    variant={null}
                    className="ml-auto text-black bg-white rounded-[15px] "
                    type="submit"
                  >
                    Gitme Notu Ekle
                  </Button>
                </form>
              </Form>
            </div>
          </div>
        </div>
      </ScrollArea>
    </>
  );
};

export default AssignedSensorFormForOnRoad;
