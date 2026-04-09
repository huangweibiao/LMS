# LMS系统一键打包脚本（PowerShell版本）

Write-Host "====================================" -ForegroundColor Cyan
Write-Host "LMS系统一键打包脚本" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan
Write-Host ""

# 函数：清理后端静态资源目录中的旧文件
function Clean-Static-Files {
    param([string]$path)

    $staticDir = "lms-backend\src\main\resources\static"

    if (Test-Path $staticDir) {
        Write-Host "[6/6] 清理后端静态资源目录中的旧文件: $staticDir" -ForegroundColor Yellow

        # 获取所有文件和文件夹
        $items = Get-ChildItem -Path "$staticDir\*" -Force

        if ($items) {
            foreach ($item in $items) {
                # 排除 .gitkeep 文件
                if ($item.Name -ne ".gitkeep") {
                    Remove-Item -Path $item.FullName -Force -Recurse
                    Write-Host "  已删除: $($item.FullName)" -ForegroundColor Green
                }
            }
            Write-Host "  后端静态资源目录清理完成" -ForegroundColor Green
        } else {
            Write-Host "  后端静态资源目录为空或不存在" -ForegroundColor Gray
        }
    } else {
        Write-Host "  后端静态资源目录不存在: $staticDir" -ForegroundColor Red
    }
}

function Clean-Old-Builds {
    Write-Host "[1/6] 清理旧的构建文件..." -ForegroundColor Yellow

    $oldDirs = @(
        "lms-frontend\dist",
        "lms-frontend\node_modules",
        "lms-backend\target"
    )
    foreach ($dir in $oldDirs) {
        if (Test-Path $dir) {
            Remove-Item -Path $dir -Recurse -Force
            Write-Host "  已删除: $dir" -ForegroundColor Green
        }
    }
}

# 函数：安装前端依赖
function Install-Frontend-Deps {
    Write-Host "[2/6] 安装前端依赖..." -ForegroundColor Yellow

    Set-Location lms-frontend
    $result = npm install 2>&1 | Out-String

    if ($LASTEXITCODE -ne 0) {
        Write-Host "前端依赖安装失败" -ForegroundColor Red
        exit 1
    }

    Write-Host "  前端依赖安装完成" -ForegroundColor Green
    Set-Location ..
}

# 函数：构建前端项目
function Build-Frontend {
    Write-Host "[3/6] 构建前端项目..." -ForegroundColor Yellow

    Set-Location lms-frontend
    $result = npm run build 2>&1 | Out-String

    if ($LASTEXITCODE -ne 0) {
        Write-Host "前端构建失败" -ForegroundColor Red
        exit 1
    }

    Write-Host "  前端构建完成" -ForegroundColor Green
    Set-Location ..
}

# 函数：复制前端构建文件
function Copy-Frontend-Build {
    Write-Host "[4/6] 复制前端构建文件到后端..." -ForegroundColor Yellow

    $sourceDir = "lms-frontend\dist"
    $targetDir = "lms-backend\src\main\resources\static"

    # 确保目标目录存在
    if (-not (Test-Path $targetDir)) {
        New-Item -ItemType Directory -Force -Path $targetDir
    }

    # 复制文件
    Copy-Item -Path "$sourceDir\*" -Destination $targetDir -Recurse -Force

    # 复制 public 目录中的静态资源（如 favicon）
    if (Test-Path "lms-frontend\public") {
        $publicFiles = Get-ChildItem -Path "lms-frontend\public" -Force
        foreach ($file in $publicFiles) {
            Copy-Item -Path $file.FullName -Destination $targetDir -Force
            Write-Host "  已复制: $($file.Name)" -ForegroundColor Green
        }
    }

    Write-Host "  前端文件已复制到: $targetDir" -ForegroundColor Green
}

# 函数：打包后端项目
function Package-Backend {
    Write-Host "[5/6] 打包后端项目..." -ForegroundColor Yellow

    Set-Location lms-backend
    $result = mvn clean package -DskipTests 2>&1 | Out-String

    if ($LASTEXITCODE -ne 0) {
        Write-Host "后端打包失败" -ForegroundColor Red
        exit 1
    }

    Write-Host "  后端打包完成" -ForegroundColor Green
    Set-Location ..
}

# 主流程
try {
    Write-Host ""
    Write-Host "====================================" -ForegroundColor Cyan
    Write-Host "LMS系统一键打包脚本" -ForegroundColor Cyan
    Write-Host "====================================" -ForegroundColor Cyan
    Write-Host ""

    # 步骤 1：清理后端静态资源目录中的旧文件
    Clean-Static-Files

    Write-Host ""

    # 步骤 2：清理旧的构建文件
    Clean-Old-Builds

    Write-Host ""

    # 步骤 3：安装前端依赖
    Install-Frontend-Deps

    Write-Host ""

    # 步骤 4：构建前端项目
    Build-Frontend

    Write-Host ""

    # 步骤 5：复制前端构建文件
    Copy-Frontend-Build

    Write-Host ""

    # 步骤 6：打包后端项目
    Package-Backend

    Write-Host ""
    Write-Host "====================================" -ForegroundColor Green
    Write-Host "打包完成！" -ForegroundColor Green
    Write-Host "====================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "后端JAR包位置: lms-backend\target" -ForegroundColor Cyan
    Write-Host "前端静态文件位置: lms-backend\src\main\resources\static" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "部署命令:" -ForegroundColor Yellow
    Write-Host "  java -jar lms-backend\target\lms-backend-1.0.0.jar" -ForegroundColor White
    Write-Host ""
    Write-Host "可选参数:" -ForegroundColor Yellow
    Write-Host "  --server.port=8880  指定端口(默认8880)" -ForegroundColor White
    Write-Host ""
    Write-Host "可选参数:" -ForegroundColor Yellow
    Write-Host "  --server.port=8880  指定端口(默认8880)" -ForegroundColor White
    Write-Host ""

} catch {
    Write-Host "打包过程中出现错误: $_" -ForegroundColor Red
    exit 1
}

Write-Host "按任意键退出..."
$null = $Host.UI.RawUI.ReadKey()