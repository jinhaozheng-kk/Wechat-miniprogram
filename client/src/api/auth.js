// 登录相关 API
import http from './http'

/** 后台登录 */
export const login = (data) => http.post('/api/admin/login', data)
