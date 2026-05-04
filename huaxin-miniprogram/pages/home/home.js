const app = getApp();
const api = require('../../utils/api');

Page({
  data: {
    company: app.globalData.company,
    // 分类列表，启动时从后端拉取
    categories: [],
    loading: true,
    banners: [
      {
        tag: '华信通讯',
        title: '专注建筑\n电气箱体',
        subtitle: '14大产品系列  品质保证',
        bg: 'linear-gradient(135deg, #1565C0 0%, #1E88E5 100%)',
        image: '/images/xiao-fang/1.png',
      },
      {
        tag: '热销系列',
        title: '户外不锈钢\n防水箱',
        subtitle: '201/304不锈钢  IP65防水',
        bg: 'linear-gradient(135deg, #263238 0%, #455A64 100%)',
        image: '/images/hu-wai-bx/1.png',
      },
      {
        tag: '精品推荐',
        title: '等电位\n联结端子箱',
        subtitle: 'TD-28系列  建筑防雷专用',
        bg: 'linear-gradient(135deg, #BF360C 0%, #E64A19 100%)',
        image: '/images/deng-dian-wei/1.png',
      },
    ],
    currentBanner: 0,
  },

  onLoad() {
    this.loadCategories();
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ active: 0 });
    }
  },

  // 拉取分类
  async loadCategories() {
    this.setData({ loading: true });
    try {
      const categories = await api.listCategories();
      this.setData({ categories });
    } catch (e) {
      // 错误 toast 已在 request.js 统一弹过
    } finally {
      this.setData({ loading: false });
    }
  },

  // 轮播切换
  onBannerChange(e) {
    this.setData({ currentBanner: e.detail.current });
  },

  // 拨打电话
  callPhone() {
    const phone = this.data.company.phone;
    if (phone.startsWith('请填写')) {
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

  // 前往询价页
  goToInquiry() {
    wx.switchTab({ url: '/pages/inquiry/inquiry' });
  },
});
