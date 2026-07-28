const assert = require('node:assert/strict');

const share = require('../huaxin-miniprogram/utils/share');

assert.deepEqual(share.buildHomeShare(), {
  title: '华信通讯科技有限公司 - 配电箱产品展示',
  path: '/pages/home/home',
  imageUrl: '/images/xiao-fang/1.jpg',
});

assert.deepEqual(share.buildProductsShare(), {
  title: '华信通讯产品中心 - 配电箱/信息箱/端子箱',
  path: '/pages/products/products',
  imageUrl: '/images/xiao-fang/1.jpg',
});

assert.deepEqual(
  share.buildCategoryShare({
    id: 'hu-wai-bx',
    name: '户外不锈钢箱',
    image: '/images/hu-wai-bx/1.jpg',
  }),
  {
    title: '户外不锈钢箱 - 华信通讯科技有限公司',
    path: '/pages/category/category?id=hu-wai-bx',
    imageUrl: '/images/hu-wai-bx/1.jpg',
  }
);

assert.deepEqual(
  share.buildProductShare({
    id: 'bxpd-001',
    name: '不锈钢户外配电箱',
    images: ['/images/hu-wai-bx/1.jpg'],
  }),
  {
    title: '不锈钢户外配电箱 - 华信通讯',
    path: '/pages/detail/detail?id=bxpd-001',
    imageUrl: '/images/hu-wai-bx/1.jpg',
  }
);

assert.deepEqual(
  share.buildProductTimeline({
    id: 'bxpd-001',
    name: '不锈钢户外配电箱',
    images: ['/images/hu-wai-bx/1.jpg'],
  }),
  {
    title: '不锈钢户外配电箱 - 华信通讯',
    query: 'id=bxpd-001',
    imageUrl: '/images/hu-wai-bx/1.jpg',
  }
);

console.log('share tests passed');
