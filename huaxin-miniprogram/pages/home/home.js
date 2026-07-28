const app = getApp();
const api = require('../../utils/api');
const share = require('../../utils/share');

Page({
  data: {
    company: app.globalData.company,
    statusBarHeight: 44, // px，onLoad 时按真机校准

    // 分类（启动时从后端拉取）
    categories: [],
    totalCategories: 0,
    // Bento 网格切片
    bentoHero: null,
    bentoHalves: [],
    bentoSmalls: [],
    loading: true,

    // 精选卡片（App Store Today 风格）
    featured: [
      {
        tag: '华信通讯',
        title: '建筑电气\n从箱体开始',
        subtitle: '14大产品系列 · 工厂直供',
        bg: 'linear-gradient(160deg, #1A1A1E 0%, #000000 100%)',
        image: '/images/xiao-fang/1.jpg',
      },
      {
        tag: '热销系列',
        title: '户外不锈钢\n防水箱',
        subtitle: '201/304不锈钢 · IP65防水',
        bg: 'linear-gradient(160deg, #3D4A54 0%, #1E262C 100%)',
        image: '/images/hu-wai-bx/1.jpg',
      },
      {
        tag: '精品推荐',
        title: '等电位\n联结端子箱',
        subtitle: 'TD-28系列 · 建筑防雷专用',
        bg: 'linear-gradient(160deg, #16518F 0%, #082843 100%)',
        image: '/images/deng-dian-wei/1.jpg',
      },
      {
        tag: '工厂能力',
        title: '非标箱体\n按需定制',
        subtitle: '来图来样 · 尺寸材质工艺可定',
        bg: 'linear-gradient(160deg, #2E2A24 0%, #15120D 100%)',
        image: '/images/ji-ye/1.jpg',
        custom: true,
      },
    ],
    currentFeatured: 0,
  },

  onLoad() {
    share.enableShareMenu();

    // 真机状态栏高度（custom navigationStyle 需要自己留白）
    try {
      const info = wx.getWindowInfo ? wx.getWindowInfo() : wx.getSystemInfoSync();
      this.setData({ statusBarHeight: info.statusBarHeight || 44 });
    } catch (e) { /* 取不到就用默认 44px */ }

    this.loadCategories();
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ active: 0 });
    }
  },

  // 拉取分类，并切出 Bento 网格用的三段
  async loadCategories() {
    this.setData({ loading: true });
    try {
      const categories = await api.listCategories();
      this.setData({
        categories,
        totalCategories: categories.length,
        bentoHero: categories[0] || null,
        bentoHalves: categories.slice(1, 3),
        bentoSmalls: categories.slice(3, 6),
      });
    } catch (e) {
      // 错误 toast 已在 request.js 统一弹过
    } finally {
      this.setData({ loading: false });
    }
  },

  // 精选卡片切换
  onFeaturedChange(e) {
    this.setData({ currentFeatured: e.detail.current });
  },

  // 点击精选卡：定制卡 -> 询价页定制模式；其他 -> 产品页
  onFeaturedTap(e) {
    const item = this.data.featured[e.currentTarget.dataset.index];
    if (item && item.custom) {
      app.globalData.inquiryCustomMode = true;
      wx.switchTab({ url: '/pages/inquiry/inquiry' });
    } else {
      wx.switchTab({ url: '/pages/products/products' });
    }
  },

  // 拨打电话
  callPhone() {
    const phone = this.data.company.phone;
    if (phone.startsWith('请填')) {
      wx.showToast({ title: '请先在 data/company.js 中填写联系电话', icon: 'none', duration: 3000 });
      return;
    }
    wx.makePhoneCall({ phoneNumber: phone });
  },

  // 进入分类
  goToCategory(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/category/category?id=${id}` });
  },

  // 查看全部产品
  goToProducts() {
    wx.switchTab({ url: '/pages/products/products' });
  },

  // 前往询价页
  goToInquiry() {
    wx.switchTab({ url: '/pages/inquiry/inquiry' });
  },

  onShareAppMessage() {
    return share.buildHomeShare();
  },

  onShareTimeline() {
    return share.buildHomeTimeline();
  },
});
