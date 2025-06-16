"use client";

import { Task } from "@/app/supervisor/superVizorDataTypes/types";
import { ColumnDef } from "@tanstack/react-table";



export const columns: ColumnDef<Task>[] = [
   {
    accessorKey: "assignedTo.firstName",
    header: "Assignalanan Worker Adı",
  },
  {
    accessorKey: "sensorDTO.sensorName",
    header: "Sensör Adı",
  },
 {
    accessorKey: "superVizorDeadline",
    header: "Deadline",
  },

 {
    accessorKey: "workerArriving",
    header: "workerArriving",
  },
 {
    accessorKey: "workerArrived",
    header: "workerArrived",
  },
]