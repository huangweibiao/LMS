#!/bin/bash
# 后端打包脚本

echo "========================================="
echo "开始编译LMS后端项目..."
echo "========================================="

cd /Users/hwb1122/AiProject/LMS/lms-backend

# 检查Maven是否可用
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven，请先安装Maven"
    exit 1
fi

# 清理并编译
echo "执行 mvn clean compile..."
mvn clean compile -DskipTests

if [ $? -eq 0 ]; then
    echo "========================================="
    echo "编译成功!"
    echo "========================================="
else
    echo "========================================="
    echo "编译失败!"
    echo "========================================="
    exit 1
fi