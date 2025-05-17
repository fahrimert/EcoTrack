"use client";
import dynamic from "next/dynamic";
import "chart.js/auto";
import React from "react";
import { Client, over } from "stompjs";
export const Bar = dynamic(() => import("react-chartjs-2").then((mod) => mod.Bar), {
  ssr: false,
});

const BarChart = ({
  lastweekdata,
  lastmonthdata,
  lastdaydata
}: {
  lastweekdata: {
    period: string;
    labels: string[];
    data: number[];
  }[];
    lastdaydata: {
    period: string;
    labels: string[];
    data: number[];
  }[];
    lastmonthdata: {
    period: string;
    labels: string[];
    data: number[];
  }[];
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
    labels: periods, // ["last_month", "last_week", "last_day"]
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
    labels: lastdayperiods, // ["last_month", "last_week", "last_day"]
    datasets:datasetslastday,
  };

 const lastMonthPeriods = lastmonthdata.map((item) => item.period);
  const userTypeslastmonth = lastMonthPeriods[0]?.labels ?? [];

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
    labels: lastMonthPeriods, // ["last_month", "last_week", "last_day"]
    datasets:datasetslastmonth,
  };


  return (
    <div className="w-full h-full p-[20px] flex flex-col">
      <h1 className="text-black text-[24px]">Tarihlere Göre Kullanıcı Türlerine Göre Dağılım</h1>
      <div  className="w-full h-full p-[20px]  flex flex-row max-xl:flex max-md:flex-col">
        <div className=" w-full flex flex-col">
          <h2>Haftaya Göre Dağılım </h2>

      <Bar data={data} />
        </div>
        <div className="w-full flex flex-col"> 
          <h2>Son 24 saate Göre Dağılım </h2>

      <Bar data={lastdaydataa} />
        </div>
        <div className="w-full flex flex-col" >
          <h2>Son Aya  Göre Dağılım </h2>

      <Bar data={lastmonthdataa} />
        </div>

      </div>

    </div>
  );
};

export default BarChart;
