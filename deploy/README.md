# 华信小程序后端上线部署说明

这套部署按以下方式运行：

- Spring Boot 后端监听 `127.0.0.1:8080`
- Nginx 对外提供 `https://api.example.com`
- MySQL 与后端先放同一台服务器
- 小程序只请求 HTTPS API 域名

## 服务器准备

推荐系统：Ubuntu 22.04 或 Ubuntu 24.04。

服务器安全组需要放行：

- `22/tcp` SSH
- `80/tcp` HTTP 证书申请和跳转
- `443/tcp` HTTPS API

不要对公网开放 MySQL `3306`。

## 首次安装

把 `deploy/deploy-ubuntu.sh` 上传到服务器后执行：

```bash
sudo bash deploy-ubuntu.sh api.example.com
```

把 `api.example.com` 换成真实 API 域名。

## 数据库初始化

登录 MySQL 后创建业务用户：

```sql
CREATE USER 'huaxin'@'localhost' IDENTIFIED BY 'CHANGE_ME_STRONG_DB_PASSWORD';
GRANT ALL PRIVILEGES ON huaxin.* TO 'huaxin'@'localhost';
FLUSH PRIVILEGES;
```

导入初始化 SQL：

```bash
mysql -u root -p --default-character-set=utf8mb4 < server/server/src/main/resources/sql/schema.sql
```

## 后端部署

本地打包：

```bash
cd server/server
./mvnw clean package -DskipTests
```

上传：

```bash
scp target/server-0.0.1-SNAPSHOT.jar root@SERVER_IP:/opt/huaxin/server.jar
```

复制环境变量模板：

```bash
sudo mkdir -p /etc/huaxin
sudo cp deploy/env.example /etc/huaxin/huaxin-server.env
sudo nano /etc/huaxin/huaxin-server.env
```

必须修改：

- `DB_PASSWORD`
- `HUAXIN_ADMIN_PASSWORD`
- `HUAXIN_ADMIN_TOKEN`

安装 systemd 服务：

```bash
sudo cp deploy/huaxin-server.service /etc/systemd/system/huaxin-server.service
sudo systemctl daemon-reload
sudo systemctl enable --now huaxin-server
sudo systemctl status huaxin-server
```

本机验证：

```bash
curl http://127.0.0.1:8080/api/health
curl http://127.0.0.1:8080/api/categories
```

## HTTPS 与 Nginx

申请证书：

```bash
sudo certbot --nginx -d api.example.com
```

安装 Nginx 配置模板时，把 `api.example.com` 替换成真实域名：

```bash
sudo cp deploy/nginx-api.conf.example /etc/nginx/sites-available/huaxin-api
sudo nano /etc/nginx/sites-available/huaxin-api
sudo ln -sf /etc/nginx/sites-available/huaxin-api /etc/nginx/sites-enabled/huaxin-api
sudo nginx -t
sudo systemctl reload nginx
```

公网验证：

```bash
curl https://api.example.com/api/health
curl https://api.example.com/api/categories
```

## 微信小程序配置

微信公众平台：

```text
开发管理 -> 开发设置 -> 服务器域名
```

添加：

```text
request 合法域名：https://api.example.com
uploadFile 合法域名：https://api.example.com
downloadFile 合法域名：https://api.example.com
```

然后修改小程序：

```js
// huaxin-miniprogram/config.js
module.exports = {
  baseURL: 'https://api.example.com',
};
```

重新上传体验版，真机测试后提交审核。
