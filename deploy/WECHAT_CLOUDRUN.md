# 微信云托管上线说明

本项目已经支持两种小程序请求模式：

- `local`：本地开发，走 `wx.request` 请求 `http://localhost:8080`
- `cloudrun`：体验版/正式版，走 `wx.cloud.callContainer` 请求微信云托管

## 1. 创建云开发环境

在微信开发者工具或微信公众平台里开通云开发，记录环境 ID，例如：

```text
prod-xxxxxx
```

当前项目已配置的环境 ID：

```text
prod-d9gq3q3ib3c798208
```

## 2. 创建云托管服务

在云开发控制台进入「云托管」，创建 Spring Boot 服务。

服务名建议：

```text
huaxin-api
```

当前项目已配置的服务名：

```text
springboot-44kf
```

代码目录选择：

```text
server/server
```

构建方式选择 Dockerfile，项目已提供：

```text
server/server/Dockerfile
```

容器端口：

```text
8080
```

## 3. 配置 MySQL

云托管需要可访问的 MySQL。可以使用云托管配套 MySQL 或云数据库 MySQL。

如果使用云托管模板自带 MySQL，本项目会在容器启动时自动执行：

```text
server/server/src/main/resources/sql/schema-cloud.sql
```

云托管模板通常已经注入这些变量：

```text
MYSQL_ADDRESS=host:port
MYSQL_USERNAME
MYSQL_PASSWORD
```

如果改用自定义 MySQL，请在云托管环境变量中填写：

```text
DB_HOST=host:port
DB_NAME
DB_USERNAME
DB_PASSWORD
```

环境变量模板见：

```text
deploy/cloudrun-env.example
```

## 4. 部署后端

每次部署前先在本地打包：

```bash
cd server/server
./mvnw -DskipTests package
```

当前 Dockerfile 会把本地生成的 `target/server-0.0.1-SNAPSHOT.jar` 放进镜像，避免云端 Maven 构建时因为网络依赖失败导致发布失败。

部署完成后，在云托管控制台确认服务运行正常。

注意：如果 `/api/count` 能访问，但 `/api/health` 或 `/api/categories` 返回 404，说明云托管里仍然运行微信 Spring Boot 模板代码，还没有部署本项目后端。

健康检查接口：

```text
/api/health
```

分类接口：

```text
/api/categories
```

## 5. 修改小程序云托管配置

修改：

```text
huaxin-miniprogram/config.js
```

示例：

```js
module.exports = {
  apiMode: 'cloudrun',
  baseURL: 'http://localhost:8080',
  cloud: {
    enabled: true,
    env: 'prod-d9gq3q3ib3c798208',
    service: 'springboot-44kf',
  },
};
```

## 6. 体验版测试

重新上传体验版后，重点测试：

- 首页分类加载
- 产品列表加载
- 产品详情加载
- 搜索
- 询价提交

如果分类和产品能显示，但询价失败，优先看云托管服务日志和 MySQL 连接配置。

## 注意事项

- 云托管模式下，小程序不需要直接配置 `request` 合法域名来访问自己的云托管服务。
- 后台上传文件目前会保存到容器本地目录。容器重建后本地文件可能丢失；正式长期使用建议接入对象存储。
- `HUAXIN_ADMIN_PASSWORD` 和 `HUAXIN_ADMIN_TOKEN` 必须改成强密码/随机长 token。
