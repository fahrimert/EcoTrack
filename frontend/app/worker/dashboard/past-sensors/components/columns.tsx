"use client";

import { ColumnDef } from "@tanstack/react-table";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import { format } from "date-fns"
import { tr } from "date-fns/locale"
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { GroupedSensorData } from "@/app/supervisor/superVizorDataTypes/types";
import { SensorTaskDetail } from "./PastSensorList";
import { Badge } from "@/components/ui/badge";
import { ExternalLink, History } from "lucide-react";
export const columns: ColumnDef<SensorTaskDetail>[] = [
{
    accessorKey: "sensorName",
    header: "Sensör Adı",
    cell: ({ row }) => <span className="font-medium text-slate-900">{row.original.sensorName}</span>
  },
{
    accessorKey: "status",
    header: "Şu Anki Durum",
    cell: ({ row }) => {
      const status = row.original.status;
      return (
        <Badge 
          className={`
            ${status === "ACTIVE" ? "bg-emerald-100 text-emerald-700 hover:bg-emerald-100" :
              status === "IN_REPAIR" ? "bg-amber-100 text-amber-700 hover:bg-amber-100" :
              status === "FAULTY" ? "bg-red-100 text-red-700 hover:bg-red-100" :
              "bg-slate-100 text-slate-700"} border-0
          `}
        >
          {status}
        </Badge>
      )
    }
  },
{
    accessorKey: "installationDate",
    header: "Kurulum Tarihi",
    cell: ({ row }) => {
      const date = new Date(row.original.installationDate);
      return <span className="text-slate-500 text-sm">{format(date, 'dd MMM yyyy', { locale: tr })}</span>
    }
  },
{
    id: "actions",
    header: "İşlemler",
    cell: ({ row }) => {
      const sensor = row.original;
      
      return (
        <Accordion type="single" collapsible className="w-full">
          <AccordionItem value="item-1" className="border-none">
            <div className="flex items-center">
              <AccordionTrigger className="py-1 hover:no-underline px-2 rounded-md hover:bg-slate-50">
                <div className="flex items-center gap-2 text-sm text-slate-600">
                  <History size={16} />
                  <span>{sensor.sessions.length} Kayıt</span>
                </div>
              </AccordionTrigger>
            </div>
            
            <AccordionContent className="p-2">
              <div className="flex flex-col gap-2 mt-2 pl-4 border-l-2 border-slate-100">
                {sensor.sessions.map(session => (
                  <div key={session.id} className="flex flex-col gap-2 p-3 rounded-lg bg-slate-50 border border-slate-100 hover:border-slate-200 transition-colors">
                    
                    <div className="flex justify-between items-start">
                       <div className="flex flex-col">
                          <span className="text-xs font-semibold text-slate-700">Başlangıç: {format(new Date(session.startTime), 'dd MMMM HH:mm', { locale: tr })}</span>
                          <span className="text-xs text-slate-500">Bitiş: {format(new Date(session.completedTime), 'dd MMMM HH:mm', { locale: tr })}</span>
                       </div>
                       
                       <Link href={`/worker/dashboard/past-sensors/${session.id}`}>
                          <Button size="sm" variant="outline" className="h-7 text-xs gap-1 border-slate-300">
                             Detay <ExternalLink size={12} />
                          </Button>
                       </Link>
                    </div>

                    {session.note && (
                        <p className="text-xs text-slate-600 italic line-clamp-2 bg-white p-2 rounded border border-slate-100">
                            "{session.note}"
                        </p>
                    )}
                  </div>
                ))}
              </div> 
            </AccordionContent>
          </AccordionItem>
        </Accordion>
      )
    }
  }
]