#!/bin/bash
nohup python3 predict.py > predict.log 2>&1 &
echo "Python预测服务已启动，日志文件：predict.log"