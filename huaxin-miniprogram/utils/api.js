// 业务接口封装
// 各页面通过 require('../../utils/api') 调用业务方法，不直接接触 wx.request
const { get, post } = require('./request');
const localCategories = require('../data/categories');
const localProducts = require('../data/products');

const imagePathMap = {
  '/images/guang-xian/1.png': '/images/guang-xian/1.jpg',
  '/images/xiao-fang/1.png': '/images/xiao-fang/1.jpg',
  '/images/xiao-fang/2.png': '/images/xiao-fang/2.jpg',
  '/images/xiao-fang/3.png': '/images/xiao-fang/3.jpg',
  '/images/duo-mei-ti/1.png': '/images/duo-mei-ti/1.jpg',
  '/images/deng-dian-wei/1.png': '/images/deng-dian-wei/1.jpg',
  '/images/pz30/1.png': '/images/pz30/1.jpg',
  '/images/hu-wai-bx/1.png': '/images/hu-wai-bx/1.jpg',
  '/images/hu-wai-bx/2.png': '/images/hu-wai-bx/2.jpg',
};

function normalizeImagePath(path) {
  return imagePathMap[path] || path;
}

function normalizeCategory(category) {
  if (!category) return category;
  return {
    ...category,
    image: normalizeImagePath(category.image),
  };
}

function normalizeProduct(product) {
  if (!product) return product;
  return {
    ...product,
    images: Array.isArray(product.images)
      ? product.images.map(normalizeImagePath)
      : product.images,
  };
}

function listLocalProducts(params = {}) {
  let products = localProducts.products;
  if (params.keyword) {
    products = localProducts.search(params.keyword);
  }
  if (params.categoryId) {
    products = products.filter(product => product.categoryId === params.categoryId);
  }
  return products.map(normalizeProduct);
}

/** 查询所有分类 */
async function listCategories() {
  try {
    const categories = await get('/api/categories', undefined, { silent: true });
    return Array.isArray(categories) ? categories.map(normalizeCategory) : categories;
  } catch (e) {
    return localCategories.map(normalizeCategory);
  }
}

/** 查询单个分类 */
async function getCategory(id) {
  try {
    return normalizeCategory(await get(`/api/categories/${id}`, undefined, { silent: true }));
  } catch (e) {
    return normalizeCategory(localCategories.find(category => category.id === id) || null);
  }
}

/**
 * 产品列表
 * @param {Object} [params]
 * @param {string} [params.categoryId] 按分类筛选
 * @param {string} [params.keyword]    关键字（匹配名称/型号）
 */
async function listProducts(params) {
  try {
    const products = await get('/api/products', params, { silent: true });
    return Array.isArray(products) ? products.map(normalizeProduct) : products;
  } catch (e) {
    return listLocalProducts(params);
  }
}

/** 查询单个产品 */
async function getProduct(id) {
  try {
    return normalizeProduct(await get(`/api/products/${id}`, undefined, { silent: true }));
  } catch (e) {
    const product = localProducts.getById(id);
    if (!product) throw e;
    return normalizeProduct(product);
  }
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
