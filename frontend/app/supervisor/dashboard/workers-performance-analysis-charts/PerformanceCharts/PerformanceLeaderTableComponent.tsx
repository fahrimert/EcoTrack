
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
import { LeaderBoardComponent } from "./LeaderBoardComponent/LeaderBoardComponent";
import { LeaderboardResponse } from "@/app/supervisor/superVizorDataTypes/types";

const PerformanceLeaderTableComponent = ({session} : {session: RequestCookie | undefined}) => {
    
    
      const [leaderTableFilter,setleaderTableFilter] = useState("")
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

                  <div className=" bg-white w-[70%] h-fit flex flex-col justify-start items-start rounded-[20px] p-[20px] gap-[10px] ">
        
        <div className="bg-black w-fit p-[20px] gap-[5px] rounded-[10px] text-white">

         <Select
              
                  onValueChange={(e) => (setleaderTableFilter(e))}
                  value={leaderTableFilter}
                >
                  <h2 className=" border-b-[1px] text-white mb-[10px]">
                    Lider Tablosu Filtresi Seçiniz
                  </h2>
                    <SelectTrigger className="text-white ">
                      <SelectValue
                        defaultValue={leaderTableFilter}
                        className="w-fit  text-white"
                        placeholder="Lider Tablosunu Seçiniz"
                      >
<h2 className="text-white">
  İşçiyi Seçiniz
</h2>

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
                <LeaderBoardComponent leaderTableFilter = {leaderTableFilter} dataforleaderboard = { dataforleaderboard[0].averageChartData} />
      
          }
            {leaderTableFilter == "Toplam Çözülen Sensör Tablosu" ?
            
                <LeaderBoardComponent leaderTableFilter = {leaderTableFilter} dataforleaderboard = { dataforleaderboard[1].totalSensorChartData} />
           : null
          }
            {leaderTableFilter == "Bugün Çözülen Sensöre Göre Lider tablosu" ?
            
                <LeaderBoardComponent leaderTableFilter = {leaderTableFilter} dataforleaderboard = { dataforleaderboard[2].last_day} />
           : null
          }
              </div> 
     </div> 
  )
}

export default PerformanceLeaderTableComponent