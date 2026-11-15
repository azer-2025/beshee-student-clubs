import { request } from '../utils/request'

export function listCarousels() {
  return request('/content/carousel/public/list', { method: 'get' })
}

export function listNotices(params = {}) {
  const clean = Object.fromEntries(Object.entries(params).filter(([, value]) => value !== undefined && value !== '' && value !== null))
  const query = new URLSearchParams(clean).toString()
  return request(`/system/notice/public/list?${query}`, { method: 'get' })
}
