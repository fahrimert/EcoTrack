"use client";
import { cn } from "@/lib/utils";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import PerformanceLineChart from "./PerformanceLineChart";
import PerformanceChartComponent from "./PerformanceChartComponent";
import PerformanceLeaderTableComponent from "./PerformanceLeaderTableComponent";
import PerformanceHeatmapComponent from "./PerformanceHeatmapComponent";
import PerformanceCalendarComponent from "./CalendarHeatmapComponents/PerformanceCalendarComponent";

const WorkerPerformanceAnalysis = ({
  session,
}: {
  session: RequestCookie | undefined;
}) => {
  return (
    <>
      <div
        className={cn(
          `relative w-full h-fit flex flex-col   items-center  justify-center  gap-[24px] p-0 `
        )}
      >
       <PerformanceLineChart session={session} />
        
        <PerformanceChartComponent session={session} /> 
        <div className="w-fit">

        <PerformanceCalendarComponent session={session} />
        </div>

        <PerformanceHeatmapComponent session={session} />

        <PerformanceLeaderTableComponent session={session} />
      </div>
    </>
  );
};

export default WorkerPerformanceAnalysis;
