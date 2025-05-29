"use client";

import { ColumnDef } from "@tanstack/react-table";
import { Task } from "../page";



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