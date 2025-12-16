  "use client"
  import { AddressComponent } from "@googlemaps/google-maps-services-js";
import { PastSensorDetailDto } from "@/app/worker/types/types";
import { UserProfileDTO } from "@/app/sharedTypes";
import SensorHeaderCard from "./SensorHeaderCard";
import SensorDetailsCard from "./SensorDetailsCard";
import ProcessDetailsCard from "./SolvingProcessDetailCard";
import SolvingProcessEvidenceGallery from "./SolvingProcessEvidenceGallery";

  const LeftStack = ({userProfile,addressComponents,initialData} : {
  userProfile : UserProfileDTO
  addressComponents: AddressComponent[]
    
    initialData : PastSensorDetailDto,

 
  } , ) => {
    

 return (
    <div className="w-full max-w-4xl mx-auto space-y-6">
      
      <SensorHeaderCard data={initialData} />

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <SensorDetailsCard data={initialData} />
        
        <ProcessDetailsCard data={initialData} userProfile={userProfile} />
      </div>
      <SolvingProcessEvidenceGallery images={initialData.evidenceImages} />
      
    </div>
  );
  }

  export default LeftStack