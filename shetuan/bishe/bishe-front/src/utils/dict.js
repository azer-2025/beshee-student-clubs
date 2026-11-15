import { request } from './request'

const cache = new Map()

export async function getDict(dictType) {
  if (cache.has(dictType)) {
    return cache.get(dictType)
  }
  const res = await request(`/system/dict/data/type/${dictType}`, { method: 'get' })
  const list = res.data || []
  cache.set(dictType, list)
  return list
}

export function dictLabel(options, value) {
  const match = options.find(item => item.dictValue === value || item.value === value)
  return match ? (match.dictLabel || match.label) : value
}
