// 统计相关 API
import http from './http'

/** 后台首页 dashboard 数据 */
export const getStats = () => http.get('/api/admin/stats')
