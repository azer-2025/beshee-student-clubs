import { request } from '../utils/request'

export function getCaptcha() {
  return request('/captchaImage', { method: 'get', json: false })
}

export function login(payload) {
  return request('/login', { method: 'post', body: JSON.stringify(payload) })
}

export function register(payload) {
  return request('/register', { method: 'post', body: JSON.stringify(payload) })
}

export function getInfo() {
  return request('/getInfo', { method: 'get' })
}

export function logout() {
  return request('/logout', { method: 'post' })
}
