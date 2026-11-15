import { getBaseUrl } from './request'

export function resolveAsset(url) {
  if (!url) return ''
  if (url.startsWith('http://') || url.startsWith('https://')) return url
  if (url.startsWith('data:')) return url
  const base = getBaseUrl()
  return `${base}${url}`
}
