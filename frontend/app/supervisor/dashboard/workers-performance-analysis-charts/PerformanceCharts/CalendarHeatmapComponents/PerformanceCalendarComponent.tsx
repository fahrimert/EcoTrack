"use client"

import { useEffect, useState } from "react";
import axios from "axios";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { useFetchAllWorkers } from "@/hooks/useFetchAllWorkers";
import WorkerSelect from "../../WorkerSelect";
import WrapperForContext from "./CalendarHeatmapForWorker";
import { DateTypeCount } from "@/app/supervisor/superVizorDataTypes/types";
const PerformanceCalendarComponent = ({session } : {session: RequestCookie | undefined}) => {
  const [usernameForCalendar,setUsernameForCalendar] = useState(1)
  const [dataForCalendar,setdataForCalendar] = useState<DateTypeCount[]>([])

  
       useEffect(() => {
  if (!usernameForCalendar) return

    axios.get(`http://localhost:8080/superVizorSensors/getSensorDatesAndSessionCounts/${usernameForCalendar}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setdataForCalendar(res.data))
    .catch((err) => console.log(err));
  }, [usernameForCalendar]);

        const { users,  error } = useFetchAllWorkers(session);
  
  return (
      <div className=" bg-white w-fit h-fit flex flex-col justify-start items-start rounded-[20px] p-[20px] gap-[10px] ">
         <h2 className="text-black text-[24px]">
            {" "}
Kullanıcı Bazlı Olarak işçinin Bu Ay İçerisindeki Görev Dışında Çözdüğü Tarihlere Göre Onarma Sayılarını Verir

          </h2>
        <div className="bg-black w-fit p-[20px] gap-[5px] rounded-[10px]">

          <WorkerSelect
      users={users}
     onChange={e => setUsernameForCalendar(e)}
        value={usernameForCalendar}
        label="İşçi Seçiniz"
        />
        </div>
  
<div className="w-full h-fit flex flex-row items-center justify-center">
  
                    <WrapperForContext session = {session} dataForCalendar = {dataForCalendar}/>
</div>

     </div> 
  )
}

export default PerformanceCalendarComponent