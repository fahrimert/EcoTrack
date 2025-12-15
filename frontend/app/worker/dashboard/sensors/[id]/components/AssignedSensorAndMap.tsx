  "use client"
  import React, { useState } from "react";
  import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
  import { SourceContext } from "@/context/SourceContext";

  import AssignedSensorForm from "./AssignedSensorForm";
  import MapOfSingleSensor from "./MapOfSingleSensor";
  import SearchUserLocation from "../../../components/SearchComponents/SearchUserLocation";
  import { SensorSolvingSensorDto } from "@/app/worker/types/types";
  import { UserLocationDTO } from "@/app/sharedTypes";




  const AssignedSensorAndMap = ({initialData ,  userLocation  } : {initialData: SensorSolvingSensorDto,  userLocation : UserLocationDTO}) => {
    const [source,setSource] = useState({
      lat:39.9334,
      lng: 32.8597
    })

    
    return (
      <>
        <SourceContext.Provider value={{source,setSource}}>
  <div className="w-full max-w-[1920px] mx-auto p-4 md:p-6 lg:p-8">

    
  <div className="w-full grid grid-cols-1 xl:grid-cols-12 gap-6 items-start">
      <div className=" w-full order-2 xl:order-1 xl:col-span-7 flex flex-col gap-6">
              <div className="bg-white  rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
                  <AssignedSensorForm initialData={initialData} />
              </div>
            </div>

      <div className="order-1 xl:order-2 xl:col-span-5 w-full">
              <div className="sticky top-6 flex flex-col gap-4">
                
                <div className="relative w-full h-[400px] xl:h-[600px] rounded-2xl overflow-hidden shadow-lg border border-gray-200 bg-white">
                  
                  <div className="absolute top-4 left-4 right-4 z-10 sm:w-[350px]">
                    <SearchUserLocation />
                  </div>

                  <MapOfSingleSensor 
                    userLocation={userLocation} 
                    initialData={initialData} 
                  />
                </div>

                <div className="bg-blue-50 border border-blue-100 p-4 rounded-xl text-sm text-blue-800">
                  <p>📍 <strong>Konum İpucu:</strong> Arıza kaydını tamamlamadan önce sensörün fiziksel konumunu haritadan doğruladığınızdan emin olun.</p>
                </div>

              </div>
            </div>
        </div>

      </div>
        </SourceContext.Provider>
      </>
    );
  };

  export default AssignedSensorAndMap;
