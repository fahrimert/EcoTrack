
"use client"

import { useEffect, useState } from "react";
import axios from "axios";
import { ChartComponentForWorker } from "./ChartComponent/ChartComponent";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import WorkerSelect from "../WorkerSelect";
import { useFetchAllWorkers } from "@/hooks/useFetchAllWorkers";
import { SensorTypeCount } from "@/app/supervisor/superVizorDataTypes/types";

const PerformanceChartComponent = ({session} : {session: RequestCookie | undefined}) => {
    
      const [userNameForPie,setUsernameForPie] = useState(1)
      const [dataForPie,setdataForPie] = useState<SensorTypeCount[]>([])

     
    
  useEffect(() => {
  if (!userNameForPie) return
    axios.get(`http://localhost:8080/supervizor/getNonTaskSessionSolvingSensorNames/${userNameForPie}`, {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setdataForPie(res.data))
    .catch((err) => console.log(err));
  }, [userNameForPie]);

      const { users,  error } = useFetchAllWorkers(session);



  return (
  <div className=" bg-white w-[70%] h-fit flex flex-col justify-start items-start rounded-[20px] p-[20px] gap-[10px] ">
        
        <div className="bg-black w-fit p-[20px] gap-[5px] rounded-[10px]">

         <WorkerSelect
      users={users}
     onChange={e => setUsernameForPie(e)}
        value={userNameForPie}
        label="İşçi Seçiniz"
        />
        </div>

<div className="w-full h-fit flex flex-col items-start justify-start">
    <h2 className='text-[18px]'>Kullanıcının Görev Dışı Çözdüğü Sensörlerin İsimlere Göre Dağılımları</h2>
<div className="w-full  flex flex-col items-center justify-center h-[300px]">

<ChartComponentForWorker dataForPie={dataForPie} />
</div>
</div>
{/* burda kullanıcının gittiği sensör isimlerine göre pie chart yapıcaz 
o yüzden de aldığımız data seçtiğimiz kullanıcıya göre 

*/}
     </div> 
  )
}

export default PerformanceChartComponent