import { getToken } from './auth'

const BASE_URL = import.meta.env.VITE_API_BASE_URL || '/dev-api'

async function parseResponse(response) {
  const data = await response.json().catch(() => ({}))
  if (data && data.code !== undefined && data.code !== 200) {
    const error = new Error(data.msg || '请求失败')
    error.data = data
    throw error
  }
  return data
}

export async function request(url, options = {}) {
  const headers = { ...(options.headers || {}) }
  const token = getToken()
  if (token) {
    headers.Authorization = `Bearer ${token}`
  }
  if (options.json !== false) {
    headers['Content-Type'] = 'application/json'
  }
  const res = await fetch(`${BASE_URL}${url}`, {
    ...options,
    headers
  })
  if (!res.ok) {
    const error = new Error(`HTTP ${res.status}`)
    throw error
  }
  return parseResponse(res)
}

export function getBaseUrl() {
  return BASE_URL
}
