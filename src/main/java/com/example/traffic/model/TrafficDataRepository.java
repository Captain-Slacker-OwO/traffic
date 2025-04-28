package com.example.traffic.model;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TrafficDataRepository extends JpaRepository<TrafficFlow, Long> {
    List<TrafficFlow> findByIntersectionIdAndTimestampBetween(
        String intersectionId, 
        LocalDateTime start, 
        LocalDateTime end
    );
}