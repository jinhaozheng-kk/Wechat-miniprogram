const api = require('../../utils/api');

Page({
  data: {
    category: null,
    products: [],
    loading: true,
  },

  onLoad(options) {
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
});
