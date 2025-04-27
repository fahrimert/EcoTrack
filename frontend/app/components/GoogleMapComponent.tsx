"use client"
import React, { useCallback, useContext, useEffect, useMemo, useRef, useState } from 'react'
import { GoogleMap, MarkerF, OverlayView, useJsApiLoader } from '@react-google-maps/api'
import axios from 'axios'
import { cookies } from 'next/headers'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import { SourceContext } from '@/context/SourceContext'
import { DestinationContext } from '@/context/DestinationContext'
import { Wrapper } from '@googlemaps/react-wrapper'
import { AdvancedMarker, ControlPosition, MapControl, useAdvancedMarkerRef, useMap, useMapsLibrary } from '@vis.gl/react-google-maps'
import InputItem from './InputItem'



const GoogleMapComponent = ({session} : {session : RequestCookie}) => {
  const [data, setData] = useState<google.maps.places.PlaceResult | null>({ latitude: 39.9334, longitude: 32.8597 });
  const { source } = useContext(SourceContext);
  const { destination } = useContext(DestinationContext);
  const [mapKey, setMapKey] = useState(0);


  console.log("Source information " + source.lat + "lng"  + source.lng);


  useEffect(() => {
    axios.get("http://localhost:8080/getUserLocation", {
      headers: { Authorization: `Bearer ${session?.value}` },
      withCredentials: true,
    })
    .then((res) => setData(res.data))
    .catch((err) => console.log(err));
  }, []);
  const ref = React.useRef(null)

  const center = useMemo(() => ({
    lat: data?.latitude,
    lng: data?.longitude,
  }), [data]);


 

      
  
    
      useEffect(() => {
        if (source) {
          setData({latitude:source.lat, longitude:source.lng})
          setMapKey(prev => prev + 1); // Key'i değiştirerek bileşeni yeniden yükle

        }
        console.log(source);
      
      },[source])
      const onUnmount = useCallback(() => setMap(null), []);
      const [map, setMap] = useState<google.maps.Map | null>(null);

const onLoad = useCallback((mapInstance: google.maps.Map) => {
  setMap(mapInstance);
}, []);

      useEffect(() => {
        if (map && source.lat && source.lng) {
          // Haritayı yeni konuma animasyonla kaydır
          map.panTo({ lat: source.lat, lng: source.lng });
          
          // Veya direkt merkezi değiştir
          map.setCenter({ lat: source.lat, lng: source.lng });
          
          // Zoom seviyesini de güncelleyebilirsiniz
          map.setZoom(12);
        }
      }, [source, map]);
      return (
        <div>
      <Wrapper apiKey="AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E" key={mapKey}>
        <GoogleMap
        mapContainerStyle={{ width: '50%', height: '600px', position: 'absolute' }}
        center={center}
          zoom={10}
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
       <MarkerF 
            position={{lat:36.9914, lng:35.3308}}
       
          />
     

          {/* Child components, such as markers, info windows, etc. */}
        </GoogleMap>
      
              </Wrapper>
        
        </div>

      ) 
}

export default GoogleMapComponent
