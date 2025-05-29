// components/HeatMapComponent.tsx
"use client";
import { GoogleMap, HeatmapLayer } from "@react-google-maps/api";
import { useCallback, useEffect, useMemo, useState } from "react";
import { Wrapper } from '@googlemaps/react-wrapper'


const containerStyle = {
  width: '100%',
  height: window.innerHeight*0.8
};

const center = {
  lat: 39.9208, 
  lng: 32.8541
};


const HeatMapForWorkerSensorsComponent = ({ response }: { response: [{
    id: string;
    latitude: number;
    longitude: number;
}] }) => {
    const [mapLoaded, setMapLoaded] = useState(false);
      const [map, setMap] = useState<google.maps.Map | null>(null);

      const onUnmount = useCallback(() => setMap(null), []);

  
 
 const heatmapData = useMemo(
    () =>

      {
            if (!mapLoaded || !window.google?.maps) return [];
 return response.map(
        (point) =>
          new window.google.maps.LatLng(point.latitude, point.longitude)
      )
      }
     ,
    [mapLoaded,response]
  );

  return (
   <Wrapper
      googleMapsApiKey="AIzaSyBKLifBrIReU58VvfnhLRz0I73c-_laK0E"
      libraries={["visualization"]}
    >      <GoogleMap 
              onLoad={() => setMapLoaded(true)}
                  onUnmount={onUnmount}
      mapContainerStyle={containerStyle} center={center} zoom={12}>
             {mapLoaded && heatmapData.length > 0 && (
          <HeatmapLayer data={heatmapData} />
        )}
      </GoogleMap>
             </Wrapper>

  );
};

export default HeatMapForWorkerSensorsComponent;
