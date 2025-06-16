"use client";

import { useEffect, useState } from "react";
import axios from "axios";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import WorkerSelect from "../WorkerSelect";
import HeatMapForWorkerSensors from "./HeatmapComponents/HeatMapForWorkerSensors";
import { useFetchAllWorkers } from "@/hooks/useFetchAllWorkers";
import { HeatMapSelectWorkerContext } from "@/context/HeatMapSelectWorkerContext";
import WorkerSelectDifferentForHeatmap from "../WorkerSelectDifferentForHeatmap";

const PerformanceHeatmapComponent = ({
  session,
}: {
  session: RequestCookie | undefined;
}) => {
    const [usernameForHeatmap,setUsernameForHeatmap] = useState(25)
  const [dataForHeatmap, setDataForHeatmap] = useState<
    [
      {
        id: string;
        latitude: number;
        longitude: number;
      }
    ]
  >([]);
  const { users, error } = useFetchAllWorkers(session);

  useEffect(() => {
    if (!usernameForHeatmap) return;
    axios
      .get(
        `http://localhost:8080/supervizor/getNonTaskHeatmapComponent/${usernameForHeatmap}`,
        {
          headers: { Authorization: `Bearer ${session?.value}` },
          withCredentials: true,
        }
      )
      .then((res) => setDataForHeatmap(res.data))
      .catch((err) => console.log(err));
  }, [usernameForHeatmap]);

  console.log(usernameForHeatmap);
     
  return (
             <HeatMapSelectWorkerContext.Provider  value={{usernameForHeatmap,setUsernameForHeatmap}}>
    <div className=" bg-white w-fit h-fit flex flex-col justify-start items-start rounded-[20px] p-[20px] gap-[10px] ">
      <div className="bg-black w-fit p-[20px] gap-[5px] rounded-[10px]">
        <WorkerSelectDifferentForHeatmap
          users={users}
          label="İşçi Seçiniz"
        />
      </div>

      <div className=" w-fit h-fit bg-white rounded-[20px] p-[20px]">
        <HeatMapForWorkerSensors response={dataForHeatmap} />
      </div>
    </div>
  
             </HeatMapSelectWorkerContext.Provider>
  
  );
};

export default PerformanceHeatmapComponent;
