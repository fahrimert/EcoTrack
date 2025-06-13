
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
import { LeaderBoardComponentForSupervizor } from "./LeaderBoardComponentForSupervizor";

const LeaderTableForSupervizor = ({session} : {session: RequestCookie | undefined}) => {
    
    
      const [leaderTableFilter,setleaderTableFilter] = useState("Lider Tablosunu Seçiniz")
      const [supervizorDataForLeaderboard,setSupervizorDataForLeaderboard] = useState<LeaderboardResponseSuperVizor>([{}])

    

  useEffect(() => {

    axios.get(`http://localhost:8080/manager/getTheSupervizorPerformanceCharts`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) =>setSupervizorDataForLeaderboard(res.data))
    .catch((err) => console.log(err));
  }, []);
  console.log(supervizorDataForLeaderboard);

  console.log(supervizorDataForLeaderboard[0].SuperVizorGivenCompletedTasksAverageMinLastMonth);
  return (

                  <div className=" bg-white w-full h-fit flex flex-col justify-start items-start rounded-[20px] p-[20px] gap-[10px] ">
        
        <div className="bg-black w-full p-[20px] gap-[5px] rounded-[10px] text-white">

         <Select
              
                  onValueChange={(e) => (setleaderTableFilter(e))}
                  value={leaderTableFilter}
                >
                  <h2 className=" border-b-[1px] text-white mb-[10px]">
                    Supervizor Lider Tablosu Filtresi Seçiniz
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
                    {["Supervizor Tasklerinin Ortalama Çözülme Tablosu","Süpervizörlerin Toplam Çözülmüş Task Tablosu","Süpervizörlerin Vermiş Oldukları Görevlerde Bugün Çözülen Görevler Tablosu"].map((status,b) => (
                      <SelectItem key={b} value={status} className="text-black" >
                        {status}
                      </SelectItem>
                    ))}
                    </SelectContent>

                </Select>
        </div>

                { }

          <div className=" w-full h-fit bg-white rounded-[20px] p-[20px]">
            {leaderTableFilter == "Supervizor Tasklerinin Ortalama Çözülme Tablosu" 
            &&
                <LeaderBoardComponentForSupervizor leaderTableFilter = {leaderTableFilter} dataforleaderboard = { supervizorDataForLeaderboard[0].SuperVizorGivenCompletedTasksAverageMinLastMonth} />
      
          }
            {leaderTableFilter == "Süpervizörlerin Toplam Çözülmüş Task Tablosu" ?
            
                <LeaderBoardComponentForSupervizor leaderTableFilter = {leaderTableFilter} dataforleaderboard = { supervizorDataForLeaderboard[1].SupervizorGivenTasksTotalCount
} />
           : null
          }
            {leaderTableFilter == "Süpervizörlerin Vermiş Oldukları Görevlerde Bugün Çözülen Görevler Tablosu" ?
            
                <LeaderBoardComponentForSupervizor leaderTableFilter = {leaderTableFilter} dataforleaderboard = { supervizorDataForLeaderboard[2].SupervizorGivenTaskCountOfLastDay
} />
           : null
          }
              </div> 
     </div> 
  )
}

export default LeaderTableForSupervizor