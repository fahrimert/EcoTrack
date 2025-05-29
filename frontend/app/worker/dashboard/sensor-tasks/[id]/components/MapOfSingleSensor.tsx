"use client"
import React, { useCallback, useContext, useEffect, useState } from 'react'
import { DirectionsRenderer, GoogleMap, MarkerF, OverlayView, useJsApiLoader } from '@react-google-maps/api'
import axios from 'axios'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import { SourceContext } from '@/context/SourceContext'
import { Wrapper } from '@googlemaps/react-wrapper'



const MapOfSingleSensor = ({session , initialData } : {session : RequestCookie, initialData: 
 {
  id: number;
  taskSensors: {
    id: number;
    sensorName: string;
    status:string;
    color_code: string;
    latitude: number;
    longitude: number;
    currentSensorSession: {
      id: number;
      sensorName: string;
      displayName: string;
      color_code: string;
      note: string | null;
      startTime: string;
      completedTime: string | null;
      latitude: number;
      longitude: number;
    };
  };
  superVizorDescription: string;
  superVizorDeadline: string; // ISO 8601 format
  assignedBy: {
    id: number;
    firstName: string;
    surName: string;
  };
  workerArriving: string | null;
  workerArrived: string | null;
  workerNote:string
}
   }   ) => {
  const [data, setData] = useState({ latitude: 39.9334, longitude: 32.8597 });
  const { source ,setSource} = useContext(SourceContext);
  
  //
  const [apiLoaded,setApiLoaded] = useState<google.maps.Map | null >(null)
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

  useEffect(() => {
     axios.get("http://localhost:8080/getUserLocation", {
      headers: { Authorization: `Bearer ${session.value}` },
      withCredentials: true,
    })
    .then((res) => {setSource({lat:res.data.latitude, lng: res.data.longitude} )  
  setData(res.data)}
  )
    .catch((err) => console.log(err));
  }, []);


  const [mapKey, setMapKey] = useState(0);

  //centeri source datası yap 
  //bizim marker f ve overliev userin datası 
  const [centerData, setCenterData] = useState({ latitude: initialData?.latitude, longitude: initialData?.longitude });
    
      useEffect(() => {
        if (source) {
          setCenterData({latitude:source.lat, longitude:source.lng})
          setMapKey(prev => prev + 1); // Key'i değiştirerek bileşeni yeniden yükle

        }
      
      },[source])

      const onUnmount = useCallback(() => setMap(null), []);
      const [map, setMap] = useState<google.maps.Map | null>(null);
    
    


      const directionRoute = useCallback(() => {
        if (!isApiLoaded || !window.google || !source || !initialData) return
    
        try {
          const DirectionService = new window.google.maps.DirectionsService()
          
          DirectionService.route({
            origin: { lat: source.lat, lng: source.lng },
            destination: { 
              lat: initialData.taskSensors.latitude, 
              lng: initialData.taskSensors.longitude 
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
          console.error("DirectionsService oluşturulamadı:", error)
        }
      }, [isApiLoaded, source, initialData])
    
      useEffect(() => {
        if (isApiLoaded &&   source.lat !== null && source.lng !== null && initialData?.taskSensors.latitude !== null && initialData?.taskSensors.longitude !== null) {
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
                   key={mapKey} // Key değiştiğinde bileşen yeniden yüklenir
         
                   onUnmount={onUnmount}
                 >
                  
         
               
                         {/* burada sessiondaki userin datasını alıp onu */}
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
                
                   {/* buralarda sensörleri listeleyip tek tek onların locasyonlarını koyucam  */}
              
         
                   {/* Child components, such as markers, info windows, etc. */}
                 <DirectionsRenderer
                  directions={directionRoutePoints}
                  options={{
                   suppressMarkers:true
                  }}
                 >
         
         
                 </DirectionsRenderer>
                     <MarkerF 
                             position={{lat:initialData?.taskSensors.latitude, lng:initialData?.taskSensors.latitude}}
                         
                             
                           />
                     
                    
                 
                 </GoogleMap>
          
                 </Wrapper>

 
      
        
        </div>

      ) 
}

export default MapOfSingleSensor
