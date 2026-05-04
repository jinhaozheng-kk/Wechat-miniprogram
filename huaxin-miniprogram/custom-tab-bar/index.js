Component({
  data: {
    active: 0,
    tabs: [
      { pagePath: '/pages/home/home',        text: '首页', icon: '⌂' },
      { pagePath: '/pages/products/products', text: '产品', icon: '▦' },
      { pagePath: '/pages/inquiry/inquiry',   text: '询价', icon: '☎' },
      { pagePath: '/pages/about/about',       text: '关于', icon: 'ℹ' },
    ],
  },
  methods: {
    switchTab(e) {
      const { index, path } = e.currentTarget.dataset;
      this.setData({ active: index });
      wx.switchTab({ url: path });
    },
  },
});
