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
  goToInquiry() {
    if (!this.data.product) return;
    wx.navigateTo({
      url: `/pages/inquiry/inquiry?productId=${this.data.product.id}`,
    });
  },

  // 分享
  onShareAppMessage() {
    return {
      title: this.data.product.name,
      path: `/pages/detail/detail?id=${this.data.product.id}`,
    };
  },
});
