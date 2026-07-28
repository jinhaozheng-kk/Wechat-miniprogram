# 上线部署进度

## 2026-07-05
- 用户已购买服务器。
- 已检查后端模块、数据库脚本和小程序接口配置。
- 开始补齐生产部署文件。
- 已新增 `HealthController`、`application-prod.yml`、`deploy/` 部署模板。
- 已执行 `server/server/.\\mvnw.cmd -DskipTests package`，结果 BUILD SUCCESS，生成 `target/server-0.0.1-SNAPSHOT.jar`。
- 已新增微信云托管说明 `deploy/WECHAT_CLOUDRUN.md` 和云托管环境变量模板 `deploy/cloudrun-env.example`。
- 已修改小程序 `config.js`、`app.js`、`utils/request.js`，支持本地 HTTP 与云托管 callContainer 双模式。
- 已验证小程序 JS 语法、请求封装本地模式、后端 Maven 打包、微信开发者工具 preview，均通过。
- 已安装 CloudBase CLI `3.5.9`。
- 已尝试 `tcb login --flow device`，生成用户码 `GVEJ-L8QU`，但授权未完成；登录进程已停止，等待用户完成授权后继续部署。
