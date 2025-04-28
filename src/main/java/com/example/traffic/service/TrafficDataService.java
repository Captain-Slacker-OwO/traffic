package com.example.traffic.service;
import com.example.traffic.model.TrafficFlow;
import java.util.List;
import java.time.LocalDate;

public interface TrafficDataService {
    List<TrafficFlow> getHourlyData(String intersectionId, LocalDate date);
    
    List<TrafficFlow> getRangeData(String intersectionId, LocalDate startDate, LocalDate endDate);
}