"use client";
import React, { useEffect } from "react";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Doughnut } from "react-chartjs-2";
ChartJS.register(ArcElement, Tooltip, Legend);
export interface SensorTypeCount {
		"name": string ,
		"count": number
	
}

export interface DateTypeCount {
		"date": string ,
		"count": number
	
}


export const ChartComponentForWorker = ({
  dataForPie,
}: {
  dataForPie: SensorTypeCount[];
}) => {
const getRandomColor = () => {
  const r = Math.floor(Math.random() * 200);
  const g = Math.floor(Math.random() * 200);
  const b = Math.floor(Math.random() * 200);
  return `rgba(${r}, ${g}, ${b}, 0.7)`;
};
   const backgroundColors = dataForPie.map(() => getRandomColor());
  const borderColors = backgroundColors.map((color) =>
    color.replace("0.7", "1")
  );
  const labels = dataForPie.map((g) => g.name);
  const values = dataForPie.map((g) => g.count);

  const data = {
    labels,
    datasets: [
      {
        data: values,
        backgroundColor:  backgroundColors,
        borderColor: borderColors,
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    cutout: "60%",


    plugins: {
      legend: {
        position: "bottom" as const,
      },
    },
  };

  return <Doughnut data={data} options={options} />;
};
