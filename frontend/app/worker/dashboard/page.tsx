import { cookies } from "next/headers";
import React from "react";
import SensorsAndMap from "../../components/SensorsAndMap";

const page = async () => {
  const session = cookies().get("session");

  return (
    <>
      <div className=" h-fit w-full">
        <SensorsAndMap session={session} />
      </div>
    </>
  );
};

export default page;
