package com.example.traffic;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;
import com.example.traffic.model.TrafficFlow;
import com.example.traffic.model.TrafficDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TrafficGeneratorService {
    // 模拟不同时段的交通流量基数
    private static final int[] HOURLY_BASE = {50, 30, 20, 15, 20, 40, 60, 80, 100, 120, 110, 100};

    @Autowired
    private TrafficDataRepository trafficDataRepository;
    
    @Scheduled(fixedRateString = "${traffic.simulation.interval:5000}")
    public void generateSimulationData() {
        int hour = LocalDateTime.now().getHour();
        double mean = HOURLY_BASE[hour % 12];
        double trafficFlow = new Random().nextGaussian() * 15 + mean;
        
        TrafficFlow flow = new TrafficFlow();
        flow.setIntersectionId("intersection_1");
        flow.setTimestamp(LocalDateTime.now());
        flow.setVehicleCount((int)trafficFlow);
        trafficDataRepository.save(flow);
        
        System.out.println("生成并存储模拟交通流量: " + trafficFlow);
    }
}