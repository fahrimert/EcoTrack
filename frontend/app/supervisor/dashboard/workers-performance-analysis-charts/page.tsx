import { cookies } from "next/headers";
import React from "react";
import WorkerPerformanceAnalysis from "./PerformanceCharts/WorkerPerformanceAnalysis";

const page = async () => {
  const session = cookies().get("session");

  return (
    <>
      <div className=" h-fit w-full items-center justify-center flex">
        <WorkerPerformanceAnalysis session={session} />
      </div>
    </>
  );
};

export default page;
