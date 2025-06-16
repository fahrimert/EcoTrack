"use client";
import dynamic from "next/dynamic";
import "chart.js/auto";
import React from "react";
import { workerTimeBasedStatsGraph } from "../../../superVizorDataTypes/types";
export const Bar = dynamic(() => import("react-chartjs-2").then((mod) => mod.Bar), {
  ssr: false,
});

const BarChart = ({
  lastweekdata,
  lastmonthdata,
  lastdaydata
}: {
  lastweekdata: workerTimeBasedStatsGraph
    lastdaydata: workerTimeBasedStatsGraph
    lastmonthdata: workerTimeBasedStatsGraph
}) => {
  const periods = lastweekdata.map((item) => item.period);
  const userTypes = lastweekdata[0]?.labels ?? [];

  const datasets = userTypes.map((userType, idx) => {
    return {
      label: userType,
      data: lastweekdata.map((period) => Number(period.data[idx] || 0)),
      backgroundColor: `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(
        Math.random() * 255
      )}, ${Math.floor(Math.random() * 255)}, 0.5)`,
      borderColor: "rgba(0,0,0,0.1)",
      borderWidth: 1,
    };
  });

  const data = {
    labels: periods, 
    datasets,
  };
   const lastdayperiods = lastdaydata.map((item) => item.period);
  const userTypeslastday = lastdaydata[0]?.labels ?? [];

  const datasetslastday = userTypeslastday.map((userType, idx) => {
    return {
      label: userType,
      data: lastdaydata.map((period) => Number(period.data[idx] || 0)),
      backgroundColor: `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(
        Math.random() * 255
      )}, ${Math.floor(Math.random() * 255)}, 0.5)`,
      borderColor: "rgba(0,0,0,0.1)",
      borderWidth: 1,
    };
  });

  const lastdaydataa = {
    labels: lastdayperiods,
    datasets:datasetslastday,
  };

 const lastMonthPeriods = lastmonthdata.map((item) => item.period);

  const datasetslastmonth = userTypeslastday.map((userType, idx) => {
    return {
      label: userType,
      data: lastmonthdata.map((period) => Number(period.data[idx] || 0)),
      backgroundColor: `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(
        Math.random() * 255
      )}, ${Math.floor(Math.random() * 255)}, 0.5)`,
      borderColor: "rgba(0,0,0,0.1)",
      borderWidth: 1,
    };
  });

  const lastmonthdataa = {
    labels: lastMonthPeriods, 
    datasets:datasetslastmonth,
  };


  return (
    <div className="w-full h-full p-[20px] flex flex-col">
      <h1 className="text-black text-[24px]">Tarihlere Göre Görev Harici Kullanıcı Bazlı Sensör Çözme Analizi</h1>
      <div  className="w-full h-full p-[20px]  grid grid-cols-2 max-md:grid max-md:grid-cols-1">
        <div className=" w-full flex flex-col">
          <h2>Haftaya Göre Dağılım </h2>

      <Bar data={data} />
        </div>
              <div className="w-full flex flex-col" >
          <h2>Son Aya  Göre Dağılım </h2>

      <Bar data={lastmonthdataa} />
        </div>
        <div className="w-full flex flex-col"> 
          <h2>Son 24 Saate Göre Dağılım </h2>

      <Bar data={lastdaydataa} />
        </div>
  

      </div>

    </div>
  );
};

export default BarChart;
