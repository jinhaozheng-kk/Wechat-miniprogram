# 上线部署发现

- 后端 Maven 模块位于 `server/server/`，Spring Boot 版本为 4.0.6，Java 17。
- 当前 `application.yml` 默认连接本机 MySQL：`jdbc:mysql://localhost:3306/huaxin`。
- 数据库初始化脚本位于 `server/server/src/main/resources/sql/schema.sql`，包含建库、建表、分类、产品和测试询价数据。
- 小程序接口配置位于 `huaxin-miniprogram/config.js`，当前仍为本地开发地址 `http://localhost:8080`。
- 体验版/正式版必须使用微信公众平台配置过的 HTTPS request 合法域名。
- 项目尚未发现 Docker、Nginx、systemd 或部署脚本。
- 已新增生产 profile：`server/server/src/main/resources/application-prod.yml`。
- 已新增健康检查接口：`GET /api/health`。
- 已新增部署模板目录：`deploy/`，包含环境变量、systemd、Nginx、Ubuntu 安装脚本和说明。
- 已选择微信云托管路线。
- 小程序已支持 `apiMode: 'cloudrun'`，通过 `wx.cloud.callContainer` 调用云托管服务；默认仍保持 `local`，避免未创建云环境前影响本地开发。
- 后端已新增 `server/server/Dockerfile`，可用于云托管构建 Spring Boot 服务。
