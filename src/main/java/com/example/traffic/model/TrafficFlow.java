package com.example.traffic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.persistence.Column;

@Entity
@Data
@NoArgsConstructor
public class TrafficFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "intersection_id") // 确保与数据库列名一致
    private String intersectionId;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    private int vehicleCount;
    private double averageSpeed;
    private String trafficStatus;
    
    public TrafficFlow(String intersectionId, LocalDateTime timestamp, int vehicleCount) {
        this.intersectionId = intersectionId;
        this.timestamp = timestamp;
        this.vehicleCount = vehicleCount;
        // 添加缺失字段初始化
        this.averageSpeed = calculateSpeed(vehicleCount); 
        this.trafficStatus = determineStatus(vehicleCount);
    }
    
    private double calculateSpeed(int count) {
        return count > 0 ? 60.0 - (count * 0.3) : 0;
    }
    
    private String determineStatus(int count) {
        if (count < 30) return "畅通";
        if (count < 80) return "缓行";
        return "拥堵";
    }
}