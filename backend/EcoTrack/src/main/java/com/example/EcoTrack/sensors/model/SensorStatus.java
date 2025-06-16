package com.example.EcoTrack.sensors.model;

import java.util.List;

public enum SensorStatus {

    ACTIVE(
            "ACTIVE",
            "#2bf21e"
    ),
    FAULTY("FAULTY",
            "#d63124"
    ),
    IN_REPAIR("IN_REPAIR",
            "#e8d14d"
    ),
    SOLVED("SOLVED",
              "#e8d14d"
    );

    private final String statusName;
    private final String colorCode;

    SensorStatus(String statusName, String colorCode ) {
        this.statusName = statusName;
        this.colorCode = colorCode;
    }

    public String getDisplayName() { return statusName; }
    public String  getColorCode() { return colorCode; }

    public static List<SensorStatus> getAll() {
        return List.of(SensorStatus.values());
    }
}
