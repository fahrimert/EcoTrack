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
import { SensorList } from './SensorList'
import { TaskSensorWithTask } from './SensorsAndMap'
import { DifferentUserProfileType } from '@/app/sharedTypes'



const GoogleMapComponent = ({session,sensorListData,userProfile,taskSensorListData} : {session : RequestCookie, sensorListData:SensorList[] | undefined,userProfile : DifferentUserProfileType | undefined, taskSensorListData: TaskSensorWithTask[]}) => {
  const [centerStateData, setCenterStateData] = useState({ latitude: 39.9334, longitude: 32.8597 });
  
  //source and destination context states
  const { source ,setSource} = useContext(SourceContext);
  const { destination ,setDestination} = useContext(DestinationContext);
  const [directionRoutePoints,setDirectionRoutePoints] = useState([])

  //useeffect for putting the current user location on map center and setting the source value users location 
  useEffect(() => {
    axios.get("http://localhost:8080/user/getUserLocation", {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => {setSource({lat:res.data.latitude, lng: res.data.longitude} )  
    setCenterStateData(res.data)}
  )
    .catch((err) => console.log(err));
  }, []);

  /* centerState data for */
  const [centerData, setCenterData] = useState({ latitude: destination.lat, longitude: destination.lng });
  useEffect(() => {
    if (source) {
      setCenterData({latitude:source.lat, longitude:source.lng})
      setMapKey(prev => prev + 1); // Key'i değiştirerek bileşeni yeniden yükle
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
      <Wrapper apiKey="AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E" key={mapKey}>
        <GoogleMap
        mapContainerStyle={{ width: '100%', height: '700px', position: 'relative' }}
        center={ {lat:centerData.latitude,lng:centerData.longitude}}
          zoom={11}
          key={mapKey} // Key değiştiğinde bileşen yeniden yüklenir

          onUnmount={onUnmount}
        >
         
      
                {/* burada sessiondaki userin datasını alıp onu */}
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
    
          {taskSensorListData.length !== 0  ? 
            taskSensorListData?.map((a,b) => (

              <>
       <MarkerF 
       key={b}
            position={{lat:a.taskSensors.latitude, lng:a.taskSensors.longitude}}
        
            
          />
          <OverlayViewF 
          position={{lat:a.taskSensors.latitude, lng:a.taskSensors.longitude}}
          mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}
        >
                    <div className={cn(`w-fit h-fit  text-nowrap inline-block p-[5px] rounded-[3px]`)}
                      style={{ backgroundColor: a.taskSensors.color_code }}

                    >

                     <MdOutlineSensors  color={a.taskSensors.color_code}/>
       <h2 className=' w-fit h-fit text-[12px] text-white '> {a.taskSensors.sensorName}</h2>
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
      
              </Wrapper>
        
        </div>

      ) 
}

export default GoogleMapComponent
