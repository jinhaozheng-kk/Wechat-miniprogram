# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在本仓库中工作时提供指引。

## 项目目标（来自 `项目需求.txt`）

为华信通讯科技有限公司（中国建筑电气配电箱制造商）做一套**展示各类产品各种型号**的系统，要求页面美观。架构为**三端一体**：

- **小程序**（消费端）→ `huaxin-miniprogram/`
- **后端**：Spring Boot 4 → `server/`（实际 Maven 模块在 `server/server/`）
- **前端**：Vue 3 + **Element Plus** → `client/`

> Element Plus 是需求中明确指定的 UI 库。当前 `client/package.json` 里**还没有装** `element-plus`——首次在 `client/` 干活时需要先 `npm install element-plus` 并在 `main.js` 里注册。

## 仓库结构

每个子项目各自独立构建，**根目录没有统一的构建脚本**。

| 路径 | 内容 | 状态 |
|---|---|---|
| `huaxin-miniprogram/` | **小程序**（消费端：产品目录、对比、询价），7 个页面 + 自定义 tabBar，已实现，但产品数据目前是静态 JS | 活跃 — 详见 `huaxin-miniprogram/CLAUDE.md` |
| `client/` | **Vue 3 + Vite** 项目，按需求要装 Element Plus，承担管理/展示前端 | 仅 Vite 脚手架——只有 `App.vue` + `HelloWorld.vue`，未装 Element Plus |
| `server/server/` | **Spring Boot 4.0.6** + MyBatis 4.0.1 + Spring Security 后端（Java 17，Maven） | 仅脚手架——只有 `ServerApplication.java`，`application.properties` 为空，无 Controller / Mapper |
| `产品/` | 按 14 个产品类别组织的原始产品图片与规格 PDF；含真正的需求文档 `华信通讯科技有限公司_微信小程序需求文档.md` | 资源目录——详见 `产品/CLAUDE.md`。在 `huaxin-miniprogram/产品/` 有一份镜像 |

## 各子项目的命令

### `huaxin-miniprogram/`（正式小程序）
没有构建命令。使用 **微信开发者工具** 打开，AppID 为 `wxab8299303b8f0174`。IDE 自动处理编译。未配置测试框架。完整架构说明位于 `huaxin-miniprogram/CLAUDE.md`，**在此目录下工作前先阅读该文件**。

### `client/`（Vue 3 + Vite，目标技术栈含 Element Plus）
```
cd client
npm install
npm run dev      # 开发服务器
npm run build    # 生产构建
npm run preview  # 预览构建结果
```
需求要求使用 Element Plus，但 `package.json` 当前未包含。需要时执行 `npm install element-plus` 并按官方文档在 `src/main.js` 里 `app.use(ElementPlus)`。

### `server/server/`（Spring Boot 4）
```
cd server\server
.\mvnw spring-boot:run                        # 运行应用
.\mvnw test                                   # 运行全部测试
.\mvnw test -Dtest=ClassName#methodName       # 运行单个测试
.\mvnw clean package                          # 打包 jar
```
需要 Java 17。Maven Wrapper 已随仓库提交（Windows 下使用 `mvnw.cmd`）。Spring Boot 默认端口 8080；目前还没有任何 Controller / Mapper / 配置，应用能启动但不对外提供任何接口。

## 约定

- **语言：** UI 文案、代码注释、产品/类别名称以及大部分目录名都是简体中文。请保留路径中的中文标识符（如 `产品/电表箱/`），不要拼音化或英化。
- **微信小程序样式：** 尺寸使用 `rpx`（不是 `px`）。每个页面/组件由 4 个同名文件组成（`.js`/`.json`/`.wxml`/`.wxss`）。
- **数据流现状 vs 目标：** 当前 `huaxin-miniprogram/` 里的产品数据是静态 JS（`data/*.js`），后端、Vue 前端都还是空架子，三端尚未真正联通。引入 Spring Boot 接口时，记得也要把小程序的数据源从静态文件切换到 API。
- **真正的需求文档** 是 `产品/华信通讯科技有限公司_微信小程序需求文档.md`；根目录的 `项目需求.txt` 给出的是高层架构指令（三端 + 各自目录约定），两份文档要结合看。
