package com.example.traffic;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;
import com.example.traffic.model.TrafficFlow;
import com.example.traffic.model.TrafficDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class TrafficGeneratorService {
    // 模拟不同时段的交通流量基数
    private static final int[] HOURLY_BASE = {50, 30, 20, 15, 20, 40, 60, 80, 100, 120, 110, 100};

    @Autowired
    private TrafficDataRepository trafficDataRepository;
    
    @Autowired
    private TransactionTemplate transactionTemplate; // 添加缺失的事务模板注入

    private static final Logger log = LoggerFactory.getLogger(TrafficGeneratorService.class); // 添加日志实例化

    @Scheduled(fixedRateString = "${traffic.simulation.interval:5000}")
    public void generateSimulationData() {
        int hour = LocalDateTime.now().getHour();
        double mean = HOURLY_BASE[hour % 12];
        double trafficFlow = new Random().nextGaussian() * 15 + mean;
        
        TrafficFlow flow = new TrafficFlow();
        flow.setIntersectionId("intersection_1");
        flow.setTimestamp(LocalDateTime.now());
        flow.setVehicleCount((int)trafficFlow);
        
        // 新增交通状态计算逻辑
        if (trafficFlow < 30) {
            flow.setTrafficStatus("free");
        } else if (trafficFlow < 80) {
            flow.setTrafficStatus("slow");
        } else {
            flow.setTrafficStatus("congested");
        }
        
        // 添加事务管理
        // 添加异常处理
        // try {
        //     transactionTemplate.execute(status -> {
        //         trafficDataRepository.save(flow);
        //         return null;
        //     });
        //     log.info("生成并存储模拟交通流量: {}", trafficFlow);
        // } catch (Exception e) {
        //     log.error("数据存储失败: {}", e.getMessage());
        // }
    }
}