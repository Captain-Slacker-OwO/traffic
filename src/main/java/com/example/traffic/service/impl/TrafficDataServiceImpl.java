package com.example.traffic.service.impl;

import com.example.traffic.service.TrafficDataService;
import com.example.traffic.model.TrafficFlow;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TrafficDataServiceImpl implements TrafficDataService {
    @Override
    public List<TrafficFlow> getHourlyData(String intersectionId, java.time.LocalDate date) {
        // TODO: Implement actual data retrieval logic
        return List.of(new TrafficFlow(intersectionId, date, 100)); // 模拟数据
    }
}