package com.example.traffic.service.impl;

import com.example.traffic.service.TrafficDataService;
import com.example.traffic.model.TrafficFlow;

import org.springframework.stereotype.Service;
import java.util.List;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.ZoneId; // 新增导入
import java.time.LocalDateTime; // 新增导入
import com.example.traffic.model.TrafficDataRepository; // 新增导入
import lombok.extern.slf4j.Slf4j; // 新增导入
@Slf4j



@Service
public class TrafficDataServiceImpl implements TrafficDataService {
    
    @Autowired
    private TrafficDataRepository trafficDataRepository; // 注入配置类

    @Override
    public List<TrafficFlow> getHourlyData(String intersectionId, LocalDate date) {
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        // 修正时区转换逻辑
        LocalDateTime start = date.atStartOfDay().atZone(zone).toLocalDateTime();
        LocalDateTime end = start.plusDays(1).minusNanos(1);
        
        // 添加查询日志
        log.info("查询条件 - 路口ID: {}, 起始时间: {}, 结束时间: {}", 
            intersectionId, start, end);
        
        return trafficDataRepository.findByIntersectionIdAndTimestampBetween(
            intersectionId, 
            start,
            end
        );
    }
    
    @Override
    public List<TrafficFlow> getRangeData(String intersectionId, LocalDate startDate, LocalDate endDate) {
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        LocalDateTime start = startDate.atStartOfDay().atZone(zone).toLocalDateTime();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay().atZone(zone).toLocalDateTime();
        
        return trafficDataRepository.findByIntersectionIdAndTimestampBetween(intersectionId, start, end);
    }
}