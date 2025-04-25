package com.example.traffic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class TrafficFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String intersectionId;
    private LocalDateTime timestamp;
    private int vehicleCount;
    private double averageSpeed;
    private String trafficStatus;
    
    public TrafficFlow(String intersectionId, LocalDateTime timestamp, int vehicleCount) {
        this.intersectionId = intersectionId;
        this.timestamp = timestamp;
        this.vehicleCount = vehicleCount;
    }
}