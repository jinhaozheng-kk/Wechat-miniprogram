// 产品相关 API
import http from './http'

/** 产品列表（支持按分类与关键字筛选） */
export const listProducts = (params) => http.get('/api/products', { params })

/** 产品详情 */
export const getProduct = (id) => http.get(`/api/products/${id}`)
