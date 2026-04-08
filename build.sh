#!/bin/bash
# LMS 整合打包脚本
# 将前端打包后的静态资源整合到Spring Boot应用中

echo "========================================="
echo "开始LMS项目整合打包..."
echo "========================================="

# 获取脚本所在目录（项目根目录）
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 定义路径
FRONTEND_DIR="$PROJECT_ROOT/lms-frontend"
BACKEND_DIR="$PROJECT_ROOT/lms-backend"
FRONTEND_DIST="$FRONTEND_DIR/dist"
BACKEND_STATIC="$BACKEND_DIR/src/main/resources/static"

echo "项目根目录: $PROJECT_ROOT"
echo "前端目录: $FRONTEND_DIR"
echo "后端目录: $BACKEND_DIR"

# ============ 前端打包 ============
echo ""
echo "========================================="
echo "步骤1: 构建前端项目"
echo "========================================="

cd "$FRONTEND_DIR"

# 检查node是否可用
if ! command -v node &> /dev/null; then
    echo "错误: 未找到Node.js，请先安装Node.js"
    exit 1
fi

# 检查npm是否可用
if ! command -v npm &> /dev/null; then
    echo "错误: 未找到npm，请先安装Node.js"
    exit 1
fi

# 检查依赖是否存在，不存在则安装
if [ ! -d "node_modules" ]; then
    echo "安装前端依赖..."
    HOME=/tmp npm install
    if [ $? -ne 0 ]; then
        echo "错误: 前端依赖安装失败"
        exit 1
    fi
fi

# 打包前端
echo "执行前端构建..."
HOME=/tmp npm run build
if [ $? -ne 0 ]; then
    echo "错误: 前端构建失败"
    exit 1
fi

echo "前端构建完成!"

# ============ 整合前端资源 ============
echo ""
echo "========================================="
echo "步骤2: 整合前端资源到后端"
echo "========================================="

# 检查前端dist目录是否存在
if [ ! -d "$FRONTEND_DIST" ]; then
    echo "错误: 前端构建输出目录不存在: $FRONTEND_DIST"
    exit 1
fi

# 创建static目录（如果不存在）
if [ ! -d "$BACKEND_STATIC" ]; then
    mkdir -p "$BACKEND_STATIC"
fi

# 复制前端构建产物到static目录
echo "复制前端静态资源到: $BACKEND_STATIC"
rm -rf "$BACKEND_STATIC"/*
cp -r "$FRONTEND_DIST"/* "$BACKEND_STATIC/"

if [ $? -ne 0 ]; then
    echo "错误: 复制前端资源失败"
    exit 1
fi

echo "前端资源整合完成!"

# ============ 后端打包 ============
echo ""
echo "========================================="
echo "步骤3: 构建后端项目"
echo "========================================="

cd "$BACKEND_DIR"

# 检查Maven是否可用
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven，请先安装Maven"
    exit 1
fi

# 清理并打包（跳过测试）
echo "执行Maven打包..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "错误: 后端打包失败"
    exit 1
fi

# ============ 输出结果 ============
echo ""
echo "========================================="
echo "打包完成!"
echo "========================================="

# 查找生成的jar文件
JAR_FILE=$(ls -1 target/*.jar 2>/dev/null | grep -v "original" | head -1)

if [ -n "$JAR_FILE" ]; then
    JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
    echo "生成的jar文件: $JAR_FILE"
    echo "文件大小: $JAR_SIZE"
    echo ""
    echo "运行方式: java -jar $JAR_FILE"
    echo "默认访问地址: http://localhost:8080"
else
    echo "警告: 未找到生成的jar文件"
    exit 1
fi

echo ""
echo "========================================="
echo "LMS项目打包成功!"
echo "========================================="