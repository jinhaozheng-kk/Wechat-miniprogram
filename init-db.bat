@echo off
setlocal

rem ============================================================
rem 华信通讯科技 - 数据库初始化脚本（幂等，可重复执行）
rem ------------------------------------------------------------
rem 执行 server\server\src\main\resources\sql\schema.sql：
rem   - 数据库 / 表 已存在则跳过创建（IF NOT EXISTS）
rem   - 分类 / 产品按主键 id 跳过已存在记录，缺失则补齐（INSERT IGNORE）
rem   - 询价单仅在表为空时插入示例数据
rem 已录入的真实数据全部保留，不会被覆盖。
rem
rem 默认连接 localhost MySQL，账号 root，密码 root。
rem 如需改账号，请设置环境变量后再运行：
rem   set DB_USERNAME=youruser
rem   set DB_PASSWORD=yourpass
rem   init-db.bat
rem ============================================================

echo.
echo === 华信通讯科技 数据库初始化 / 校准 ===
echo 已有数据保留，缺失结构与默认分类/产品会被补齐。
echo.

rem 定位 mysql.exe：先 PATH，再扫常见安装路径
set "MYSQL_BIN="
for /f "delims=" %%I in ('where mysql 2^>nul') do if not defined MYSQL_BIN set "MYSQL_BIN=%%I"

if not defined MYSQL_BIN (
    for %%P in (
        "D:\mysql\bin\mysql.exe"
        "C:\mysql\bin\mysql.exe"
        "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe"
        "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
        "C:\Program Files\MySQL\MySQL Workbench 8.0\mysql.exe"
        "C:\Program Files\MySQL\MySQL Shell 8.0\bin\mysql.exe"
        "C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\mysql.exe"
        "C:\xampp\mysql\bin\mysql.exe"
        "C:\wamp64\bin\mysql\mysql8.0.31\bin\mysql.exe"
    ) do (
        if not defined MYSQL_BIN if exist %%~P set "MYSQL_BIN=%%~P"
    )
)

if not defined MYSQL_BIN (
    echo [错误] 找不到 mysql.exe。
    echo        本机若已装 MySQL Server / Workbench / Shell，把它们的 bin 目录加进 PATH 即可。
    echo        否则到 https://dev.mysql.com/downloads/installer/ 安装 MySQL Installer
    echo        勾选 MySQL Server 与 MySQL Workbench 任一组件，重跑此脚本。
    pause
    exit /b 1
)

echo 使用 mysql 客户端：%MYSQL_BIN%

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
echo 使用 user=%DB_USER% 连接 localhost MySQL，应用 schema.sql（已存在的表/记录会被保留）...
"%MYSQL_BIN%" -u %DB_USER% -p%DB_PASS% --default-character-set=utf8mb4 < "%SCHEMA%"

if errorlevel 1 (
    echo.
    echo [错误] schema.sql 执行失败。常见原因：
    echo   - MySQL 没启动 / 端口不是 3306
    echo   - 账号密码不对（默认 root/root，可用 DB_USERNAME / DB_PASSWORD 覆盖）
    pause
    exit /b 1
)

echo.
echo === 数据库 huaxin 已就绪 ===
echo 下一步：双击 start.bat 一键启动后端和管理端。
echo.
pause
endlocal
