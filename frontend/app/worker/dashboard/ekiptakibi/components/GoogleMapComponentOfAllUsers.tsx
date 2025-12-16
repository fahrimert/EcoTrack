"use client"
import React, { useCallback, useContext, useEffect,  useState } from 'react'
import { GoogleMap, MarkerF, OverlayView, OverlayViewF } from '@react-google-maps/api'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import { SourceContext } from '@/context/SourceContext'
import { Wrapper } from '@googlemaps/react-wrapper'
import { MdOutlineSensors } from 'react-icons/md'
import { cn } from '@/lib/utils'
import Image from 'next/image'
import { SensorList } from '../../components/SensorComponents/SensorList'
import { CrewJobsUserAndSessionSensorDTO, WorkerDashboardSensorDto } from '@/app/worker/types/types'

interface MapProps {
  crewJobTrackPageSensorsAndTheirLocations: CrewJobsUserAndSessionSensorDTO[];
  workerSensors: WorkerDashboardSensorDto[];
}

const GoogleMapComponentOfAllUsers = ({crewJobTrackPageSensorsAndTheirLocations,workerSensors} :  MapProps) => {
  const [data, setData] = useState({ latitude: 39.8324, longitude: 32.8577 });
  const { source ,setSource} = useContext(SourceContext);
  console.log("CREWJOBTRACKPAGESENSORSand their locations", crewJobTrackPageSensorsAndTheirLocations);
    const [centerData, setCenterData] = useState({ latitude: data.latitude, longitude: data.longitude });
    useEffect(() => {
      if (source) {
        setCenterData({latitude:source.lat, longitude:source.lng})
        setMapKey(prev => prev + 1); // Key'i değiştirerek bileşeni yeniden yükle
  
      }
    
    },[source])




  const [mapKey, setMapKey] = useState(0);





  const ref = React.useRef(null)



  

  //centeri source datası yap 
  //bizim marker f ve overliev userin datası 
    

      const onUnmount = useCallback(() => setMap(null), []);
      const [map, setMap] = useState(null);
      
 
      return (
        <div className='w-full flex flex-row'>

        
             <Wrapper apiKey="AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E" key={mapKey}>
                <GoogleMap
                mapContainerStyle={{ width: '100%', height: '700px', position: 'relative' }}
                center={ {lat:centerData.latitude,lng:centerData.longitude}}
                  zoom={11}
                  key={mapKey} // Key değiştiğinde bileşen yeniden yüklenir
        
                  onUnmount={onUnmount}
                >
                 
                   {crewJobTrackPageSensorsAndTheirLocations.map((g) => (
                    <>
   <MarkerF
            position={{ lat: g.workerLatitude, lng: g.workerLongitude }}
            icon={{
              url: '/images.png',
              scaledSize: {
                    width:30,
                    height:30
                  }
            }}
            zIndex={100} // İşçiler sensörlerin üstünde görünsün
          />
          <OverlayViewF
            position={{ lat: g.workerLatitude, lng: g.workerLongitude }}
            mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}
          >
            <div className="absolute -top-14 -left-10 bg-white px-2 py-1 rounded-lg shadow-md border border-blue-500 whitespace-nowrap z-50">
              <p className="text-xs font-bold text-blue-800">{g.workerName}</p>
              <p className="text-[10px] text-gray-500 truncate max-w-[100px]">{g.sensorName} üzerinde</p>
            </div>
          </OverlayViewF>

<OverlayView 
position={{lat:g.workerLatitude, lng:g.workerLongitude}}
mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}
>


<div className=' w-fit h-fit bg-white '>
        
        <p className=' text-[16px] text-black'> {g.sensorName}</p>
        </div>
</OverlayView>

</>

                   ))}
                   
                        {/* burada sessiondaki userin datasını alıp onu */}
                   
             
               
            {workerSensors?.map((a,b) => (
        
                      <>
<MarkerF
            position={{ lat: a.latitude, lng: a.longitude }}
            icon={{
              url: '/smallloc.png', // Küçük sensör ikonu
              scaledSize:{
                    width:25,
                    height:25
                  }
            }}
            opacity={0.7} // İşçilere odaklanmak için sensörleri biraz soluk yaptık
          />
          <OverlayViewF
            position={{ lat: a.latitude, lng: a.longitude }}
            mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}
          >
            <div 
                className={cn("px-1 py-0.5 rounded text-[9px] text-white font-medium shadow-sm")}
                style={{ backgroundColor: a.colorCode || '#999' }}
            >
                <MdOutlineSensors className="inline mr-1" />
                {a.sensorName}
            </div>
          </OverlayViewF>
              </>
        
                  ))}
        
                  {/* Child components, such as markers, info windows, etc. */}
                
        
                </GoogleMap>
              
                      </Wrapper>
        </div>

      ) 
}

export default GoogleMapComponentOfAllUsers
