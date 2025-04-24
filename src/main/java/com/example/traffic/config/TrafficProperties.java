package com.example.traffic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


@Component
@ConfigurationProperties(prefix = "traffic")
public class TrafficProperties {
    private int[] basePattern = new int[12]; // 初始化数组
    private int fluctuation;

    @PostConstruct
    public void init() {
        if(basePattern == null || basePattern.length == 0) {
            basePattern = new int[]{50,30,20,15,20,40,60,80,100,120,110,100}; // 默认值
        }
    }

    // getters和setters
    public int[] getBasePattern() { return basePattern; }
    public void setBasePattern(int[] basePattern) { this.basePattern = basePattern; }
    public int getFluctuation() { return fluctuation; }
    public void setFluctuation(int fluctuation) { this.fluctuation = fluctuation; }
}