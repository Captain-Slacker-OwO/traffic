package com.example.traffic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrafficDataRepository extends JpaRepository<TrafficFlow, Long> {
    List<TrafficFlow> findByIntersectionId(String intersectionId);
}