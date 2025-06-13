"use client";

import React, { useState } from "react";
import Calendar from "./calendarComponentsForPdfSheet/Calendar";
import { DateContext } from "@/context/DateContext";
import PastSensorsForReportWrapper from "./PastSensorsForReportWrapper";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";

const WrapperForContext = ({
  session,
}: {
  session: RequestCookie | undefined;
}) => {
  const [date, setDate] = useState({
    sortDate: null,
  });
  return (
    <>
      <DateContext.Provider value={{ date, setDate }}>
        <div className="   w-fit h-fit flex flex-row items-center justify-center max-xl:flex max-xl:flex-col max-xl:h-fit">
          <div className=" w-full h-fit max-xl:w-fit">
            <Calendar session={session} />
          </div>
          <div className="w-full max-xl:w-full ">
            <PastSensorsForReportWrapper session={session} />
          </div>
        </div>
      </DateContext.Provider>
    </>
  );
};

export default WrapperForContext;
