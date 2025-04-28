package com.example.traffic.util;
import java.time.LocalDate;


public class TimeRangeCalculator {
    // 添加对相对时间范围的支持
    public static LocalDate[] calculate(String timeRange) {
        if (timeRange == null || timeRange.isEmpty()) {
            return new LocalDate[]{LocalDate.now().minusDays(7), LocalDate.now()};
        }
        
        // 处理相对时间格式（如last7days）
        if (timeRange.startsWith("last")) {
            String[] parts = timeRange.substring(4).split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            if (parts.length == 2) {
                int num = Integer.parseInt(parts[0]);
                String unit = parts[1].toLowerCase();
                LocalDate endDate = LocalDate.now();
                
                switch(unit) {
                    case "days":
                        return new LocalDate[]{endDate.minusDays(num), endDate};
                    // 可扩展其他时间单位...
                    default:
                        throw new IllegalArgumentException("不支持的相对时间单位: " + unit);
                }
            }
        }
        
        // 原有的绝对时间处理逻辑...
        LocalDate end = LocalDate.now();
        return switch (timeRange) {
            case "1d" -> new LocalDate[]{end, end};
            case "7d" -> new LocalDate[]{end.minusDays(6), end};
            case "1m" -> new LocalDate[]{end.minusMonths(1), end};
            case "3m" -> new LocalDate[]{end.minusMonths(3), end};
            case "1y" -> new LocalDate[]{end.minusYears(1), end};
            case "3y" -> new LocalDate[]{end.minusYears(3), end};
            default -> throw new IllegalArgumentException("无效的时间范围参数");
        };
    }
}