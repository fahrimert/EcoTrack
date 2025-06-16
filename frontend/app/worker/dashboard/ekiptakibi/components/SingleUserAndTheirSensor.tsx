import { SourceContext } from "@/context/SourceContext";
import { cn } from "@/lib/utils";
import Image from "next/image";
import { useContext} from "react";

const SingleUserAndTheirSensor = ({
  sensors,
}: {
  sensors: {
    id: number;
    name: string;
    latitude: number;
    longitude: number;
    sensorlatitude: number;
    sensorlongitude: number;
    sensor: {
      id: number;
      sensorName: string;
      status: string;
      installationDate: string;
    };
  };
}) => {

  const { source, setSource } = useContext(SourceContext);
  return (
    <div
      className={cn(
        `flex flex-row   w-full h-fit  justify-center items-center rounded-[30px]  p-[10px] gap-[10px] `
      )}
      

    >
      {/* usera image yükleyebilsek buraya userin avatar imagesi gelebilir  */}
      <Image
            onMouseEnter={() =>
    
              ( setSource({
                 lat: sensors.latitude,
                 lng: sensors.longitude,
               }))
             }
        src={"/indir.jpg"}
        alt="232"
        className={cn(
          ` w-[150px] h-[100px]  object-fit  rounded-[30px] cursor-pointer  `
        )}
        width={50}
        height={100}
      />
      <div 
            onMouseEnter={() =>
    
              ( setSource({
                 lat: sensors.sensorlongitude,
                 lng: sensors.sensorlatitude,
               }))
             }
      className=" bg-[#f1f0ee] rounded-[30px] flex flex-col w-fit h-fit p-[10px]   justify-start items-start  shadow-lg  hover:scale-105 duration-300 cursor-pointer ; ">
        <div className=" bg-[#c0ccc9]  w-full p-[5px] rounded-[5px] mb-[5px]">
          <div className=" h-full w-full justify-start items-start flex flex-row p-[5px] gap-[5px]  ">
            <h2 className="w-full text-[16px] font-normal     text-white ">
              İşçi İsmi:
            </h2>
            <h2 className="w-full text-[16px] font-normal   text-white">
              {sensors.name}{" "}
            </h2>
          </div>
          <div className=" h-full w-full justify-center items-center flex flex-row p-[5px] gap-[5px]  ">
            <h2 className="w-full h-fit text-[16px] font-normal     text-white ">
              Sensor idsi:
            </h2>
            <h2 className="w-full h-fit  text-[13px] font-normal   text-white ">
              {sensors.sensor.id}{" "}
            </h2>
          </div>

          <div className=" h-full w-full justify-center items-center flex flex-row p-[5px] gap-[5px]  ">
            <h2 className="w-full h-fit text-[16px] font-normal     text-white ">
              Sensor ismi:
            </h2>
            <h2 className="w-full h-fit  text-[13px] font-normal   text-white ">
              {sensors.sensor.sensorName}{" "}
            </h2>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SingleUserAndTheirSensor;
