package com.example.traffic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "traffic.simulation")
public class TrafficProperties {
    private static final Logger log = LoggerFactory.getLogger(TrafficProperties.class);
    private int[] basePattern;
    private int fluctuation = 20; // 设置默认值
    
    @PostConstruct
    public void init() {
        // 添加basePattern默认值
        if(basePattern == null || basePattern.length == 0) {
            basePattern = new int[]{50,30,20,15,20,40,60,80,100,120,110,100};
            log.warn("使用默认交通流量基值配置");
        }
        if(fluctuation <= 0) {
            throw new IllegalStateException("配置错误：traffic.simulation.fluctuation必须大于0");
        }
    }

    // getters和setters
    public int[] getBasePattern() { return basePattern; }
    public void setBasePattern(int[] basePattern) { this.basePattern = basePattern; }
    public int getFluctuation() { return fluctuation; }
    public void setFluctuation(int fluctuation) { this.fluctuation = fluctuation; }
}