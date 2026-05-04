// 全局配置
// 开发期：本机后端 8080；生产期改为 https://api.huaxin.example.com 之类
// 注意：开发期 baseURL 是 http://，需要在微信开发者工具
//       「设置 → 项目设置 → 不校验合法域名、HTTPS 证书...」中勾选放行
module.exports = {
  baseURL: 'http://localhost:8080',
};
