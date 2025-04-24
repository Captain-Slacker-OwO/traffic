package com.example.traffic.service;

import java.util.Map;

public interface PredictionService {
    Map<String, Object> predictTraffic(String data);
    Map<String, Object> predictNextHour(String data);
}