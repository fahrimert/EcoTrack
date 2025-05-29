"use client"

import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"
import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/Calendar "
import React from "react"
import { CalendarIcon } from "lucide-react"
import { format } from "date-fns"

export default function DatePickerDemo() {
  const [date, setDate] = React.useState<Date>()

  return (
    <div className="p-20 w-fit h-fit">
<Popover>
  <PopoverTrigger><CalendarIcon />
          {date ? format(date, "PPP") : <span>Pick a date</span>}</PopoverTrigger>
  <PopoverContent className="bg-white">
    <Calendar
          mode="single"
          selected={date}
          onSelect={setDate}
          initialFocus
        />

  </PopoverContent>
</Popover>
    </div>
  )
}
