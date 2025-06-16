"use client";
import { LineChart, XAxis, YAxis, Tooltip, Line } from "recharts";
import { useEffect, useState } from "react";
import axios from "axios";
import WorkerSelect from "../WorkerSelect";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { useFetchAllWorkers } from "@/hooks/useFetchAllWorkers";

const PerformanceLineChart = ({
  session,
}: {
  session: RequestCookie | undefined;
}) => {
  const [userName, setUsername] = useState(1);

  const [durations, setDurations] = useState<
    Record<string, { formatted: string; rawMinutes: number }>
  >({});

  useEffect(() => {
    if (!userName) return;
    axios
      .get(
        `http://localhost:8080/supervizor/getScatterPlotGraphDataOfWorkerTasks/${userName}`,
        {
          headers: { Authorization: `Bearer ${session?.value}` },
          withCredentials: true,
        }
      )
      .then((res) => setDurations(res.data))
      .catch((err) => console.log(err));
  }, [userName]);

  console.log(durations);
   const { users, loading, error } = useFetchAllWorkers(session);
 
  return (
    <div className="relative w-[70%] h-fit flex flex-col justify-between items-center  bg-white p-[20px] gap-[10px] rounded-[30px] ">
      <h2 className="w-full h-fit flex justify-start items-center text-black text-[24px]  ">
        İşçinin Görev Harici Çözdüğü Sensörler Performans Grafiği
      </h2>

      <div className="w-full h-fit flex flex-col items-start justify-start gap-[10px]">
        <div className=" w-full h-fit ">
          <h2 className="text-black">
            {" "}
            Sensör İdlerine Göre İşçinin Son 1 Ay içerisindeki Görev Harici Kendi Çözdüğü Sensörleri Çözme Dakikalarını Gösterir.
          </h2>
        </div>
        <div className="bg-black w-fit p-[5px] gap-[5px] rounded-[10px]">
          <WorkerSelect
            users={users}
            onChange={(e) => setUsername(e)}
            value={userName}
            label="İşçi Seçiniz"
          />
        </div>

        <div className="w-full h-fit flex p-[20px] items-center justify-center ">
          <LineChart
            width={600}
            height={300}
            data={Object.entries(durations).map(([id, d]) => ({
              id,
              minutes: d.rawMinutes,
              label: d.formatted,
            }))}
          >
            <XAxis dataKey="id" />
            <YAxis />
            <Tooltip formatter={(value, name, props) => props.payload.label} />
            <Line type="monotone" dataKey="minutes" stroke="#8884d8" />
          </LineChart>
        </div>
      </div>
    </div>
  );
};

export default PerformanceLineChart;
