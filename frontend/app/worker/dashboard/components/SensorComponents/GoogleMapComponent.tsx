"use client"
import React, { useCallback, useContext, useEffect, useState } from 'react'
import { DirectionsRenderer, GoogleMap, MarkerF, OverlayView, OverlayViewF} from '@react-google-maps/api'
import axios from 'axios'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import { SourceContext } from '@/context/SourceContext'
import { DestinationContext } from '@/context/DestinationContext'
import { Wrapper } from '@googlemaps/react-wrapper'
import { MdOutlineSensors } from 'react-icons/md'
import { cn } from '@/lib/utils'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { UserLocationDTO, UserProfileDTO } from '@/app/sharedTypes'
import { WorkerDashboardSensorDto, WorkerDashboardTaskSensorWithTaskDto } from '@/app/worker/types/types'

const containerStyle = {
  width: '100%',
  height: '100%', 
};
const mapOptions = {
  disableDefaultUI: false,
  zoomControl: true,
  streetViewControl: false,
  mapTypeControl: false,
};

interface MapProps {
  sensorListData: WorkerDashboardSensorDto[] | undefined;
  userProfile: UserProfileDTO | undefined;
  sensorListFromTasksOfSingleUser: WorkerDashboardTaskSensorWithTaskDto[];
  userLocation: UserLocationDTO;
}
//Component çalışıyor çalışanı bozmayacağım 
const GoogleMapComponent = ({sensorListData,userProfile,sensorListFromTasksOfSingleUser,userLocation} : MapProps) => {
  const [centerStateData, setCenterStateData] = useState({ latitude: 39.9334, longitude: 32.8597 });
  
  //source and destination context states
  const { source ,setSource} = useContext(SourceContext);
  const { destination ,setDestination} = useContext(DestinationContext);
  const [directionRoutePoints,setDirectionRoutePoints] = useState([])
  const libraries: ("places" | "drawing" | "geometry" | "localContext" | "visualization")[] = ["places"];
  //useeffect for putting the current user location on map center and setting the source value users location 
  useEffect(() => {
setSource({lat:userLocation.latitude, lng: userLocation.longitude} )  
    setCenterStateData({latitude:userLocation.latitude,longitude:userLocation.longitude})}
  , []);

  /* centerState data for */
  const [centerData, setCenterData] = useState({ latitude: destination.lat, longitude: destination.lng });
  useEffect(() => {
    if (source) {
      setCenterData({latitude:source.lat, longitude:source.lng})
      setMapKey(prev => prev + 1); 
    }
  },[source])

  const [mapKey, setMapKey] = useState(0);


  //centeri source datası yap 
  //bizim marker f ve overliev userin datası 
    
  

      const onUnmount = useCallback(() => setMap(null), []);
      const [map, setMap] = useState<google.maps.Map | null>(null);
      
      

      const directionRoute= () => {
        const DirectionService= new google.maps.DirectionsService()


        DirectionService.route({
          origin:{lat:source.lat,lng:source.lng},
          destination:{lat:destination.lat,lng:destination.lng},
          travelMode:google.maps.TravelMode.DRIVING
        },(result,status) => {
          if (status=== google.maps.DirectionsStatus.OK) {
            {
              setDirectionRoutePoints(result)
            }
           
          } else{
            console.log('Error');
          }
          
        })
      }
      useEffect(() => {
        if (source.lat !== null && source.lng !== null && destination.lat !== null && destination.lng !== null) {
          {
            directionRoute()
          }
    
        }
      
      },[source,destination])

      return (
        <div>
      <Wrapper 
                    apiKey={process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY}
      
      key={mapKey} 
      libraries={libraries}>
        <div className="w-full h-full relative">
        <GoogleMap
        
        mapContainerStyle={{ width: '100%', height: '700px', position: 'relative' }}
        center={ {lat:centerData.latitude,lng:centerData.longitude}}
          zoom={11}
          key={mapKey} 

          onUnmount={onUnmount}
        >
         
      
                <MarkerF 
                position={{lat:centerStateData.latitude, lng:centerStateData.longitude}}
              icon={{url:'/images.png',scaledSize:{
                width:30,
                height:30
              }}}
            
          />
          <OverlayView 
                position={{lat:centerStateData.latitude, lng:centerStateData.longitude}}
                mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}
              >

             <Avatar className=' w-fit h-fit text-nowrap p-[5px] rounded-[15px]  bg-white '>

             <AvatarFallback>{userProfile?.firstName} {userProfile?.surName} adlı işçi </AvatarFallback>
             </Avatar>
            </OverlayView>
       
          {/* buralarda sensörleri listeleyip tek tek onların locasyonlarını koyucam  */}
    
          {sensorListFromTasksOfSingleUser.length !== 0  ? 
            sensorListFromTasksOfSingleUser?.map((a,b) => (

              <>
       <MarkerF 
       key={b}
            position={{lat:a.latitude, lng:a.longitude}}
        
            
          />
          <OverlayViewF 
          position={{lat:a.latitude, lng:a.longitude}}
          mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}
        >
                    <div className={cn(`w-fit h-fit  text-nowrap inline-block p-[5px] rounded-[3px]`)}
                      style={{ backgroundColor: a.colorCode }}

                    >

                     <MdOutlineSensors  color={a.colorCode}/>
       <h2 className=' w-fit h-fit text-[12px] text-white '> {a.sensorName}</h2>
       </div>
      </OverlayViewF>
      </>

          ))
          
          : 
          sensorListData?.map((a,b) => (

              <>
       <MarkerF 
       key={b}
            position={{lat:a.latitude, lng:a.longitude}}
        
            
          />
          <OverlayViewF 
          position={{lat:a.latitude, lng:a.longitude}}
          mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}
        >
                    <div className={cn(`w-fit h-fit  text-nowrap inline-block p-[5px] rounded-[3px]`)}
                      style={{ backgroundColor: a.colorCode }}

                    >

                     <MdOutlineSensors  color={a.colorCode}/>
       <h2 className=' w-fit h-fit text-[12px] text-white '> {a.sensorName}</h2>
       </div>
      </OverlayViewF>
      </>

          ))}
     

          {/* Child components, such as markers, info windows, etc. */}
        <DirectionsRenderer
         directions={directionRoutePoints}
         options={{
          suppressMarkers:true
         }}
        >


        </DirectionsRenderer>
        </GoogleMap>
        </div>
      
              </Wrapper>
        
        </div>

      ) 
}

export default GoogleMapComponent
