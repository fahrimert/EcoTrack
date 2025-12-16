import React from 'react'
import { userService } from '@/app/services/userService';
import { sensorService } from '@/app/services/sensorService';
import CrewJobTrackPageSensorsAndTheirLocations from './components/CrewJobTrackPageSensorsAndTheirLocations';

const page = async ({params} : {params:{id:string} }) => {
  
       const [ userProfile, workerSensors ,crewJobTrackPageSensorsAndTheirLocations ] = await Promise.all([
        userService.getDashboardData(),
        sensorService.getWorkerDashboardSensors(),
        userService.getCrewJobTrackPageSensorsAndTheirLocations()
      ]);




  return (
    <>
  <div className="w-full min-h-screen bg-gray-50/50">
      <CrewJobTrackPageSensorsAndTheirLocations 
        crewJobTrackPageSensorsAndTheirLocations={crewJobTrackPageSensorsAndTheirLocations.data || []} 
        workerSensors={workerSensors || []} 
        userProfile={userProfile.userProfile} 
      />
    </div>
</>  
)
}

export default page 