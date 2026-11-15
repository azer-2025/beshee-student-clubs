import { request } from '../utils/request'

export function listActivities(params = {}) {
  const clean = Object.fromEntries(Object.entries(params).filter(([, value]) => value !== undefined && value !== '' && value !== null))
  const query = new URLSearchParams(clean).toString()
  return request(`/activity/info/public/list?${query}`, { method: 'get' })
}

export function getActivityDetail(id) {
  return request(`/activity/info/public/detail/${id}`, { method: 'get' })
}

export function signupActivity(data) {
  return request('/activity/signup', { method: 'post', body: JSON.stringify(data) })
}

export function mySignups(params = {}) {
  const clean = Object.fromEntries(Object.entries(params).filter(([, value]) => value !== undefined && value !== '' && value !== null))
  const query = new URLSearchParams(clean).toString()
  return request(`/activity/signup/my?${query}`, { method: 'get' })
}
