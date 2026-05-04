const app = getApp();
const api = require('../../utils/api');

// 中国大陆手机号校验
const PHONE_REGEX = /^1[3-9]\d{9}$/;

Page({
  data: {
    company: app.globalData.company,
    // 询价产品列表 [{ id, name, model }]
    products: [],
    // 表单字段
    form: {
      contactName: '',
      contactPhone: '',
      projectName: '',
      city: '',
      requirement: '',
      remark: '',
    },
    submitting: false,
    // 提交成功视图开关
    submitted: false,
  },

  /**
   * 进入页面的来源：
   * 1. 详情页：?productId=xxx           → 预填 1 项
   * 2. 其他渠道：?productIds=a,b,c      → 预填多项
   * 3. tab 直接进入：产品区为空，引导用户去浏览产品
   */
  async onLoad(options) {
    let ids = [];
    if (options.productId) {
      ids = [options.productId];
    } else if (options.productIds) {
      ids = options.productIds.split(',').filter(Boolean);
    }
    if (ids.length > 0) {
      await this._fillProductsByIds(ids);
    }
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ active: 2 });
    }
  },

  // 按 id 列表批量取产品（小程序端循环单查；后端目前没有批量查询接口）
  async _fillProductsByIds(ids) {
    try {
      const list = await Promise.all(
        ids.map(id => api.getProduct(id).catch(() => null))
      );
      const products = list
        .filter(p => p)
        .map(p => ({ id: p.id, name: p.name, model: p.model }));
      this.setData({ products });
    } catch (e) {}
  },

  // 移除一项产品
  removeProduct(e) {
    const id = e.currentTarget.dataset.id;
    this.setData({ products: this.data.products.filter(p => p.id !== id) });
  },

  // 表单输入双向绑定
  onFieldInput(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({ [`form.${field}`]: e.detail.value });
  },

  goToProducts() {
    wx.switchTab({ url: '/pages/products/products' });
  },

  // 拨打电话（保留兜底入口，方便老客户直接打）
  callPhone() {
    const phone = this.data.company.phone;
    if (phone && !phone.startsWith('请填写')) {
      wx.makePhoneCall({ phoneNumber: phone });
    }
  },

  // 提交询价
  async onSubmit() {
    const { products, form } = this.data;
    // 校验
    if (products.length === 0) {
      wx.showToast({ title: '请先选择询价产品', icon: 'none' });
      return;
    }
    if (!form.contactName.trim()) {
      wx.showToast({ title: '请填写联系人姓名', icon: 'none' });
      return;
    }
    if (!PHONE_REGEX.test(form.contactPhone)) {
      wx.showToast({ title: '联系电话格式不正确', icon: 'none' });
      return;
    }
    if (!form.requirement.trim()) {
      wx.showToast({ title: '请填写需求描述', icon: 'none' });
      return;
    }
    if (form.remark.length > 200) {
      wx.showToast({ title: '备注最多 200 字', icon: 'none' });
      return;
    }

    this.setData({ submitting: true });
    try {
      await api.submitInquiry({
        productIds: products.map(p => p.id),
        contactName: form.contactName.trim(),
        contactPhone: form.contactPhone.trim(),
        projectName: form.projectName.trim() || null,
        city: form.city.trim() || null,
        requirement: form.requirement.trim(),
        remark: form.remark.trim() || null,
      });
      // 提交成功：切换到成功视图，并清空 compare（业务上认为已结清）
      this.setData({ submitted: true });
    } catch (e) {
      // 错误 toast 已在 request.js 弹过
    } finally {
      this.setData({ submitting: false });
    }
  },

  // 返回首页
  backHome() {
    wx.switchTab({ url: '/pages/home/home' });
  },

  // 再提交一单
  submitAnother() {
    this.setData({
      submitted: false,
      products: [],
      form: {
        contactName: '',
        contactPhone: '',
        projectName: '',
        city: '',
        requirement: '',
        remark: '',
      },
    });
  },
});
