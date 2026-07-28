// 全局配置
// apiMode:
// - local: 本地开发，走 wx.request + http://localhost:8080
// - cloudrun: 体验版/正式版，走 wx.cloud.callContainer 调微信云托管
module.exports = {
  apiMode: 'cloudrun',
  baseURL: 'http://localhost:8080',
  cloud: {
    enabled: true,
    env: 'prod-d9gq3q3ib3c798208',
    service: 'springboot-44kf',
  },
};
