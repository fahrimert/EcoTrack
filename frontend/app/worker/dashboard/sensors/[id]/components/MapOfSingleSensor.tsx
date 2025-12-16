"use client"
import React, { useCallback, useContext, useEffect, useState } from 'react'
import { DirectionsRenderer, GoogleMap, MarkerF, OverlayView, useJsApiLoader } from '@react-google-maps/api'
import axios from 'axios'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import { SourceContext } from '@/context/SourceContext'
import { Wrapper } from '@googlemaps/react-wrapper'
import { SensorSolvingSensorDto } from '@/app/worker/types/types'
import { UserLocationDTO } from '@/app/sharedTypes'



const MapOfSingleSensor = ({userLocation , initialData } : {userLocation : UserLocationDTO, initialData: SensorSolvingSensorDto   }   ) => {
  const [data, setData] = useState({ latitude: 39.9334, longitude: 32.8597 });
  const { source ,setSource} = useContext(SourceContext);
  
  const [isApiLoaded, setIsApiLoaded] = useState(false)

  const [directionRoutePoints,setDirectionRoutePoints] = useState([])

  useEffect(() => {
    if (typeof window !== 'undefined' && window.google && window.google.maps) {
      setIsApiLoaded(true)
    } else {
      const checkApi = setInterval(() => {
        if (typeof window !== 'undefined' && window.google && window.google.maps) {
          setIsApiLoaded(true)
          clearInterval(checkApi)
        }
      }, 100)
      
      return () => clearInterval(checkApi)
    }
  }, [])


  const [mapKey, setMapKey] = useState(0);


  const [centerData, setCenterData] = useState({ latitude: initialData?.latitude, longitude: initialData.longitude });
    
      useEffect(() => {
        if (source) {
          setCenterData({latitude:source.lat, longitude:source.lng})
          setMapKey(prev => prev + 1);

        }
      
      },[source])

      const onUnmount = useCallback(() => setMap(null), []);
      const [map, setMap] = useState<google.maps.Map | null>(null);
    
    


      const directionRoute = useCallback(() => {
        if (!isApiLoaded || !window.google || !source || !initialData) return
    
        try {
          const DirectionService = new window.google.maps.DirectionsService()
          DirectionService.route({
            origin: { lat: source.lng, lng: source.lat },
            destination: { 
              lat: initialData.latitude, 
              lng: initialData.longitude 
            },
            travelMode: window.google.maps.TravelMode.DRIVING
          }, (result, status) => {
            if (status === window.google.maps.DirectionsStatus.OK) {
              setDirectionRoutePoints(result)
            } else {
              console.error('Yönlendirme hatası:', status)
            }
          })
        } catch (error) {
          console.log(error.message);
          console.error("DirectionsService oluşturulamadı:", error)
        }
      }, [isApiLoaded, source, initialData])
    
      useEffect(() => {
        if (isApiLoaded &&   source.lat !== null && source.lng !== null && initialData?.latitude !== null && initialData?.longitude !== null) {
          {
            directionRoute()
          }
    
        }
      
      },[isApiLoaded,source, initialData])

      if (!isApiLoaded) {
        return <div>Harita yükleniyor...</div>
      }
    
      return (
        <div>
          <Wrapper apiKey='AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E' >

                 <GoogleMap
                 mapContainerStyle={{ width: '100%', height: '700px', position: 'relative' }}
                 center={ {lat:centerData.latitude,lng:centerData.longitude}}
                   zoom={11}
                   key={mapKey}
         
                   onUnmount={onUnmount}
                 >
                         <MarkerF 
                         position={{lat:data.latitude, lng:data.longitude}}
                       icon={{url:'/images.png',scaledSize:{
                         width:30,
                         height:30
                       }}}
                     
                   />
                   <OverlayView 
                         position={{lat:data.latitude, lng:data.longitude}}
                         mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}
                       >
                                   <div className=' w-fit h-fit bg-white '>
         
                      <p className=' text-[16px] text-black'> Ahmet</p>
                      </div>
                     </OverlayView>
                
                 <DirectionsRenderer
                  directions={directionRoutePoints}
                  options={{
                   suppressMarkers:true
                  }}
                 >
         
         
                 </DirectionsRenderer>
                     <MarkerF 
                             position={{lat:initialData?.latitude, lng:initialData?.longitude}}
                         
                             
                           />
                     
                    
                 
                 </GoogleMap>
          
                 </Wrapper>

 
      
        
        </div>

      ) 
}

export default MapOfSingleSensor
