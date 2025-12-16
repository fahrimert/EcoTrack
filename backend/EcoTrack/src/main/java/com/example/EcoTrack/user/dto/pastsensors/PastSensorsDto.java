package com.example.EcoTrack.user.dto.pastsensors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PastSensorsDto {
    private Long sensorId;
    private String sensorName;
    private String status;
    private Date installationDate;

    private List<PastSensorsSensorFixDto> sessions;
}
