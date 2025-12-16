package com.example.EcoTrack.user.dto.pastsensors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PastSensorsSensorFixDto {
    private Long id;
    private Date startTime;
    private Date completedTime;
    private String note;
}
