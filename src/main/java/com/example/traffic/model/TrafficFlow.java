package com.example.traffic.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TrafficFlow {
    public TrafficFlow(String intersectionId, LocalDate date, int vehicleCount) {
        this.intersectionId = intersectionId;
        this.timestamp = date.atStartOfDay();
        this.vehicleCount = vehicleCount;
    }
    private String intersectionId;
    private LocalDateTime timestamp;
    private int vehicleCount;
    private double averageSpeed;
    private String trafficStatus;
}