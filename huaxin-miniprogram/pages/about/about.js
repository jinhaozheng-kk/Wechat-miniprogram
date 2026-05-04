const app = getApp();

Page({
  data: {
    company: app.globalData.company,
    advantages: [
      { icon: '🏭', title: '自主生产', desc: '自有工厂，品质可控，交期稳定' },
      { icon: '✅', title: '资质齐全', desc: '营业执照、产品认证一应俱全' },
      { icon: '📦', title: '品类丰富', desc: '14大系列，涵盖主流建筑电气箱体' },
      { icon: '🚚', title: '快速发货', desc: '现货充足，支持批量供货' },
    ],
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ active: 3 });
    }
  },

  callPhone() {
    const phone = this.data.company.phone;
    if (phone.startsWith('请填写')) {
      wx.showToast({ title: '请先在 data/company.js 中填写联系电话', icon: 'none', duration: 3000 });
      return;
    }
    wx.makePhoneCall({ phoneNumber: phone });
  },

  copyAddress() {
    wx.setClipboardData({
      data: this.data.company.address,
      success: () => wx.showToast({ title: '地址已复制', icon: 'success' }),
    });
  },
});
