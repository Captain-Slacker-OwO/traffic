package com.example.traffic.model;

import java.time.LocalDateTime;

public record TrafficFlowDTO(
    Long id,
    String intersectionId,
    LocalDateTime timestamp,
    Integer vehicleCount,
    Double averageSpeed,  // 修改为Double类型
    String trafficStatus
) {
    public static TrafficFlowDTO fromEntity(TrafficFlow flow) {
        return new TrafficFlowDTO(
            flow.getId(),
            flow.getIntersectionId(),
            flow.getTimestamp(),
            flow.getVehicleCount(),
            flow.getAverageSpeed(),  // 添加缺失字段
            flow.getTrafficStatus()  // 添加缺失字段
        );
    }
}