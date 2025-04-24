package com.example.traffic.service.impl;

import com.example.traffic.service.PredictionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class PredictionServiceImpl implements PredictionService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${python.service.url}")
    private String predictUrl;

    @Override
    @Retry(name = "pythonService", fallbackMethod = "fallbackPredict")
    @CircuitBreaker(name = "pythonService", fallbackMethod = "fallbackPredict") 
    public Map<String, Object> predictTraffic(String data) {
        return predictNextHour(data);
    }
    
    @Override
    @Retry(name = "pythonService", fallbackMethod = "fallbackPredict")
    @CircuitBreaker(name = "pythonService", fallbackMethod = "fallbackPredict")
    public Map<String, Object> predictNextHour(String data) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(predictUrl)
                    .queryParam("intersection", data);
            return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {}).getBody();
        } catch (RestClientException e) {
            log.error("预测服务调用失败 - 数据: {}, 错误信息: {}", data, e.getMessage());
            return Collections.singletonMap("error", "预测服务暂时不可用");
        }
    }

    private Map<String, Object> fallbackPredict(String data, Exception e) {
        log.warn("熔断触发 - 数据: {}", data);
        return Map.of("prediction", "默认值", "fallback", true);
    }
}