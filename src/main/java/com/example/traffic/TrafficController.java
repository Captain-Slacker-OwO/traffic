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

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {
    
    @Autowired
    private TrafficDataService dataService;

    @GetMapping("/history")
    public ResponseEntity<?> getHistoryData(
        @RequestParam String intersection,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return ResponseEntity.ok(dataService.getHourlyData(intersection, date));
    }
}