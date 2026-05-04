// 询价单相关 API
import http from './http'

/** 询价单列表 */
export const listInquiries = (params) => http.get('/api/admin/inquiries', { params })

/** 更新询价单状态 */
export const updateInquiryStatus = (id, status) =>
  http.put(`/api/admin/inquiries/${id}/status`, { status })
