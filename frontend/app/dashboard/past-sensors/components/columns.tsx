"use client";

import { ColumnDef } from "@tanstack/react-table";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"
import { format } from "date-fns"
import { tr } from "date-fns/locale"
import { Button } from "@/components/ui/button";
import Link from "next/link";
export type GroupedSensorData = {
  id: number;
  sensorName: string;
  status: string;
  installationDate: string;
  sessions: {
    id: number;
    startTime: string;
    completedTime: string;
    note: string;
  }[];
}
export const columns: ColumnDef<GroupedSensorData>[] = [
  {
    accessorKey: "sensorName",
    header: "Sensör Adı",
  },
  {
    accessorKey: "status",
    header: "Durum",
    cell: ({ row }) => {
      const status = row.original.status
      return (
        <span className={`px-2 py-1 rounded-full text-xs ${
          status === "ACTIVE" ? "bg-green-100 text-green-800" :
          status === "IN_REPAIR" ? "bg-yellow-100 text-yellow-800" :
          status === "FAULTY" ? "bg-red-100 text-red-800" :
          "bg-blue-100 text-blue-800"
        }`}>
          {status}
        </span>
      )
    }
  },
  {
    accessorKey: "installationDate",
    header: "Kurulum Tarihi",
    cell: ({ row }) => {
      const date = new Date(row.original.installationDate)
      return format(date, 'dd MMMM yyyy HH:mm', { locale: tr })
    }
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
                  Görev Geçmişi ({sensor.sessions.length})
                </Button>
              </AccordionTrigger>
            </div>
            <AccordionContent className="pb-0">
              <div className="space-y-2 mt-2">
                {sensor.sessions.map(session => (
                  <div key={session.id} className="p-3  border rounded-lg bg-gray-50">
                    <div className="flex justify-between">
                      <Link href={`/dashboard/past-sensors/${session.id}`}>
                      <span className="font-medium bg-black text-white p-[5px] rounded-lg">
                      Geçmişteki Sensör Görevine Git
                      </span>
                      <span className="font-medium">
                        {format(new Date(session.startTime), 'dd MMMM yyyy HH:mm', { locale: tr })}
                      </span>
                      <span className="text-sm text-gray-500">
                        {format(new Date(session.completedTime), 'dd MMMM yyyy HH:mm', { locale: tr })}
                      </span>
                      </Link>

                    </div>
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