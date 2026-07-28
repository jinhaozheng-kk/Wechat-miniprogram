const DEFAULT_IMAGE = '/images/xiao-fang/1.jpg';
const COMPANY_NAME = '华信通讯科技有限公司';
const SHORT_NAME = '华信通讯';

function getFirstImage(source) {
  if (!source) return DEFAULT_IMAGE;
  if (source.image) return source.image;
  if (Array.isArray(source.images) && source.images.length > 0) {
    return source.images[0];
  }
  return DEFAULT_IMAGE;
}

function appendId(path, id) {
  return id ? `${path}?id=${encodeURIComponent(id)}` : path;
}

function getQuery(path) {
  const index = path.indexOf('?');
  return index >= 0 ? path.slice(index + 1) : '';
}

function toTimeline(appMessage) {
  return {
    title: appMessage.title,
    query: getQuery(appMessage.path),
    imageUrl: appMessage.imageUrl,
  };
}

function enableShareMenu() {
  if (typeof wx === 'undefined' || !wx.showShareMenu) return;
  wx.showShareMenu({
    withShareTicket: true,
    menus: ['shareAppMessage', 'shareTimeline'],
  });
}

function buildHomeShare() {
  return {
    title: `${COMPANY_NAME} - 配电箱产品展示`,
    path: '/pages/home/home',
    imageUrl: DEFAULT_IMAGE,
  };
}

function buildHomeTimeline() {
  return toTimeline(buildHomeShare());
}

function buildProductsShare() {
  return {
    title: `${SHORT_NAME}产品中心 - 配电箱/信息箱/端子箱`,
    path: '/pages/products/products',
    imageUrl: DEFAULT_IMAGE,
  };
}

function buildProductsTimeline() {
  return toTimeline(buildProductsShare());
}

function buildCategoryShare(category) {
  if (!category) return buildProductsShare();
  return {
    title: `${category.name} - ${COMPANY_NAME}`,
    path: appendId('/pages/category/category', category.id),
    imageUrl: getFirstImage(category),
  };
}

function buildCategoryTimeline(category) {
  return toTimeline(buildCategoryShare(category));
}

function buildProductShare(product) {
  if (!product) return buildHomeShare();
  return {
    title: `${product.name} - ${SHORT_NAME}`,
    path: appendId('/pages/detail/detail', product.id),
    imageUrl: getFirstImage(product),
  };
}

function buildProductTimeline(product) {
  return toTimeline(buildProductShare(product));
}

module.exports = {
  enableShareMenu,
  buildHomeShare,
  buildHomeTimeline,
  buildProductsShare,
  buildProductsTimeline,
  buildCategoryShare,
  buildCategoryTimeline,
  buildProductShare,
  buildProductTimeline,
};
