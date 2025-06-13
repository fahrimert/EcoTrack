import React from "react";
import Heading from "./components/Heading";
import { cookies } from "next/headers";
import WorkerPastSensorWrapper from "./components/WorkerPastSensorWrapper";

const page = () => {
  const session = cookies().get("session");

  return (
    <>
      <div className="flex flex-col h-fit w-full pt-[10px]">
        <Heading
          title={"İşçilerin Görev Harici Sensör Çözme Geçmişi"}
          description={
            "Verdiğiniz Görevleri Çözme Değil İşçilerin Kendi Görev Dışı Çözdükleri Sensörleri Gösterir"
          }
        />
        <WorkerPastSensorWrapper session={session} />
      </div>
    </>
  );
};

export default page;
