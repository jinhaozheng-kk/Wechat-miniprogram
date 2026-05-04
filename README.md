# 华信通讯科技有限公司 三端项目

> 端到端骨架已完成，可启动联调。

## 项目结构

```
华信通讯科技有限公司小程序/
├── huaxin-miniprogram/   # 微信小程序（消费端，已实现，本阶段不动）
├── client/               # Vue 3 + Element Plus 后台管理（端到端骨架已完成）
├── server/server/        # Spring Boot 4 + MyBatis 后端（端到端骨架已完成）
├── upload/               # 用户上传的图片/文件统一放这里
├── 产品/                 # 产品图片素材与需求文档
├── 项目需求.txt          # 顶层架构需求
└── CLAUDE.md             # Claude Code 协作指引
```

## 启动顺序

### 1) 初始化数据库（一次性）

确认本地 MySQL 已启动（端口 3306，用户 root，密码 root）。在仓库根目录执行：

```powershell
# 必须加 --default-character-set=utf8mb4，否则 Windows 下中文会被当成 GBK 导致 "Data too long" 错误
mysql -u root -p --default-character-set=utf8mb4 < server\server\src\main\resources\sql\schema.sql
```

脚本会执行以下操作：

- 删除并重建 `huaxin` 数据库
- 创建 `category` / `product` / `inquiry` 三张表
- 写入测试数据：14 个分类、约 30 条产品、8 条覆盖近 7 天的询价单

### 2) 启动后端（端口 8080）

> **依赖 JDK 17**。当前默认 `JAVA_HOME` 指向 Java 8，会编译失败。请先安装 JDK 17（推荐 Adoptium Temurin），并临时切换：
>
> ```powershell
> # 假设 JDK 17 安装在 C:\Java\jdk-17
> $env:JAVA_HOME = 'C:\Java\jdk-17'
> $env:Path = "$env:JAVA_HOME\bin;$env:Path"
> java -version  # 应显示 17.x
> ```

```powershell
cd server\server
.\mvnw.cmd spring-boot:run
```

启动成功后接口可访问：

- `GET  http://localhost:8080/api/categories` — 分类列表（公开）
- `GET  http://localhost:8080/api/products?categoryId=guang-xian` — 产品列表（公开）
- `POST http://localhost:8080/api/inquiry` — 提交询价单（公开）
- `POST http://localhost:8080/api/admin/login` — 后台登录
- `GET  http://localhost:8080/api/admin/stats` — 后台首页统计数据（需 token）

### 3) 启动前端（端口 5173）

```powershell
cd client
npm install
npm run dev
```

浏览器打开 <http://localhost:5173>，使用如下账号登录：

| 账号 | 密码     |
| ---- | -------- |
| admin | admin123 |

> Vite 已配置代理：`/api` 与 `/upload` 自动转发到后端 8080，前后端分离开发无需关心 CORS。

## 后台功能

| 模块     | 路径           | 说明                                                            |
| -------- | -------------- | --------------------------------------------------------------- |
| 首页     | `/dashboard`   | 4 个数据卡片 + 各分类产品数柱状图 + 询价状态饼图 + 7 日趋势折线图 |
| 分类管理 | `/categories`  | 分类列表（图标、编码、简介、产品数）                              |
| 产品管理 | `/products`    | 产品列表，支持按分类筛选 + 名称/型号关键字搜索                    |
| 询价管理 | `/inquiries`   | 询价单列表，支持按状态筛选与状态更新（待处理 / 已联系 / 已成交）  |

## 文件上传

- **统一存放路径**：仓库根目录 `upload/` 下，按日期分目录（`upload/yyyyMMdd/<uuid>.<ext>`）
- **后端写入路径**通过 `application.yml` 中的 `huaxin.upload-dir` 配置，默认 `../../upload`（相对后端启动工作目录 `server/server/`）。也可用环境变量 `HUAXIN_UPLOAD_DIR` 覆盖
- **HTTP 访问 URL**：`http://localhost:8080/upload/yyyyMMdd/xxx.jpg`
- 上传接口：`POST /api/admin/upload`（表单字段 `file`，需登录 token，仅放行 jpg/jpeg/png/webp/gif）

## 后端架构概览

```
com.example
├── ServerApplication        # @SpringBootApplication + @MapperScan
├── common                   # Result / BusinessException / GlobalExceptionHandler
├── config                   # HuaxinProperties / SecurityConfig / WebMvcConfig / AuthInterceptor
├── controller               # 6 个 REST 控制器
├── service                  # 6 个业务服务
├── mapper                   # 3 个 MyBatis Mapper（XML 在 resources/mapper/）
├── entity                   # Category / Product / Inquiry
├── dto                      # LoginRequest / LoginResponse / InquiryRequest / StatsResponse
└── typehandler              # JsonListTypeHandler（处理 DB JSON 数组字段）
```

- **认证**：登录返回固定 token（`huaxin-admin-token-2026`），前端放在请求头 `X-Auth-Token`；`AuthInterceptor` 拦截 `/api/admin/**`（登录接口除外）
- **CORS**：开发期放行所有来源（`WebMvcConfig`）
- **JSON 字段**：`product.images / specs / tags`、`inquiry.product_ids` 都是 MySQL JSON 类型，通过自定义 `JsonListTypeHandler` 与 Java `List<Object>` 互转

## 前端架构概览

```
client/src
├── main.js                  # 入口（element-plus 全局注册 + 中文 locale + icons）
├── App.vue                  # <router-view/>
├── style.css                # 全局基础样式
├── api                      # axios 实例 + 各资源 API 模块
├── stores/auth.js           # Pinia 登录态（localStorage 持久化）
├── router/index.js          # 路由 + 全局守卫
├── layouts/AdminLayout.vue  # 侧边栏 + 顶栏
└── views                    # Login / Dashboard / Categories / Products / Inquiries
```

## 当前未做

按"端到端骨架"约定，以下功能本阶段不做，留待后续迭代：

- 分类 / 产品的新增、编辑、删除与图片上传 UI
- 询价单详情、备注跟进、Excel 导出
- Banner 管理、用户中心、订阅消息推送
- 小程序的 API 接入（仍在用静态 `data/*.js`）
- 完整的用户体系（仍是硬编码账号）

## 联调验证清单

启动三端后建议按以下顺序验证：

1. 浏览器访问 `http://localhost:8080/api/categories`，应返回 `code: 0` 和 14 条分类
2. 浏览器访问 `http://localhost:8080/api/admin/stats`，应返回 `code: 401`（未登录）
3. 前端 `npm run dev` 启动，登录页输入 `admin / admin123`，应跳转到首页并看到 4 个卡片 + 3 个图表
4. 切换到"产品管理"，按分类筛选，应能拉到对应产品
5. 切换到"询价管理"，对一条 PENDING 询价点"更新状态"，刷新后状态应已变更

### 小程序联调

> 小程序与后端联调前，请在**微信开发者工具**「设置 → 项目设置」中勾选
> **不校验合法域名、HTTPS 证书、TLS 版本和 HSTS**——否则 `http://localhost:8080`
> 会被微信小程序网络白名单拦下。

1. 打开微信开发者工具 → 项目根目录指向 `D:\华信通讯科技有限公司小程序\huaxin-miniprogram\`
2. 编译后首页应显示 14 个分类（来自后端，而非本地静态数据）
3. 进入任意产品详情 → 点"立即询价" → 跳到询价页且已预填该产品
4. 填好联系人、手机号、需求 → 点"提交询价" → 应弹"询价提交成功"
5. 在 Vue 后台 `/inquiries` 页刷新，应能看到刚提交的那条询价单
