"use client";

import { ColumnDef } from "@tanstack/react-table";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import { format } from "date-fns"
import { tr } from "date-fns/locale"
import { Button } from "@/components/ui/button";
import { ScrollArea } from "@/components/ui/scroll-area";
import { GroupedSensorDataOnPDFReport } from "@/app/supervisor/superVizorDataTypes/types";
import { Sheet, SheetContent, SheetTrigger } from "./SheetOfPDF";
import DetailsPage from "./DetailsPage";



export const columns: ColumnDef<GroupedSensorDataOnPDFReport>[] = [
  {
    accessorKey: "sensorName",
    header: "Sensör Adı",
  },

  {
    id: "actions",
    cell: ({ row }) => {
      const sensor = row.original
      
      return (
        <Accordion type="single" collapsible className="w-full">
          <AccordionItem value="item-1" className="border-none flex flex-col">
            <div className="flex items-center gap-2">
              <AccordionTrigger className="py-1 hover:no-underline">
                <Button variant="outline" size="sm">
                  Görev Geçmişi ({sensor.sessions.length !== null ? sensor.sessions.length  : null })
                </Button>
              </AccordionTrigger>
            </div>
            <AccordionContent className="pb-0">

      <ScrollArea className=" h-[200px]">

              <div className="space-y-2 mt-2">
                {sensor.sessions.map(session => (
                  <div key={session.id} className="p-3  border rounded-lg flex flex-col bg-gray-50 w-fit ">
               <Sheet >
  <SheetTrigger className="flex flex-col justify-between items-start gap-[10px]">
    
     <span className="font-medium bg-black text-white p-[5px] rounded-lg">
                      Sensör Raporunu Önizle
                      </span>
                      
                          
                      <span className="font-medium text-gray-500">
                    Başlama Tarihi:    {format(new Date(session.startTime), 'dd MMMM yyyy HH:mm', { locale: tr })}
                      </span>
                      <span className="text-sm text-gray-500">
                       Bitirme Tarihi: {format(new Date(session.completedTime), 'dd MMMM yyyy HH:mm', { locale: tr })}
                      </span></SheetTrigger>
  <SheetContent className="bg-white w-[800px] items-center justify-center flex">
  <DetailsPage session = {session.sessionkey} sessionId = {session.id} />
  </SheetContent>
</Sheet>
                 

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