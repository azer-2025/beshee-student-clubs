import request from '@/utils/request'

export function getStatsOverview() {
  return request({
    url: '/stats/overview',
    method: 'get'
  })
}

export function getStatsChart() {
  return request({
    url: '/stats/chart',
    method: 'get'
  })
}
