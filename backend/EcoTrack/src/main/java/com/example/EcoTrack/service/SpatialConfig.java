package com.example.EcoTrack.service;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpatialConfig {

    @Bean
    public GeometryFactory geometryFactory() {
        // SRID 4326 is the standard for WGS84 (latitude/longitude) coordinates
        return new GeometryFactory(new PrecisionModel(), 4326);
    }
}