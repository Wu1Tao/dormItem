@echo off
chcp 65001 >nul
title Dormitory Management - Build EXE

echo ========================================
echo  Dormitory Management - 构建 EXE
echo ========================================
echo.

REM 检查 Maven
where mvn >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [错误] 未找到 Maven，请确保已安装并配置 MAVEN_HOME
    pause
    exit /b 1
)

REM 检查 Java
where java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [错误] 未找到 Java，请确保已安装 JDK 8+
    pause
    exit /b 1
)

echo [1/2] 编译打包 JAR ...
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo [错误] 编译失败，请检查错误信息
    pause
    exit /b 1
)
echo [完成] JAR 打包成功

echo [2/2] 生成 EXE ...
REM Launch4j 插件会在 package 阶段自动执行，所以上面一步已生成 EXE
echo [完成] EXE 生成成功

echo.
echo ========================================
echo  构建完成！
echo.
echo  JAR 文件：application\target\application-1.0.0.jar
echo  EXE 文件：application\target\DormitoryManagement.exe
echo.
echo  使用方法：双击 DormitoryManagement.exe 启动
echo  然后在浏览器打开 http://localhost:8080
echo.
echo  注意：
echo  - 确保 PostgreSQL 已启动
echo  - 如需自定义配置，在 exe 同目录下放 application.yml
echo ========================================

pause
