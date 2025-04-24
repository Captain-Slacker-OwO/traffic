#!/bin/bash
# 自动生成数据库配置脚本（适配Docker部署）
docker exec -i mysql-traffic mysql -uroot -p'yourpassword' <<EOF
CREATE DATABASE IF NOT EXISTS traffic_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE traffic_db;

CREATE TABLE IF NOT EXISTS traffic_flow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    intersection_id VARCHAR(50) NOT NULL COMMENT '路口编号',
    timestamp DATETIME NOT NULL COMMENT '记录时间',
    vehicle_count INT NOT NULL COMMENT '车辆计数',
    average_speed DOUBLE COMMENT '平均速度（km/h）',
    traffic_status VARCHAR(20) COMMENT '交通状态',
    INDEX idx_intersection (intersection_id),
    INDEX idx_timestamp (timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
EOF

echo "数据库表结构已根据Java实体类自动生成"