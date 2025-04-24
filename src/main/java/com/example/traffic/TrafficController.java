package com.example.traffic;
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

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {
    
    @Autowired
    private TrafficDataService dataService;

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<TrafficFlow>>> getHistoryData(
        @RequestParam String intersection,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    
        try {
            List<TrafficFlow> data = dataService.getHourlyData(intersection, date);
            return ResponseEntity.ok()
                .header("Cache-Control", "max-age=60")
                .body(ApiResponse.success(data));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("DATA_FETCH_ERROR", e.getMessage()));
        }
    }
}