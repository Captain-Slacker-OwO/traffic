package com.example.traffic;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TrafficGeneratorService {
    // 模拟不同时段的交通流量基数
    private static final int[] HOURLY_BASE = {50, 30, 20, 15, 20, 40, 60, 80, 100, 120, 110, 100};

    @Scheduled(fixedRateString = "${traffic.simulation.interval:5000}")
    public void generateSimulationData() {
        // 生成符合正态分布的随机交通流量
        int hour = LocalDateTime.now().getHour();
        double mean = HOURLY_BASE[hour % 12];
        double trafficFlow = new Random().nextGaussian() * 15 + mean;
        
        // 保存到数据库或发送到消息队列
        System.out.println("生成模拟交通流量: " + trafficFlow);
    }
}