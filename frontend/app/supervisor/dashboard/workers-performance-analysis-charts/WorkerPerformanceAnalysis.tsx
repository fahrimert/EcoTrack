
"use client"
import { FullUserStats } from "../page";
import { cn } from "@/lib/utils";
import { cookies } from "next/headers";
import { LineChart,XAxis,YAxis,Tooltip,Line } from 'recharts';

import {
  Select,
  SelectContent,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { UserOnlineStatusDTO } from "../components/OnlineUsers";
import { useEffect, useState } from "react";
import axios from "axios";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { ChartComponentForWorker, DateTypeCount, SensorTypeCount } from "./ChartComponent";
import WrapperForContext from "./CalendarHeatmapForWorker";
import HeatMapForWorkerSensors from "./HeatMapForWorkerSensors";
import { LeaderBoardComponent } from "./LeaderBoardComponent";
export interface UserDataMap {
  NewUserr: number;
  SupervisorUserr: number;
  TestUser: number;
  SupervisorUser: number;
}

interface LeaderboardData {
  averageChartData?: UserDataMap;
  totalSensorChartData?: UserDataMap;
  last_day?: UserDataMap;
}

type LeaderboardResponse = LeaderboardData[];
const WorkerPerformanceAnalysis = ({session}
  : {session: RequestCookie | undefined}
) => {

  const [userName,setUsername] = useState(1)
  const [userNameForPie,setUsernameForPie] = useState(1)
  const [usernameForCalendar,setUsernameForCalendar] = useState(1)
  const [usernameForHeatmap,setUsernameForHeatmap] = useState(1)

  const [leaderTableFilter,setleaderTableFilter] = useState("")
  const [dataForPie,setdataForPie] = useState<SensorTypeCount[]>([])
  const [dataForCalendar,setdataForCalendar] = useState<DateTypeCount[]>([])
  const [dataforleaderboard,setDataforleaderboard] = useState<LeaderboardResponse>([{}])
  const [dataForHeatmap,setDataForHeatmap] = useState<[{
    id: string;
    latitude: number;
    longitude: number;
}]>([])
  const [users,setUsers] = useState<UserOnlineStatusDTO[]>([])
const [durations, setDurations] = useState<Record<string, { formatted: string, rawMinutes: number }>>({});

   useEffect(() => {
    axios.get(`http://localhost:8080/superVizorSensors/getAllUser`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setUsers(res.data))
    .catch((err) => console.log(err));
  }, []);

useEffect(() => {
  if (!userNameForPie) return
    axios.get(`http://localhost:8080/superVizorSensors/getSensorNames/${userNameForPie}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setdataForPie(res.data))
    .catch((err) => console.log(err));
  }, [userNameForPie]);


  useEffect(() => {

    axios.get(`http://localhost:8080/superVizorSensors/getTheUserPerformanceCharts`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) =>setDataforleaderboard(res.data))
    .catch((err) => console.log(err));
  }, []);
 useEffect(() => {
  if (!usernameForHeatmap) return
    axios.get(`http://localhost:8080/superVizorSensors/getSensorSessionLocationsBasedOnUser/${usernameForHeatmap}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setDataForHeatmap(res.data))
    .catch((err) => console.log(err));
  }, [usernameForHeatmap]);
  
   useEffect(() => {
    if (!userName) return
    axios.get(`http://localhost:8080/superVizorSensors/getSensorSessionsOfLastMonth/${userName}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setDurations(res.data))
    .catch((err) => console.log(err));
  }, [userName]);
   useEffect(() => {
  if (!usernameForCalendar) return

    axios.get(`http://localhost:8080/superVizorSensors/getSensorDatesAndSessionCounts/${usernameForCalendar}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setdataForCalendar(res.data))
    .catch((err) => console.log(err));
  }, [usernameForCalendar]);

  return (
    <>
      {/* WorkerPerformanceAnalysis */}
      <div className={cn(`relative w-full h-fit flex flex-col   items-center  justify-center  gap-[24px] p-0 `)}  >
   {/* bu kısımda kafamda grafikler var tek tek eklenecek türlere göre onların sayılarıyla circle grafiği koyarız o kadar maksat onlara ne eklenmiş  */}
           
  
          <div className="relative w-[70%] h-fit flex flex-col justify-between items-center  bg-white p-[20px] gap-[20px] rounded-[30px] ">
            <h2 className="w-full h-fit flex justify-start items-center text-black text-[24px]  border-b-[1px]">İşçinin Performans Grafiği </h2>


            <div className="w-full h-fit flex flex-col items-start justify-start gap-[10px]" >
            <div className=" w-full h-fit ">
              <h2 className="text-black"> Sensör İdlerine İşçinin Son 1 Ay içerisindeki Sensörleri Çözme Performansını Gösterir.</h2>

            </div>
        <div className="bg-black w-fit p-[20px] gap-[5px] rounded-[10px]">

              <Select
            
                  onValueChange={(e) => (setUsername(e))}
                  value={userName}
                >
                      <h2 className=" border-b-[1px] text-white mb-[10px]">
                    İşçi Seçiniz
                  </h2>
                    <SelectTrigger className="text-white ">
                      <SelectValue
                        defaultValue={userName}
                        className="w-fit bg-white"
                        placeholder="Sensörün Durumunu Güncelleyiniz"
                      ></SelectValue>
                    </SelectTrigger>
                    <SelectContent className="bg-white">
                    {users.map((status,b) => (
                      <SelectItem key={b} value={status.id} className="text-black" >
                        {status.firstName}
                      </SelectItem>
                    ))}
                    </SelectContent>

                </Select>
        </div>

            <div className="w-full h-fit flex p-[20px] items-center justify-center ">
<LineChart 
  width={600}
  height={300}
data={Object.entries(durations).map(([id, d]) => ({
  id,
  minutes: d.rawMinutes,
  label: d.formatted
}))}>
  <XAxis dataKey="id" />
  <YAxis />
  <Tooltip formatter={(value, name, props) => props.payload.label} />
  <Line type="monotone" dataKey="minutes" stroke="#8884d8" />
</LineChart>
           
            </div>
            </div>
         
            
          
          </div>
  
  
   <div className=" bg-white w-[70%] h-fit flex flex-col justify-start items-start rounded-[20px] p-[20px] gap-[10px] ">
        
        <div className="bg-black w-fit p-[20px] gap-[5px] rounded-[10px]">

         <Select
              
                  onValueChange={(e) => (setUsernameForPie(e))}
                  value={userNameForPie}
                >
                  <h2 className=" border-b-[1px] text-white mb-[10px]">
                    İşçi Seçiniz
                  </h2>
                    <SelectTrigger className="text-white ">
                      <SelectValue
                        defaultValue={userNameForPie}
                        className="w-fit bg-white"
                        placeholder="İşçiyi Seçiniz"
                      ></SelectValue>
                    </SelectTrigger>
                    <SelectContent className="bg-white">
                    {users.map((status,b) => (
                      <SelectItem key={b} value={status.id} className="text-black" >
                        {status.firstName}
                      </SelectItem>
                    ))}
                    </SelectContent>

                </Select>
        </div>

<div className="w-full h-[300px] flex flex-row items-center justify-center">

<ChartComponentForWorker dataForPie={dataForPie} />
</div>
{/* burda kullanıcının gittiği sensör isimlerine göre pie chart yapıcaz 
o yüzden de aldığımız data seçtiğimiz kullanıcıya göre 

*/}
     </div> 
   <div className=" bg-white w-[70%] h-fit flex flex-col justify-start items-start rounded-[20px] p-[20px] gap-[10px] ">
        
        <div className="bg-black w-fit p-[20px] gap-[5px] rounded-[10px]">

         <Select
              
                  onValueChange={(e) => (setUsernameForCalendar(e))}
                  value={usernameForCalendar}
                >
                  <h2 className=" border-b-[1px] text-white mb-[10px]">
                    İşçi Seçiniz
                  </h2>
                    <SelectTrigger className="text-white ">
                      <SelectValue
                        defaultValue={usernameForCalendar}
                        className="w-fit bg-white"
                        placeholder="İşçiyi Seçiniz"
                      ></SelectValue>
                    </SelectTrigger>
                    <SelectContent className="bg-white">
                    {users.map((status,b) => (
                      <SelectItem key={b} value={status.id} className="text-black" >
                        {status.firstName}
                      </SelectItem>
                    ))}
                    </SelectContent>

                </Select>
        </div>

<div className="w-full h-fit flex flex-row items-center justify-center">
                    <WrapperForContext session = {session?.value} dataForCalendar = {dataForCalendar}/>
</div>
{/* burda kullanıcının gittiği sensör isimlerine göre pie chart yapıcaz 
o yüzden de aldığımız data seçtiğimiz kullanıcıya göre 

*/}
     </div> 



             <div className=" bg-white w-[70%] h-fit flex flex-col justify-start items-start rounded-[20px] p-[20px] gap-[10px] ">
        
        <div className="bg-black w-fit p-[20px] gap-[5px] rounded-[10px]">

         <Select
              
                  onValueChange={(e) => (setUsernameForHeatmap(e))}
                  value={usernameForHeatmap}
                >
                  <h2 className=" border-b-[1px] text-white mb-[10px]">
                    İşçi Seçiniz
                  </h2>
                    <SelectTrigger className="text-white ">
                      <SelectValue
                        defaultValue={usernameForHeatmap}
                        className="w-fit bg-white"
                        placeholder="İşçiyi Seçiniz"
                      ></SelectValue>
                    </SelectTrigger>
                    <SelectContent className="bg-white">
                    {users.map((status,b) => (
                      <SelectItem key={b} value={status.id} className="text-black" >
                        {status.firstName}
                      </SelectItem>
                    ))}
                    </SelectContent>

                </Select>
        </div>

        
              <div className=" w-[70%] h-fit bg-white rounded-[20px] p-[20px]">
                <HeatMapForWorkerSensors response = {dataForHeatmap}/>
              </div>
     </div> 

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
        </div> 
    </>
  );
};

export default WorkerPerformanceAnalysis;
