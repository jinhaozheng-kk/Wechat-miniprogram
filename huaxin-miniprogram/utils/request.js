// 网络请求工具：把 wx.request 包成 Promise，并统一拆解后端返回的 { code, message, data } 包装
const { baseURL } = require('../config');

/**
 * 通用请求方法
 * @param {Object} opts
 * @param {string} opts.url     接口路径（不含 baseURL，如 /api/categories）
 * @param {string} [opts.method] HTTP 方法，默认 GET
 * @param {Object} [opts.data]   请求体或 query 参数
 * @param {boolean} [opts.silent] 是否静默：true 时不弹错误 toast
 * @returns Promise<data>        成功时直接 resolve 业务数据 (data)
 */
function request({ url, method = 'GET', data, silent = false } = {}) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseURL + url,
      method,
      data,
      header: { 'Content-Type': 'application/json' },
      success(res) {
        // HTTP 层错误
        if (res.statusCode < 200 || res.statusCode >= 300) {
          const msg = `服务器错误 ${res.statusCode}`;
          if (!silent) wx.showToast({ title: msg, icon: 'none' });
          reject(new Error(msg));
          return;
        }
        // 业务层错误（按统一返回结构 { code, message, data }）
        const body = res.data || {};
        if (body.code === 0) {
          resolve(body.data);
        } else {
          const msg = body.message || '请求失败';
          if (!silent) wx.showToast({ title: msg, icon: 'none' });
          reject(new Error(msg));
        }
      },
      fail(err) {
        const msg = '网络错误，请检查后端是否启动';
        if (!silent) wx.showToast({ title: msg, icon: 'none' });
        reject(err);
      },
    });
  });
}

/** GET 简化方法：第二个参数为 query 对象 */
function get(url, params, opts = {}) {
  // 把 params 拼到 query string，避免 wx.request 把 GET 的 data 当 body 处理时的兼容问题
  let finalUrl = url;
  if (params && typeof params === 'object') {
    const qs = Object.entries(params)
      .filter(([, v]) => v !== undefined && v !== null && v !== '')
      .map(([k, v]) => `${encodeURIComponent(k)}=${encodeURIComponent(v)}`)
      .join('&');
    if (qs) finalUrl += (url.includes('?') ? '&' : '?') + qs;
  }
  return request({ url: finalUrl, method: 'GET', ...opts });
}

/** POST 简化方法 */
function post(url, data, opts = {}) {
  return request({ url, method: 'POST', data, ...opts });
}

/** PUT 简化方法 */
function put(url, data, opts = {}) {
  return request({ url, method: 'PUT', data, ...opts });
}

module.exports = { request, get, post, put };
