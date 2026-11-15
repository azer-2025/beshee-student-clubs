import { request } from '../utils/request'

export function listClubs(params = {}) {
  const clean = Object.fromEntries(Object.entries(params).filter(([, value]) => value !== undefined && value !== '' && value !== null))
  const query = new URLSearchParams(clean).toString()
  return request(`/club/info/public/list?${query}`, { method: 'get' })
}

export function getClubDetail(id) {
  return request(`/club/info/public/detail/${id}`, { method: 'get' })
}

export function applyClub(data) {
  return request('/club/info/apply', { method: 'post', body: JSON.stringify(data) })
}

export function applyJoinClub(data) {
  return request('/club/member/apply', { method: 'post', body: JSON.stringify(data) })
}

export function myClubApplications(params = {}) {
  const clean = Object.fromEntries(Object.entries(params).filter(([, value]) => value !== undefined && value !== '' && value !== null))
  const query = new URLSearchParams(clean).toString()
  return request(`/club/member/my?${query}`, { method: 'get' })
}

export function myClubApplyList(params = {}) {
  const clean = Object.fromEntries(Object.entries(params).filter(([, value]) => value !== undefined && value !== '' && value !== null))
  const query = new URLSearchParams(clean).toString()
  return request(`/club/info/my/apply?${query}`, { method: 'get' })
}

export function listCategories() {
  return request('/club/category/public/list', { method: 'get' })
}
