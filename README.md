# 华信通讯 · 官网小程序 + 管理后台（三端一体）

<p align="center">
  <img src="https://img.shields.io/badge/WeChat-MiniProgram-07C160?logo=wechat&logoColor=white" alt="WeChat MiniProgram">
  <img src="https://img.shields.io/badge/Vue-3.5-4FC08D?logo=vue.js&logoColor=white" alt="Vue 3">
  <img src="https://img.shields.io/badge/Element_Plus-2.9-409EFF" alt="Element Plus">
  <img src="https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 4">
  <img src="https://img.shields.io/badge/MyBatis-4.0.1-1E4A6B" alt="MyBatis">
  <img src="https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white" alt="MySQL 8">
  <img src="https://img.shields.io/badge/Java-17-orange" alt="Java 17">
</p>

为**华信通讯科技有限公司**（建筑电气配电箱 / 电表箱 / 端子箱等产品制造商）打造的一套**三端一体产品展示与询价系统**：

```text
用户（微信） ──> huaxin-miniprogram  微信小程序：产品目录、分类浏览、搜索、产品详情、在线询价
销售/运营    ──> client             Vue 3 + Element Plus 管理后台：数据看板、分类、产品、询价管理
两端共用     ──> server/server      Spring Boot 4 + MyBatis 后端：REST API、鉴权、文件上传、MySQL
                     └── MySQL 8（category / product / inquiry）+ upload/（图片按日期分目录）
```

三端已打通：小程序从后端拉取 14 个分类与产品数据，询价单真实落库；后台可查看统计、产品与询价单并更新询价状态。代码按"端到端骨架 + 可上线部署"整理，**生产上线所需的部署脚本、systemd 服务、Nginx/HTTPS 与微信云托管配置均已就绪**（见第 12 节）。

> 这是企业官网类项目：内容型三端系统，不涉及支付与用户体系；管理端账号目前是硬编码的最简方案（见"安全注意事项"）。

---

## 目录

