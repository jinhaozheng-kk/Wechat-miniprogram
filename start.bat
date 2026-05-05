@echo off
setlocal

rem ============================================================
rem 华信通讯科技 - 三端一键启动脚本
rem ------------------------------------------------------------
rem 启动两端到独立窗口：
rem   - Spring Boot 后端  →  http://localhost:8080
rem   - Vue 管理端        →  http://localhost:5173
rem 小程序请用微信开发者工具手动打开 huaxin-miniprogram\
rem
rem 前置条件：
rem   - JDK 17+   (mvnw 编译运行需要)
rem   - Node.js 18+ (Vite 需要)
rem   - MySQL 8 已运行；首次使用先双击 init-db.bat 初始化数据库
rem ============================================================

echo.
echo === 华信通讯科技 三端一键启动 ===
echo.

rem 1) 检查 Java
where java >nul 2>nul
if errorlevel 1 (
    echo [错误] 找不到 java，请先安装 JDK 17+ 并将 java 加入 PATH。
    pause
    exit /b 1
)

rem 2) 检查 Node
where node >nul 2>nul
if errorlevel 1 (
    echo [错误] 找不到 node，请先安装 Node.js 18+。
    pause
    exit /b 1
)

rem 3) 首次运行：装一次 client/ 依赖
if not exist "%~dp0client\node_modules" (
    echo [初始化] 第一次运行，安装 client/ 依赖中（可能需要几分钟）...
    pushd "%~dp0client"
    call npm install
    if errorlevel 1 (
        echo [错误] npm install 失败，请检查网络或 npm 配置。
        popd
        pause
        exit /b 1
    )
    popd
)

rem 4) 启动后端（独立窗口）
echo [启动] Spring Boot 后端 -> 新窗口（标题：华信-后端）
start "华信-后端 (8080)" cmd /k "chcp 65001 >nul && cd /d %~dp0server\server && .\mvnw.cmd spring-boot:run"

rem 给后端 3 秒缓冲，让端口先占上再起前端
timeout /t 3 /nobreak >nul

rem 5) 启动前端（独立窗口）
echo [启动] Vue 管理端 -> 新窗口（标题：华信-管理端）
start "华信-管理端 (5173)" cmd /k "chcp 65001 >nul && cd /d %~dp0client && npm run dev"

echo.
echo === 启动指令已发出，关掉对应窗口即可停止服务 ===
echo.
echo   后端 API:    http://localhost:8080
echo   管理端:      http://localhost:5173    （登录：admin / admin123）
echo   小程序:      用微信开发者工具打开 huaxin-miniprogram\
echo                AppID: wxab8299303b8f0174
echo.
echo 提示：如登录失败或接口 500，多半是数据库未初始化——双击 init-db.bat 运行一次。
echo.
pause
endlocal
