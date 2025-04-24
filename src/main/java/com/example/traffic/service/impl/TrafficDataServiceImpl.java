package com.example.traffic.service.impl;

import com.example.traffic.service.TrafficDataService;
import com.example.traffic.model.TrafficFlow;
import com.example.traffic.config.TrafficProperties; // 新增导入
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TrafficDataServiceImpl implements TrafficDataService {
    
    @Autowired
    private TrafficProperties trafficProperties; // 注入配置类

    @Override
    public List<TrafficFlow> getHourlyData(String intersectionId, LocalDate date) {
        List<TrafficFlow> mockData = new ArrayList<>();
        Random rand = new Random();
        
        for (int hour = 0; hour < 24; hour++) {
            int base = trafficProperties.getBasePattern()[hour % 12];
            int fluctuation = trafficProperties.getFluctuation();
            int vehicleCount = base + rand.nextInt(fluctuation * 2) - fluctuation;
            
            mockData.add(new TrafficFlow(
                intersectionId, 
                date.atTime(hour, 0), // 修复LocalDateTime转换问题
                Math.abs(vehicleCount)
            ));
        }
        return mockData;
    }
}