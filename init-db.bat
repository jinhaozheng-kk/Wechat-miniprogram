@echo off
chcp 65001 >nul
setlocal

rem ============================================================
rem 华信通讯科技 - 数据库初始化脚本（一次性）
rem ------------------------------------------------------------
rem 执行 server\server\src\main\resources\sql\schema.sql：
rem   - DROP DATABASE IF EXISTS huaxin
rem   - 重建 category / product / inquiry 三张表 + 测试数据
rem
rem 默认连接 localhost MySQL，账号 root，密码 root。
rem 如需改账号，请设置环境变量后再运行：
rem   set DB_USERNAME=youruser
rem   set DB_PASSWORD=yourpass
rem   init-db.bat
rem ============================================================

echo.
echo === 华信通讯科技 数据库初始化 ===
echo.
echo 此脚本会**清空并重建** huaxin 数据库。
echo 已有数据将丢失！
echo.
set /p confirm=确认继续？(输入 y 继续，其它任意键取消):
if /i not "%confirm%"=="y" (
    echo 已取消。
    pause
    exit /b 0
)

rem 检查 mysql 客户端
where mysql >nul 2>nul
if errorlevel 1 (
    echo [错误] 找不到 mysql 命令。请先安装 MySQL Client 并加入 PATH，
    echo        或手动执行：mysql -u root -p --default-character-set=utf8mb4 ^< server\server\src\main\resources\sql\schema.sql
    pause
    exit /b 1
)

rem 默认 root/root，可被环境变量覆盖
set DB_USER=%DB_USERNAME%
if "%DB_USER%"=="" set DB_USER=root
set DB_PASS=%DB_PASSWORD%
if "%DB_PASS%"=="" set DB_PASS=root

set SCHEMA=%~dp0server\server\src\main\resources\sql\schema.sql
if not exist "%SCHEMA%" (
    echo [错误] 找不到 schema.sql：%SCHEMA%
    pause
    exit /b 1
)

echo.
echo 使用 user=%DB_USER% 连接 localhost MySQL，导入 schema.sql ...
mysql -u %DB_USER% -p%DB_PASS% --default-character-set=utf8mb4 < "%SCHEMA%"

if errorlevel 1 (
    echo.
    echo [错误] schema.sql 执行失败。常见原因：
    echo   - MySQL 没启动 / 端口不是 3306
    echo   - 账号密码不对（默认 root/root，可用 DB_USERNAME / DB_PASSWORD 覆盖）
    pause
    exit /b 1
)

echo.
echo === 数据库 huaxin 初始化完成 ===
echo 下一步：双击 start.bat 一键启动后端和管理端。
echo.
pause
endlocal
