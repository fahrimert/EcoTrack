"use client"
import React, { useCallback, useContext, useEffect, useMemo, useRef, useState } from 'react'
import { DirectionsRenderer, GoogleMap, MarkerF, OverlayView, OverlayViewF, useJsApiLoader } from '@react-google-maps/api'
import axios from 'axios'
import { cookies } from 'next/headers'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import { SourceContext } from '@/context/SourceContext'
import { DestinationContext } from '@/context/DestinationContext'
import { Wrapper } from '@googlemaps/react-wrapper'
import { AdvancedMarker, ControlPosition, MapControl, useAdvancedMarkerRef, useMap, useMapsLibrary } from '@vis.gl/react-google-maps'
import { MdOutlineSensors } from 'react-icons/md'
import { cn } from '@/lib/utils'
import Image from 'next/image'
import { SensorList } from '@/app/components/SensorComponents/SensorList'



const GoogleMapComponentOfAllUsers = ({usersAndTheirSensors,session,sensorListData} : { usersAndTheirSensors: {
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
}[],  session: RequestCookie | undefined , sensorListData : SensorList[]}) => {
  const [data, setData] = useState({ latitude: 39.8324, longitude: 32.8577 });
  const { source ,setSource} = useContext(SourceContext);

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
      const [map, setMap] = useState<google.maps.Map | null>(null);
      
 
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
                 
                   {usersAndTheirSensors.map((g) => (
                    <>
     <MarkerF 
     position={{lat:g.latitude, lng:g.longitude}}
   icon={{url:'/images.png',scaledSize:{
     width:source.lng == g.longitude ? 50 :30  ,
     height:source.lat == g.latitude ? 50 :30 
   }}}
 
/>

<OverlayView 
position={{lat:g.latitude, lng:g.longitude}}
mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}
>


<div className=' w-fit h-fit bg-white '>
        
        <p className=' text-[16px] text-black'> {g.name}</p>
        </div>
</OverlayView>

</>

                   ))}
                   
                        {/* burada sessiondaki userin datasını alıp onu */}
                   
             
               
            {sensorListData?.map((a,b) => (
        
                      <>
               <MarkerF 
               key={b}
               icon={{url:'/smallloc.png',scaledSize:{
                width:source.lng == a.longitude ? 50 :30  ,
                height:source.lat == a.latitude ? 50 :30 
              }}}
                    position={{lat:a.latitude, lng:a.longitude}}
                
                    
                  />
                  <OverlayViewF 
                  position={{lat:a.latitude, lng:a.longitude}}
                  mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}
                >
                            <div className={cn(`w-fit h-fit  text-nowrap inline-block p-[5px] rounded-[3px]`)}
                              style={{ backgroundColor: a.color_code }}
        
                            >
        
                             <MdOutlineSensors  color={a.color_code}/>
               <h2 className=' w-fit h-fit text-[12px] text-white '> {a.sensorName}</h2>
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
