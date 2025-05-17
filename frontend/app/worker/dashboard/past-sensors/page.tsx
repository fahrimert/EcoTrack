import React from "react";
import Heading from "./components/Heading";
import PastSensorWrapper from "./components/PastSensorWrapper";
import { cookies } from "next/headers";

const page = () => {
  const session = cookies().get("session")?.value;

  return (
    <>
      <div className="flex flex-col h-fit w-full">
        <Heading
          title={"Geçmişte Uğraştığınız Sensörler"}
          description={
            "Çözdüğünüz veya Çözemediğiniz Tüm Geçmişteki Sensörler Bu Sayfada gözükür "
          }
        />
        <PastSensorWrapper session={session} />
      </div>
    </>
  );
};

export default page;
