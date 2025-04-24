package com.example.traffic.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TrafficFlow {
    private String intersectionId;
    private LocalDateTime timestamp;  // 修改为LocalDateTime
    private int vehicleCount;
    
    public TrafficFlow(String intersectionId, LocalDateTime timestamp, int vehicleCount) {
        this.intersectionId = intersectionId;
        this.timestamp = timestamp;
        this.vehicleCount = vehicleCount;
    }
    private double averageSpeed;
    private String trafficStatus;
}