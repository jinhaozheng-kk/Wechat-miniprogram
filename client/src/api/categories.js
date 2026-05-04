// 分类相关 API
import http from './http'

/** 查询所有分类 */
export const listCategories = () => http.get('/api/categories')
