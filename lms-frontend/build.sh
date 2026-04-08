#!/bin/bash
# 前端打包脚本

echo "========================================="
echo "开始构建LMS前端项目..."
echo "========================================="

cd /Users/hwb1122/AiProject/LMS/lms-frontend

# 检查node是否可用
if ! command -v node &> /dev/null; then
    echo "错误: 未找到Node.js，请先安装Node.js"
    exit 1
fi

# 安装依赖
echo "安装依赖..."
npm install

if [ $? -ne 0 ]; then
    echo "错误: 依赖安装失败"
    exit 1
fi

# 编译项目
echo "执行构建..."
npm run build

if [ $? -eq 0 ]; then
    echo "========================================="
    echo "构建成功!"
    echo "========================================="
else
    echo "========================================="
    echo "构建失败!"
    echo "========================================="
    exit 1
fi