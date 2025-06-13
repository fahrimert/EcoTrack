
"use client"

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useEffect, useState } from "react";
import axios from "axios";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { LeaderboardResponse, LeaderboardResponseSuperVizor } from "@/app/supervisor/superVizorDataTypes/types";
import { LeaderBoardComponent } from "@/app/supervisor/dashboard/workers-performance-analysis-charts/PerformanceCharts/LeaderBoardComponent/LeaderBoardComponent";
import { LeaderBoardComponentForWorker } from "./LeaderBoardComponentForWorker";

const LeaderTableForWorker = ({session} : {session: RequestCookie | undefined}) => {
    
    
      const [leaderTableFilter,setleaderTableFilter] = useState("Lider Tablosunu Seçiniz")
      const [dataforleaderboard,setDataforleaderboard] = useState<LeaderboardResponse>([{}])

    
  useEffect(() => {

    axios.get(`http://localhost:8080/superVizorSensors/getTheUserPerformanceCharts`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) =>setDataforleaderboard(res.data))
    .catch((err) => console.log(err));
  }, []);
    
  return (

                  <div className=" bg-white w-full h-fit flex flex-col justify-start items-start rounded-[20px] p-[20px] gap-[10px] ">
        
        <div className="bg-black w-full p-[20px] gap-[5px] rounded-[10px] text-white">

         <Select
              
                  onValueChange={(e) => (setleaderTableFilter(e))}
                  value={leaderTableFilter}
                >
                  <h2 className=" border-b-[1px] text-white mb-[10px]">
                    İşçi Lider Tablosu Filtresi Seçiniz
                  </h2>
                    <SelectTrigger className="text-white ">
                      <SelectValue
                        defaultValue={leaderTableFilter}
                        className="w-fit  text-white"
                        placeholder="Lider Tablosunu Seçiniz"
                      >
<h2 className="text-white">
{leaderTableFilter}</h2>

                      </SelectValue>
                    </SelectTrigger>
                    <SelectContent className="bg-white">
                    {["Ortalama Dakika Tablosu","Toplam Çözülen Sensör Tablosu","Bugün Çözülen Sensöre Göre Lider tablosu"].map((status,b) => (
                      <SelectItem key={b} value={status} className="text-black" >
                        {status}
                      </SelectItem>
                    ))}
                    </SelectContent>

                </Select>
        </div>

                { }
          <div className=" w-full h-fit bg-white rounded-[20px] p-[20px]">
            {leaderTableFilter == "Ortalama Dakika Tablosu" 
            &&
                <LeaderBoardComponentForWorker leaderTableFilter = {leaderTableFilter} dataforleaderboard = { dataforleaderboard[0].averageChartData} />
      
          }
            {leaderTableFilter == "Toplam Çözülen Sensör Tablosu" ?
            
                <LeaderBoardComponentForWorker leaderTableFilter = {leaderTableFilter} dataforleaderboard = { dataforleaderboard[1].totalSensorChartData} />
           : null
          }
            {leaderTableFilter == "Bugün Çözülen Sensöre Göre Lider tablosu" ?
            
                <LeaderBoardComponentForWorker leaderTableFilter = {leaderTableFilter} dataforleaderboard = { dataforleaderboard[2].last_day} />
           : null
          }
              </div> 
     </div> 
  )
}

export default LeaderTableForWorker