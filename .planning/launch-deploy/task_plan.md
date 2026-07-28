# 小程序上线部署计划

## 目标
让华信通讯小程序具备可上线的公网后端部署能力，并最终通过微信体验版/正式版访问 HTTPS API。

## 阶段

### Phase 1: 本地上线准备
Status: complete
- 检查现有后端配置、数据库初始化脚本、小程序接口配置。
- 增加生产环境配置、健康检查接口和部署模板。
- 本地构建验证。

### Phase 2: 服务器初始化
Status: next
- 需要用户提供服务器公网 IP、系统版本、域名。
- 安装 Java 17、MySQL、Nginx。
- 初始化数据库并导入 `schema.sql`。

### Phase 3: 后端部署
Status: pending
- 上传 jar 和配置。
- 配置 systemd 服务。
- 验证 `http://127.0.0.1:8080/api/health`。

### Phase 4: HTTPS 与微信域名
Status: pending
- 配置 `api.<域名>` DNS 解析。
- 使用 Nginx + SSL 证书反代后端。
- 在微信公众平台配置 request 合法域名。

### Phase 5: 小程序上传发布
Status: pending
- 将 `config.js` 改为生产 HTTPS API。
- 上传体验版并真机测试。
- 提交审核并发布。

## 需要用户提供
- 服务器公网 IP
- 服务器系统，例如 Ubuntu 22.04
- 域名或计划使用的 API 子域名
- 是否已经备案
- 是否允许我通过 SSH 执行部署
