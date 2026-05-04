// 业务接口封装
// 各页面通过 require('../../utils/api') 调用业务方法，不直接接触 wx.request
const { get, post } = require('./request');

/** 查询所有分类 */
function listCategories() {
  return get('/api/categories');
}

/** 查询单个分类 */
function getCategory(id) {
  return get(`/api/categories/${id}`);
}

/**
 * 产品列表
 * @param {Object} [params]
 * @param {string} [params.categoryId] 按分类筛选
 * @param {string} [params.keyword]    关键字（匹配名称/型号）
 */
function listProducts(params) {
  return get('/api/products', params);
}

/** 查询单个产品 */
function getProduct(id) {
  return get(`/api/products/${id}`);
}

/**
 * 提交询价单
 * @param {Object} data
 * @param {string[]} data.productIds   询价产品 id 列表（至少 1 项）
 * @param {string}   data.contactName  联系人
 * @param {string}   data.contactPhone 联系电话
 * @param {string}   [data.projectName] 工程名称
 * @param {string}   [data.city]        所在城市
 * @param {string}   data.requirement   需求描述
 * @param {string}   [data.remark]      备注
 */
function submitInquiry(data) {
  return post('/api/inquiry', data);
}

module.exports = {
  listCategories,
  getCategory,
  listProducts,
  getProduct,
  submitInquiry,
};
