const api = require('../../utils/api');
const share = require('../../utils/share');

Page({
  data: {
    category: null,
    products: [],
    loading: true,
  },

  onLoad(options) {
    share.enableShareMenu();
    this.loadData(options.id);
  },

  async loadData(id) {
    this.setData({ loading: true });
    try {
      // 并发取分类详情与产品列表
      const [category, products] = await Promise.all([
        api.getCategory(id),
        api.listProducts({ categoryId: id }),
      ]);
      wx.setNavigationBarTitle({ title: category ? category.name : '产品列表' });
      this.setData({ category, products });
    } catch (e) {
      this.setData({ category: null, products: [] });
    } finally {
      this.setData({ loading: false });
    }
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/detail/detail?id=${id}` });
  },

  onShareAppMessage() {
    return share.buildCategoryShare(this.data.category);
  },

  onShareTimeline() {
    return share.buildCategoryTimeline(this.data.category);
  },
});