- [1. 功能概览](#1-功能概览)
- [2. 技术栈](#2-技术栈)
- [3. 目录结构](#3-目录结构)
- [4. 快速开始](#4-快速开始)
- [5. 数据库设计](#5-数据库设计)
- [6. 后端设计](#6-后端设计)
- [7. 管理后台](#7-管理后台)
- [8. 微信小程序](#8-微信小程序)
- [9. 文件上传](#9-文件上传)
- [10. 配置与环境变量](#10-配置与环境变量)
- [11. 三端联调验证清单](#11-三端联调验证清单)
- [12. 部署上线](#12-部署上线)
- [13. 测试](#13-测试)
- [14. 安全注意事项](#14-安全注意事项)
- [15. 已知限制与后续规划](#15-已知限制与后续规划)
- [16. 许可与素材说明](#16-许可与素材说明)

---

## 1. 功能概览

### 微信小程序（消费端，`huaxin-miniprogram/`）

| 页面 | 数据来源 | 关键行为 |
| --- | --- | --- |
| `pages/home/home` | `api.listCategories()` | Banner + 分类网格 + 快捷询价 CTA |
| `pages/products/products` | `api.listProducts({ keyword })` | 关键字搜索（300ms 节流）+ sticky 搜索栏 |
| `pages/category/category` | `api.getCategory(id)` + `api.listProducts({ categoryId })` | 分类下产品列表 |
| `pages/detail/detail` | `api.getProduct(id)` + `api.getCategory(...)` | 规格表 + "立即询价"（跳转并携带产品 id） |
| `pages/inquiry/inquiry` | `api.submitInquiry()` | 完整询价表单（7 字段）+ 提交成功视图 |
| `pages/about/about` | `data/company.js`（静态） | 公司简介、联系方式、资质 |

- 6 个页面 + **自定义 tabBar**（首页 / 产品 / 询价 / 关于），`lazyCodeLoading: requiredComponents`；
- 尺寸使用 `rpx`，每个页面由 `.js/.json/.wxml/.wxss` 四件套组成；
- 网络层：`utils/request.js` Promise 封装 + 统一拆包 `{ code, message, data }`；页面只调用 `utils/api.js`；
- 支持两种 API 模式（`config.js`）：`local`（`wx.request` → `http://localhost:8080`）与 `cloudrun`（`wx.cloud.callContainer` → 微信云托管）。

### 管理后台（`client/`，Vue 3 + Element Plus）

| 模块 | 路由 | 说明 |
| --- | --- | --- |
| 登录 | `/login` | 账号密码登录，token 存 localStorage |
| 数据看板 | `/dashboard` | 4 个统计卡片 + 分类产品数柱状图 + 询价状态饼图 + 近 7 日趋势折线图（ECharts） |
| 分类管理 | `/categories` | 分类列表（图标、编码、简介、产品数） |
| 产品管理 | `/products` | 产品列表，按分类筛选 + 名称/型号关键字搜索 |
| 询价管理 | `/inquiries` | 询价单列表，按状态筛选、更新状态（待处理 / 已联系 / 已成交） |

### 后端（`server/server/`）

- 公开接口：分类、产品、产品详情、提交询价、健康检查；
- 管理接口（`X-Auth-Token` 保护）：登录、统计、询价列表/状态更新、文件上传；
- 统一响应 `Result<T>`、全局异常处理、业务异常、MyBatis XML 映射、JSON 字段 TypeHandler。

## 2. 技术栈

| 端 | 技术 | 版本 |
| --- | --- | --- |
| 小程序 | 微信小程序原生框架 | 基础库 3.15.2，AppID `wxab8299303b8f0174` |
| 管理后台 | Vue 3 + Vite | Vue `^3.5.32`、Vite `^8.0.10` |
| 管理后台 UI | Element Plus + Icons + ECharts | `^2.9.0` / `^2.3.1` / `^5.5.1` |
| 管理后台状态/路由 | Pinia + Vue Router | `^2.3.0` / `^4.5.0` |
| 管理后台请求 | axios（开发代理 `/api`、`/upload`） | `^1.7.9` |
| 后端框架 | Spring Boot（WebMVC + Security） | `4.0.6` |
| 后端 ORM | MyBatis Spring Boot Starter | `4.0.1` |
| 数据库 | MySQL 8（utf8mb4） | 端口 3306 |
| JDK | Java 17 | 必须 17（Java 8 会编译失败） |
| 构建 | Maven Wrapper（`mvnw` / `mvnw.cmd`）、npm | — |

## 3. 目录结构

```text
Wechat-miniprogram/
├─ huaxin-miniprogram/          # 微信小程序（消费端）
│  ├─ app.js / app.json / app.wxss
│  ├─ config.js                 # API 模式与 baseURL / 云托管环境
│  ├─ custom-tab-bar/           # 自定义 tabBar
│  ├─ pages/{home,products,category,detail,inquiry,about}/
│  ├─ utils/{request,api}.js    # 网络封装与业务 API
│  ├─ data/{company,categories,products}.js  # company 仍用；分类/产品为历史兜底
│  ├─ images/                   # 分类与产品图（小程序内）
│  └─ product.config.json       # AppID、编译设置、打包忽略规则
├─ client/                      # Vue 3 + Element Plus 管理后台
│  ├─ src/
│  │  ├─ api/{http,auth,categories,products,inquiries,stats}.js
│  │  ├─ stores/auth.js         # Pinia 登录态（localStorage 持久化）
│  │  ├─ router/index.js        # 路由 + 全局守卫
│  │  ├─ layouts/AdminLayout.vue
│  │  └─ views/{Login,Dashboard,Categories,Products,Inquiries}View.vue
│  └─ vite.config.js            # 开发代理 /api、/upload → 8080
├─ server/server/               # Spring Boot 4 后端
│  ├─ src/main/java/com/example/
│  │  ├─ ServerApplication.java
│  │  ├─ common/                # Result / BusinessException / GlobalExceptionHandler
│  │  ├─ config/                # Security / WebMvc / AuthInterceptor / HuaxinProperties
│  │  ├─ controller/            # Auth / Category / Product / Inquiry / Stats / Upload / Health
│  │  ├─ service/               # 6 个业务服务
│  │  ├─ mapper/                # 3 个 MyBatis Mapper（XML 在 resources/mapper/）
│  │  ├─ entity/                # Category / Product / Inquiry
│  │  ├─ dto/                   # Login / Inquiry / Stats
│  │  └─ typehandler/           # JsonListTypeHandler（MySQL JSON ↔ List）
│  ├─ src/main/resources/
│  │  ├─ application.yml        # 本地开发配置
│  │  ├─ application-prod.yml   # 生产配置（环境变量覆盖）
│  │  ├─ mapper/*.xml
│  │  └─ sql/schema.sql         # 幂等初始化（14 分类 / ~30 产品 / 8 询价）
│  ├─ Dockerfile                # temurin 17 JRE + prod profile
│  └─ cloudbaserc.json          # 微信云托管环境与服务名
├─ upload/                      # 用户/后台上传的图片（按 yyyyMMdd 分目录）
├─ deploy/                      # 上线部署资产
│  ├─ deploy-ubuntu.sh          # Ubuntu 一键安装脚本
│  ├─ huaxin-server.service     # systemd 服务
│  ├─ nginx-api.conf.example    # Nginx 反代模板
│  ├─ env.example               # 生产环境变量模板
│  └─ WECHAT_CLOUDRUN.md        # 微信云托管部署说明
├─ scripts/share.test.js        # 分享路径小测试
├─ .planning/launch-deploy/     # 上线计划、进度与发现
├─ 产品/                        # 原始产品图、规格 PDF、营业执照与需求文档
├─ 图片备份/                    # 产品图片备份（373 个文件）
├─ 项目需求.txt                 # 顶层架构需求
├─ init-db.bat / start.bat      # Windows 一键初始化 / 启动脚本
├─ AGENTS.md / CLAUDE.md        # AI 编码助手工作指引
└─ README.md
```

## 4. 快速开始

### 4.1 初始化数据库（一次性）

前置：本地 MySQL 已启动（端口 3306，默认 `root/root`）。在仓库根目录执行：

```powershell
# 必须加 --default-character-set=utf8mb4，否则 Windows 下中文会被当成 GBK 导致 "Data too long"
mysql -u root -p --default-character-set=utf8mb4 < server\server\src\main\resources\sql\schema.sql
```

脚本是**幂等**的：数据库/表不存在才创建，分类与产品按主键跳过已存在记录，询价单仅在表为空时插入示例数据。会写入 14 个分类、约 30 条产品、8 条覆盖近 7 天的询价单。

### 4.2 启动后端（端口 8080）

> **依赖 JDK 17**。若默认 `JAVA_HOME` 指向 Java 8，会编译失败。先安装 JDK 17（推荐 Adoptium Temurin）并切换：

```powershell
$env:JAVA_HOME = 'C:\Java\jdk-17'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java -version   # 应显示 17.x

cd server\server
.\mvnw.cmd spring-boot:run
```

启动后接口：

- `GET  /api/health` — 健康检查（公开）
- `GET  /api/categories` — 分类列表（公开）
- `GET  /api/products?categoryId=guang-xian` — 产品列表（公开）
- `GET  /api/products/{id}` — 产品详情（公开）
- `POST /api/inquiry` — 提交询价单（公开）
- `POST /api/admin/login` — 后台登录
- `GET  /api/admin/stats` — 后台统计（需 token）

### 4.3 启动管理后台（端口 5173）

```powershell
cd client
npm install
npm run dev
```

浏览器打开 <http://localhost:5173>，登录账号（开发默认）：

| 账号 | 密码 |
| --- | --- |
| `admin` | `admin123` |

Vite 已配置代理：`/api` 与 `/upload` 自动转发到 `http://localhost:8080`，开发期无需处理 CORS。

### 4.4 打开小程序

1. 用**微信开发者工具**打开 `huaxin-miniprogram/`（AppID `wxab8299303b8f0174`）；
2. 开发期在「设置 → 项目设置」勾选**不校验合法域名、HTTPS 证书、TLS 版本和 HSTS**，否则 `http://localhost:8080` 会被拦；
3. `config.js` 中 `apiMode: 'local'` 走本地后端；`'cloudrun'` 走微信云托管；
4. 编译后首页应显示 14 个分类（来自后端，而非本地静态数据）。

### 4.5 Windows 一键脚本

- `init-db.bat`：初始化数据库；
- `start.bat`：启动后端与管理后台（按脚本内说明）。

## 5. 数据库设计

`schema.sql` 定义 3 张表（utf8mb4）：

| 表 | 主键 | 关键字段 | 说明 |
| --- | --- | --- | --- |
| `category` | `id VARCHAR(32)`（拼音编码，如 `guang-xian`） | `name/icon/color/bg_color/image/description/product_count/sort_order` | 产品分类，冗余 `product_count` 便于展示 |
| `product` | `id VARCHAR(32)` | `category_id/name/model/images(JSON)/specs(JSON)/tags(JSON)/description` | 产品；`images/specs/tags` 为 MySQL JSON |
| `inquiry` | `id BIGINT AUTO_INCREMENT` | `product_ids(JSON)/product_names/contact_name/contact_phone/project_name/city/requirement/remark/status` | 询价单；状态 `PENDING / CONTACTED / CLOSED` |

- JSON 字段通过自定义 `JsonListTypeHandler` 与 Java `List<Object>` 互转；
- 索引：`product.idx_category`、`inquiry.idx_status`、`inquiry.idx_created`，便于按分类/状态/时间查询；
- 时间字段默认 `CURRENT_TIMESTAMP`，更新自动刷新。

## 6. 后端设计

```text
com.example
├─ ServerApplication        # @SpringBootApplication + @MapperScan
├─ common                   # Result / BusinessException / GlobalExceptionHandler
├─ config                   # HuaxinProperties / SecurityConfig / WebMvcConfig / AuthInterceptor
├─ controller               # Auth / Category / Product / Inquiry / Stats / Upload / Health
├─ service                  # Auth / Category / Product / Inquiry / Stats / Upload
├─ mapper                   # Category / Product / Inquiry（XML 在 resources/mapper/）
├─ entity                   # Category / Product / Inquiry
├─ dto                      # LoginRequest / LoginResponse / InquiryRequest / StatsResponse
└─ typehandler              # JsonListTypeHandler
```

- **统一响应**：`Result<T>`（`code/message/data`），前端统一拆包；
- **鉴权**：登录成功后返回固定 token（默认 `huaxin-admin-token-2026`），前端放在请求头 `X-Auth-Token`；`AuthInterceptor` 拦截 `/api/admin/**`（登录接口除外）；
- **CORS**：开发期放行所有来源（`WebMvcConfig`），生产由 Nginx 同源反代；
- **上传**：`UploadService` 校验扩展名、生成 UUID 文件名、按日期目录落盘；
- **配置**：`HuaxinProperties` 承载 `huaxin.upload-dir`、`huaxin.upload-url-prefix`、`huaxin.admin.*`，均支持环境变量覆盖。

## 7. 管理后台

```text
client/src
├─ main.js                  # 入口：Element Plus 全局注册 + 中文 locale + icons + Pinia + Router
├─ App.vue / style.css
├─ api/                     # http（axios 实例/拦截器）+ auth/categories/products/inquiries/stats
├─ stores/auth.js           # 登录态（localStorage 持久化）
├─ router/index.js          # 路由 + 全局守卫（未登录跳 /login）
├─ layouts/AdminLayout.vue  # 侧边栏 + 顶栏
└─ views/                   # Login / Dashboard / Categories / Products / Inquiries
```

- 看板用 ECharts 渲染分类产品数柱状图、询价状态饼图、7 日趋势折线图；
- API 层与视图分离，新增页面时只需在 `api/` 增加模块、在 `router` 注册；
- 构建：`npm run build` 输出 `dist/`，可交给 Nginx 静态托管。

## 8. 微信小程序

### 网络层

```text
config.js          # apiMode(local/cloudrun)、baseURL、云托管 env/service
utils/request.js   # wx.request 的 Promise 封装；自动拆 { code, message, data }；失败 toast
utils/api.js       # listCategories / listProducts / getProduct / getCategory / submitInquiry
```

页面通过 `require('../../utils/api')` 调用业务方法，不直接接触 `wx.request`。

### 数据流现状

- **分类 / 产品 / 产品详情**：全部来自后端 API；
- **询价提交**：真实落库（`POST /api/inquiry`）；
- **公司信息**：仍为静态 `data/company.js`（`about`、`inquiry`、`detail` 读取）；
- `data/categories.js`、`data/products.js` 已不再被页面引用，保留作历史参考与后端不可达时的兜底素材；
- 旧版的产品对比功能（`compareList`）已在 v3 移除。

## 9. 文件上传

- **存放路径**：仓库根目录 `upload/`，按日期分目录：`upload/yyyyMMdd/<uuid>.<ext>`；
- **后端配置**：`application.yml` 的 `huaxin.upload-dir`（默认 `../../upload`，相对于后端启动目录 `server/server/`），可用 `HUAXIN_UPLOAD_DIR` 覆盖；
- **访问 URL**：`http://localhost:8080/upload/yyyyMMdd/xxx.jpg`；生产经 Nginx 同源转发；
- **接口**：`POST /api/admin/upload`（表单字段 `file`，需登录 token）；
- **限制**：单文件 10 MB、请求 20 MB；仅放行 `jpg/jpeg/png/webp/gif`。

## 10. 配置与环境变量

`application.yml`（本地默认）：

| 配置 | 默认值 | 说明 |
| --- | --- | --- |
| `server.port` | `8080` | 后端端口 |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/huaxin?...` | MySQL 8，utf8mb4 |
| `spring.datasource.username/password` | `root/root` | 仅本地开发 |
| `spring.servlet.multipart.*` | 10MB / 20MB | 上传大小限制 |
| `huaxin.upload-dir` | `../../upload` | 上传目录（可用 `HUAXIN_UPLOAD_DIR` 覆盖） |
| `huaxin.upload-url-prefix` | `/upload` | 对外 URL 前缀 |
| `huaxin.admin.username/password/token` | `admin/admin123/huaxin-admin-token-2026` | 开发默认，生产必须用环境变量覆盖 |

`application-prod.yml`（`--spring.profiles.active=prod` 启用）：

- 监听地址默认 `127.0.0.1`（只对 Nginx 暴露），端口 `SERVER_PORT`；
- 数据库通过 `DB_HOST/DB_NAME/DB_USERNAME/DB_PASSWORD` 覆盖；
- 启动时执行 `classpath:sql/schema-cloud.sql` 初始化（云环境）；
- 上传目录默认 `/tmp/huaxin-upload`（云托管无持久磁盘，需挂载或改 OSS）；
- 关闭 MyBatis SQL 日志。

生产环境变量模板见 `deploy/env.example` 与 `deploy/cloudrun-env.example`。

## 11. 三端联调验证清单

1. 访问 `http://localhost:8080/api/categories` → `code: 0` + 14 条分类；
2. 访问 `http://localhost:8080/api/admin/stats`（未带 token）→ `code: 401`；
3. 前端登录 `admin / admin123` → 跳转首页，看到 4 个卡片 + 3 个图表；
4. 产品管理按分类筛选 → 能拉到对应产品；
5. 询价管理对一条 `PENDING` 记录更新状态 → 刷新后已变更；
6. 小程序首页显示 14 个分类（来自后端）；
7. 产品详情 →「立即询价」→ 询价页已预填该产品；
8. 填表提交 → 提示"询价提交成功"；后台 `/inquiries` 能看到新记录。

## 12. 部署上线

两种方案均已配置：

### 方案 A：Ubuntu + Nginx + systemd（`deploy/`）

```bash
# 1) 服务器准备：Ubuntu 22.04/24.04，安全组放行 22/80/443，不要对公网开 3306
# 2) 一键安装
sudo bash deploy-ubuntu.sh api.example.com

# 3) 初始化数据库（创建业务用户 + 导入 schema.sql）
# 4) 打包上传后端
cd server/server && ./mvnw clean package -DskipTests
scp target/server-0.0.1-SNAPSHOT.jar root@SERVER_IP:/opt/huaxin/server.jar

# 5) 配置环境变量并安装 systemd 服务
sudo cp deploy/env.example /etc/huaxin/huaxin-server.env   # 修改 DB_PASSWORD / ADMIN_PASSWORD / ADMIN_TOKEN
sudo cp deploy/huaxin-server.service /etc/systemd/system/
sudo systemctl daemon-reload && sudo systemctl enable --now huaxin-server

# 6) HTTPS（Nginx + certbot）
sudo cp deploy/nginx-api.conf.example /etc/nginx/sites-available/huaxin-api
sudo certbot --nginx -d api.example.com
```

微信公众平台 → 开发管理 → 服务器域名，添加 `request/uploadFile/downloadFile` 合法域名 `https://api.example.com`，再把 `huaxin-miniprogram/config.js` 的 `baseURL` 改成该域名、`apiMode` 改为 `local`（或继续用 `cloudrun`）。

### 方案 B：微信云托管（`cloudbaserc.json` + `Dockerfile`）

```bash
cd server/server
docker build -t huaxin-server .
# 按 deploy/WECHAT_CLOUDRUN.md 推送镜像到云托管
```

`cloudbaserc.json` 已配置环境 `prod-d9gq3q3ib3c798208`、服务 `springboot-44kf`；小程序 `config.js` 的 `apiMode: 'cloudrun'` 会通过 `wx.cloud.callContainer` 调用。云托管无持久磁盘，上传目录需改为 OSS 或挂载存储。

### 部署资产一览

| 文件 | 用途 |
| --- | --- |
| `deploy/deploy-ubuntu.sh` | 服务器一键安装脚本 |
| `deploy/huaxin-server.service` | systemd 服务单元 |
| `deploy/nginx-api.conf.example` | Nginx 反向代理 + 上传大小配置 |
| `deploy/env.example` | 生产环境变量模板 |
| `deploy/cloudrun-env.example` | 云托管环境变量模板 |
| `deploy/WECHAT_CLOUDRUN.md` | 微信云托管部署说明 |
| `server/server/Dockerfile` | JRE 17 运行镜像（prod profile） |
| `.planning/launch-deploy/` | 上线计划、进度与问题记录 |

## 13. 测试

- 后端：`cd server\server && .\mvnw.cmd test`（目前只有 `ServerApplicationTests` 的上下文加载测试）；
- 小程序：无测试框架，`scripts/share.test.js` 是一个分享路径的小型 Node 脚本；
- 管理后台：无测试配置；
- 主要验证方式仍是第 11 节的手工联调清单。

## 14. 安全注意事项

这是**企业展示型系统**，但仍需注意：

1. **默认凭据必须更换**：`admin/admin123`、`huaxin-admin-token-2026`、MySQL `root/root` 都只是开发默认值；生产用 `HUAXIN_ADMIN_PASSWORD`、`HUAXIN_ADMIN_TOKEN`、`DB_PASSWORD` 覆盖，且 token 要足够随机；
2. **固定 token 不是完整鉴权**：没有过期、刷新、多用户与权限体系；对外运营前建议接入数据库用户 + JWT/Session；
3. **CORS**：开发期放行所有来源，生产应由 Nginx 同源反代并收紧；
4. **HTTPS**：小程序只请求 HTTPS 域名；本地调试的"不校验域名"绝不能用于线上；
5. **上传校验**：目前只校验扩展名与大小，生产建议增加内容类型/MIME 校验、图片重编码、病毒扫描；
6. **仓库含真实产品图与营业执照 PDF**：公开仓库请确认这些素材可以公开；
7. **数据库端口**：不要对公网开放 3306。

## 15. 已知限制与后续规划

按"端到端骨架"约定，以下功能尚未实现：

- 分类 / 产品的新增、编辑、删除与图片上传 UI（只有列表与查询）；
- 询价单详情、备注跟进、Excel 导出；
- Banner 管理、用户中心、订阅消息推送；
- 完整用户体系（仍是硬编码账号 + 固定 token）；
- 小程序公司信息仍为静态文件（未接后端 company 接口）；
- 无自动化测试与 CI；
- 云托管方案的上传目录无持久化，需要 OSS 或挂载；
- 仓库体积较大（约 231 MB，主要是 `产品/` 与 `图片备份/` 的图片素材），建议后续用 Git LFS 或只保留必要素材；
- 仓库未附带 LICENSE 文件（企业项目，默认保留所有权利）。

## 16. 许可与素材说明

- 代码与文档未附带 LICENSE，默认保留所有权利；
- `产品/`、`图片备份/`、小程序 `images/` 中的产品图片、规格 PDF、营业执照等素材归华信通讯科技有限公司所有，仅用于该企业官网项目；
- 第三方依赖遵循各自许可证（Vue MIT、Element Plus MIT、ECharts Apache-2.0、Spring Boot Apache-2.0、MyBatis Apache-2.0 等）。
