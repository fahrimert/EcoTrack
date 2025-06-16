"use client";
import React from "react";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Doughnut } from "react-chartjs-2";
ChartJS.register(ArcElement, Tooltip, Legend);

interface SensorStatusCounts {
  ACTIVE: number;
  IN_REPAIR: number;
  FAULTY: number;
}

export const ChartComponent = ({
  responseOfDoughbut,
}: {
  responseOfDoughbut: SensorStatusCounts;
}) => {
  const labels = Object.keys(responseOfDoughbut);
  const values = Object.values(responseOfDoughbut);

  const colors = {
    ACTIVE: "#4CAF50",     
    IN_REPAIR: "#FF9800",   
    FAULTY: "#F44336",     
  };

  const backgroundColors = labels.map((label) => colors[label as keyof typeof colors]);

  const data = {
    labels,
    datasets: [
      {
        data: values,
        backgroundColor: backgroundColors,
        borderColor: backgroundColors,
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
