"use client";

import { ColumnDef } from "@tanstack/react-table";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import { format } from "date-fns"
import { tr } from "date-fns/locale"
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { ScrollArea } from "@/components/ui/scroll-area";
import { GroupedSensorData } from "@/app/supervisor/superVizorDataTypes/types";

export const columns: ColumnDef<GroupedSensorData>[] = [
  {
    accessorKey: "username",
    header: "İşçi Adı",
  },

  {
    id: "actions",
    cell: ({ row }) => {
      const sensor = row.original
      
      return (
        <Accordion type="single" collapsible className="w-full">
          <AccordionItem value="item-1" className="border-none">
            <div className="flex items-center gap-2">
              <AccordionTrigger className="py-1 hover:no-underline">
                <Button variant="outline" size="sm">
                  Sensör Çözme Geçmişi ({sensor.sessions.length})
                </Button>
              </AccordionTrigger>
            </div>
            <AccordionContent className="pb-0">

      <ScrollArea className=" h-[200px]">

              <div className="space-y-2 mt-2">
                {sensor.sessions.map(session => (
                  <div key={session.id} className="p-3  border rounded-lg bg-gray-50">
                    <div className="flex justify-between gap-[10px]">
                      <Link href={`/supervisor/dashboard/workers-past-sensors/${session.id}`}>
                      <span className="font-medium bg-black text-white p-[5px] rounded-lg">
                      İşçinin Geçmişteki Sensör Çözümünü İncele
                      </span>

                      </Link>
                      <div className=" w-full gap-[5px] flex flex-row ">
                      <span className="font-medium text-black">
                    Başlangıç Zamanı : {format(new Date(session.startTime), 'dd MMMM yyyy HH:mm', { locale: tr })}
                      </span>
                      <span className="text-sm text-black">
                      Bitiş Zamanı :  {format(new Date(session.completedTime), 'dd MMMM yyyy HH:mm', { locale: tr })}
                      </span>
                      </div>

                    </div>
                  </div>
                ))}
              </div>
      </ScrollArea>

            </AccordionContent>
          </AccordionItem>
        </Accordion>
      )
    }
  }
]