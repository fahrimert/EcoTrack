"use client";

import React from "react";
import CalendarHeatmapForWorker from "./Calendar";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { DateTypeCount } from "@/app/supervisor/superVizorDataTypes/types";

const WrapperForContext = ({
  session,
  dataForCalendar,
}: {
  session: RequestCookie | undefined;
  dataForCalendar: DateTypeCount[];
}) => {
  return (
    <>
      <div className=" flex flex-row w-fit h-fit max-xl:flex  max-xl:flex-col items-center">
        <div className=" w-fit h-fit max-xl:w-fit">
          <CalendarHeatmapForWorker
            session={session}
            dataForCalendar={dataForCalendar}
          />
        </div>
      </div>
    </>
  );
};

export default WrapperForContext;
