const api = require('../../utils/api');
const share = require('../../utils/share');

Page({
  data: {
    categories: [],
    searchQuery: '',
    searchResults: [],
    isSearching: false,
    loading: false,
  },

  // 节流定时器 id（放在 this 上而不是 data 里，避免无谓的视图更新）
  _searchTimer: null,

  onLoad() {
    share.enableShareMenu();
    this.loadCategories();
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ active: 1 });
    }
  },

  async loadCategories() {
    try {
      const categories = await api.listCategories();
      this.setData({ categories });
    } catch (e) {}
  },

  // 输入框节流：用户停 300ms 后再发请求，避免每个字符都打一次后端
  onSearchInput(e) {
    const q = e.detail.value;
    this.setData({ searchQuery: q });
    if (this._searchTimer) clearTimeout(this._searchTimer);
    if (!q.trim()) {
      this.setData({ searchResults: [], isSearching: false });
      this._searchTimer = null;
      return;
    }
    this._searchTimer = setTimeout(() => this._doSearch(q), 300);
  },

  async _doSearch(keyword) {
    this.setData({ loading: true, isSearching: true });
    try {
      const results = await api.listProducts({ keyword });
      this.setData({ searchResults: results });
    } catch (e) {
      this.setData({ searchResults: [] });
    } finally {
      this.setData({ loading: false });
    }
  },

  clearSearch() {
    if (this._searchTimer) clearTimeout(this._searchTimer);
    this._searchTimer = null;
    this.setData({ searchQuery: '', searchResults: [], isSearching: false });
  },

  goToCategory(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/category/category?id=${id}` });
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/detail/detail?id=${id}` });
  },

  // 非标定制 → 询价页（定制模式，经 globalData 中转标记）
  goToInquiry() {
    getApp().globalData.inquiryCustomMode = true;
    wx.switchTab({ url: '/pages/inquiry/inquiry' });
  },

  onShareAppMessage() {
    return share.buildProductsShare();
  },

  onShareTimeline() {
    return share.buildProductsTimeline();
  },
});
