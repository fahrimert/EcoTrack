import MainPageChartComponentOfManager from "./components/MainPageChartComponentOfManager";
import { cookies } from "next/headers";

const page = async () => {
  const session = cookies().get("session");
  const responsegetWorkerStats = await fetch(
    `http://localhost:8080/manager/getSuperVizorTasks`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${session?.value}`,
        "Content-Type": "application/json",
      },
    }
  );

  const getSupervizorTasks = await responsegetWorkerStats.json();

  return (
    <>
      <div className=" h-fit w-full items-center justify-center flex pt-[10px]">
        <MainPageChartComponentOfManager
          session={session}
          getSupervizorTasks={getSupervizorTasks}
        />
      </div>
    </>
  );
};

export default page;
