package com.example.traffic.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import com.example.traffic.service.TrafficDataService;
import java.util.List;
import java.util.stream.Collectors;
import com.example.traffic.model.TrafficFlow;
import com.example.traffic.model.ApiResponse;
import com.example.traffic.model.TrafficFlowDTO;
import jakarta.validation.constraints.NotBlank;
import java.time.ZoneId;
import java.time.LocalDateTime;
import com.example.traffic.util.TimeRangeCalculator;

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {
    
    @Autowired
    private TrafficDataService dataService;

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<TrafficFlowDTO>>> getHistoryData(
        @RequestParam("intersectionId") @NotBlank String intersection,
        @RequestParam(required = false) String timeRange) {
        
        try {
            LocalDate[] dateRange = TimeRangeCalculator.calculate(timeRange);
            List<TrafficFlow> data = dataService.getRangeData(intersection, dateRange[0], dateRange[1]);
            
            // 使用DTO转换
            List<TrafficFlowDTO> dtoList = data.stream()
                .map(TrafficFlowDTO::fromEntity)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok()
                .body(ApiResponse.success(dtoList));  // 这里返回的是包裹在ApiResponse中的DTO列表
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("DATA_FETCH_ERROR", e.getMessage()));
        }
    }
}