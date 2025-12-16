import { UserProfileDTO } from "@/app/sharedTypes";
import { CrewJobsUserAndSessionSensorDTO } from "@/app/worker/types/types";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { SourceContext } from "@/context/SourceContext";
import { cn } from "@/lib/utils";
import Image from "next/image";
import { useContext} from "react";
import { MdLocationOn, MdPerson } from "react-icons/md";

const SingleUserAndTheirSensor = ({
  userProfile,
  crew,
}: {
  userProfile:UserProfileDTO
  crew: CrewJobsUserAndSessionSensorDTO
}) => {

   const { source, setSource } = useContext(SourceContext);
  console.log("crewss",crew);

  const focusOnWorker = () => {
    setSource({ lat: crew.workerLatitude, lng: crew.workerLongitude });
  };

  const focusOnSensor = () => {
    setSource({ lat: crew.sensorLatitude, lng: crew.sensorLongitude });
  };

  return (
   <div className="bg-white border border-gray-100 rounded-xl p-3 shadow-sm hover:shadow-md transition-all flex flex-row gap-3 items-center">
      
      {/* Avatar */}
      <div className="relative w-14 h-14 rounded-full overflow-hidden border-2 border-blue-100 shrink-0">
        <Image
          src="/indir.jpg" // Varsa user avatarı
          alt={crew.workerName}
          fill
          className="object-cover"
        />
      </div>

      {/* Bilgiler */}
      <div className="flex-1 min-w-0">
        <h3 className="text-sm font-bold text-gray-800 truncate flex items-center gap-2">
            {crew.workerName}
            <Badge variant="secondary" className="text-[10px] px-1 h-5">{crew.isOnline ? "Aktif" : "Aktif Değil"}</Badge>
        </h3>
        
        <div className="flex items-center gap-1 text-xs text-gray-500 mt-1">
            <MdLocationOn className="text-emerald-500" />
            <span className="truncate">{crew.sensorName}</span>
        </div>
        
        <div className="flex items-center gap-2 mt-2">
            <Button
                variant="outline" 
                size="sm" 
                className="h-7 text-[10px] px-2"
                onClick={focusOnWorker}
            >
                <MdPerson className="mr-1"/> İşçiye Git
            </Button>
            <Button 
                variant="ghost" 
                size="sm" 
                className="h-7 text-[10px] px-2 text-gray-500"
                onClick={focusOnSensor}
            >
                Sensöre Git
            </Button>
        </div>
      </div>
    </div>
  );
};

export default SingleUserAndTheirSensor;
