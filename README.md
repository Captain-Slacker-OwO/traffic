# 智能交通流量分析系统配置与运行指南（v1.2）

## 一、项目架构总览
![系统架构图](https://via.placeholder.com/800x400.png?text=System+Architecture)

### 1.1 技术栈组成
- **前端展示层**：React 18 + Chart.js 3.7
- **业务逻辑层**：Spring Boot 2.7 + JPA 
- **数据预测层**：Python 3.9 + Prophet 1.1
- **数据存储层**：MySQL 8.0 + Redis 6.2
- **基础设施层**：Docker 20.10 + Nginx 1.21

### 1.2 目录结构解析
```bash
traffic/
├── frontend/              # 前端工程
│   ├── public/           # 静态资源
│   └── src/              # 源码目录
│       ├── components/   # 可视化组件
│       └── services/     # API服务层
├── src/                  # 后端工程
│   └── main/
│       ├── java/         # 业务逻辑
│       └── resources/    # 配置文件
├── prediction_service/    # 预测服务
└── docker/               # 容器配置
```

## 二、环境准备（macOS）

### 2.1 基础依赖安装
```bash
# 安装Homebrew
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# 安装JDK17
brew install openjdk@17

# 安装Node.js 18.x
brew install node@18

# 安装Python 3.9
brew install python@3.9

# 安装Docker Desktop
brew install --cask docker
```

### 2.2 数据库配置
1. 启动MySQL容器
```bash
docker run --name traffic-mysql -e MYSQL_ROOT_PASSWORD=yourpassword -p 3306:3306 -d mysql:8.0
```

2. 初始化数据库
```bash
mysql -h 127.0.0.1 -u root -p < init_db.sh
```

## 三、配置文件详解

### 3.1 后端配置（application.properties）
```properties
# 服务端口
server.port=8080

# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/traffic_db
spring.datasource.username=root
spring.datasource.password=yourpassword

# 数据模拟参数
traffic.simulation.interval=5000
traffic.base-pattern=50,30,20,15,20,40,60,80,100,120,110,100
```

### 3.2 前端配置（.env.development）
```env
REACT_APP_API_BASE=http://localhost:8080/api
REACT_APP_UPDATE_INTERVAL=30
```

### 3.3 预测服务配置（config.ini）
```ini
[model]
prophet_seasonality_mode=multiplicative
prediction_horizon=24
```

## 四、数据库初始化

### 4.1 表结构说明
```sql
CREATE TABLE traffic_flow (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  intersection_id VARCHAR(50) NOT NULL,
  timestamp DATETIME NOT NULL,
  vehicle_count INT NOT NULL
);
```

### 4.2 数据模拟规则
- **基准流量**：每小时预设基准值
- **随机波动**：±15% 正态分布波动
- **存储频率**：每5秒生成一条记录

## 五、系统启动流程

### 5.1 后端服务启动
```bash
# 编译打包
mvn clean package -DskipTests

# 运行服务
java -jar target/traffic-0.0.1-SNAPSHOT.jar
```

### 5.2 前端服务启动
```bash
cd frontend
npm install --legacy-peer-deps
npm start
```

### 5.3 预测服务启动
```bash
cd prediction_service
conda create -n traffic-prediction python=3.9
conda activate traffic-prediction
pip install -r requirements.txt
python predict.py
```

## 六、系统维护与监控

### 6.1 日志查看
```bash
# 后端日志
tail -f /var/log/traffic/traffic.log

# 数据库日志
docker logs -f traffic-mysql
```

### 6.2 健康检查接口
```http
GET http://localhost:8080/actuator/health
```

### 6.3 系统扩展建议
1. **增加观测点**：修改`intersection_id`参数
2. **调整预测模型**：修改predict.py中的seasonality参数
3. **扩展存储能力**：配置Redis缓存层

## 七、常见问题解决方案

### 7.1 端口冲突处理
```bash
# 查找占用端口的进程
lsof -i :8080

# 终止指定进程
kill -9 <PID>
```

### 7.2 依赖安装失败处理
```bash
# 清理npm缓存
npm cache clean --force

# 重新安装依赖
rm -rf node_modules package-lock.json
npm install
```

### 7.3 数据不显示排查步骤
1. 检查数据库连接状态
2. 验证API接口响应
3. 查看浏览器控制台报错

## 八、安全配置建议

### 8.1 访问控制策略
```nginx
location /api {
    limit_req zone=api_limit burst=20;
    proxy_pass http://backend;
}
```

### 8.2 数据备份方案
```bash
# 每日自动备份
0 2 * * * mysqldump -u root -p yourpassword traffic_db > /backup/traffic_$(date +\%Y\%m\%d).sql
```

> 系统版本要求：macOS Monterey 12.3+

        