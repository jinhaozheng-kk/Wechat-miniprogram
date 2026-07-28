const company = require('./data/company');
const { cloud } = require('./config');

App({
  globalData: {
    // 公司信息：暂以静态数据形式提供（后端尚未接入 company 接口）
    company,
  },

  onLaunch() {
    if (cloud.enabled && wx.cloud) {
      wx.cloud.init({
        env: cloud.env,
        traceUser: true,
      });
    }
  },
});
