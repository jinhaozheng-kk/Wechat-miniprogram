# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

这是华信通讯科技有限公司的微信小程序——一个建筑电气箱体的产品目录与询价应用。**自 v2 起已对接 Spring Boot 后端**：

- 分类、产品、产品详情数据均从后端 API 拉取
- 询价单提交真正落库（`POST /api/inquiry`）
- 公司信息（电话、地址、资质等）仍以静态形式保存在 `data/company.js` 中（暂未接入后端 company 接口）

## Development Workflow

**No build commands exist.** Open the project in [WeChat Developer Tools](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html) using App ID `wxab8299303b8f0174`.

**对接后端前置：** 开发期 baseURL 是 `http://localhost:8080`（明文 HTTP），需要在微信开发者工具
**「设置 → 项目设置 → 不校验合法域名、HTTPS 证书、TLS 版本和 HSTS」** 中勾选放行；否则所有 `wx.request` 都会被拦下。

- **Run/preview**: Click "Compile" or "Preview" in WeChat Developer Tools
- **Upload to production**: "Upload" button in the IDE (requires developer account)
- **No test framework is configured**

## Architecture

### 网络层

```
config.js                  # baseURL（开发：http://localhost:8080）
utils/request.js           # wx.request 的 Promise 封装；自动拆 { code, message, data }
utils/api.js               # 业务方法：listCategories / listProducts / getProduct / submitInquiry / ...
```

页面通过 `const api = require('../../utils/api')` 调用业务方法，不直接接触 `wx.request`。
失败时 `request.js` 会自动 `wx.showToast`，调用方一般只需 `try/catch` 兜底即可。

### Data Layer (`data/`)

仅 `data/company.js` 仍被使用（被 `pages/about`、`pages/inquiry`、`pages/detail` 等读取，用于电话、公司名等展示）。

`data/categories.js` 与 `data/products.js` 已**不再被任何页面引用**——保留作为历史参考与后端不可达时的本地兜底素材。

### Global State (`app.js`)

`app.globalData` 仅含一项：

1. `company` — loaded from `data/company.js`

> 旧版本曾有 `compareList` 与产品对比功能，已在 v3 移除。

### Pages (`pages/`)

| Page | 数据来源 | 关键行为 |
|------|---------|----------|
| `home` | `api.listCategories()` | Banner + 分类网格 + 快捷询价 CTA |
| `products` | `api.listProducts({ keyword })` | 关键字搜索（300ms 节流，sticky 搜索栏） |
| `category` | `api.getCategory(id)` + `api.listProducts({ categoryId })` | 分类下产品列表 |
| `detail` | `api.getProduct(id)` + `api.getCategory(...)` | 规格表 + 立即询价（跳询价页携产品 id） |
| `inquiry` | `api.submitInquiry()` | 完整表单（7 字段）+ 提交成功视图 |
| `about` | `data/company.js`（静态） | 公司简介、联系方式、资质 |

### 询价页进入方式

- 详情页"立即询价" → `?productId=xxx`，`onLoad` 调 `getProduct` 预填 1 项
- 其他外部入口（如分享链接） → `?productIds=a,b,c`，并行预填多项
- TabBar 直接进入：产品区为空，引导用户去浏览产品

### Custom Tab Bar (`custom-tab-bar/`)

仍为自定义组件。每个页面在 `onShow` 调 `this.getTabBar().setData({ active: N })` 同步选中态。Tabs：Home(0)、Products(1)、Inquiry(2)、About(3)。

### Navigation Patterns

- Tab 间：`wx.switchTab({ url: '/pages/...' })`
- 推子页：`wx.navigateTo({ url: '/pages/detail/detail?id=...' })`
- 参数走 URL query string，在 `onLoad(options)` 中读取

## Key Conventions

- **Styling units**: 一律 `rpx`，不用 `px`
- **Language**: UI 文案与代码注释为简体中文
- **Component files**: 每个页面/组件 4 个文件：`.js`、`.json`、`.wxml`、`.wxss`
- **Page JSON**: 必须含 `{ "usingComponents": {} }`，即使为空
- **网络请求**：所有业务请求过 `utils/api.js`；新增接口时同步加方法。**不要在页面里直接写 `wx.request`**——会绕开统一错误处理与 baseURL 拼接
