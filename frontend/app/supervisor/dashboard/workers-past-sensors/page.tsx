import React from "react";
import Heading from "./components/Heading";
import PastSensorWrapper from "./components/WorkerPastSensorWrapper";
import { cookies } from "next/headers";
import WorkerPastSensorWrapper from "./components/WorkerPastSensorWrapper";

const page = () => {
  const session = cookies().get("session")?.value;

  return (
    <>
      <div className="flex flex-col h-fit w-full">
        <Heading
          title={"İşçilerin Görev Geçmişi"}
          description={
            "Tüm işçilerin görev geçmişini gösterir "
          }
        />
        <WorkerPastSensorWrapper session={session} />
      </div>
    </>
  );
};

export default page;
