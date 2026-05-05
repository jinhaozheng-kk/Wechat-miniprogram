const app = getApp();
const api = require('../../utils/api');

Page({
  data: {
    product: null,
    category: null,
    company: app.globalData.company,
    loading: true,
  },

  onLoad(options) {
    this.loadData(options.id);
  },

  async loadData(id) {
    this.setData({ loading: true });
    try {
      const product = await api.getProduct(id);
      // 取所属分类，便于无图占位与配色
      const category = product.categoryId
        ? await api.getCategory(product.categoryId).catch(() => null)
        : null;
      wx.setNavigationBarTitle({ title: product.name.slice(0, 10) });
      this.setData({ product, category });
    } catch (e) {
      // 后端找不到产品时停留半秒后退回上一页
      setTimeout(() => wx.navigateBack(), 600);
    } finally {
      this.setData({ loading: false });
    }
  },

  // 立即询价：跳到询价页并预填本产品
  // 询价页是 tabBar 页面，wx.navigateTo 不能跳，必须用 switchTab；
  // 但 switchTab 不支持 query，所以产品 id 用 globalData 中转，由询价页 onShow 取走
  goToInquiry() {
    if (!this.data.product) return;
    app.globalData.pendingInquiryProductIds = [this.data.product.id];
    wx.switchTab({ url: '/pages/inquiry/inquiry' });
  },

  // 分享
  onShareAppMessage() {
    return {
      title: this.data.product.name,
      path: `/pages/detail/detail?id=${this.data.product.id}`,
    };
  },
});
