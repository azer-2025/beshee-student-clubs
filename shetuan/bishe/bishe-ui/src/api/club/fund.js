import request from '@/utils/request'

// 查询经费申请列表
export function listClubFund(query) {
  return request({
    url: '/club/fund/list',
    method: 'get',
    params: query
  })
}

// 查询经费申请详情
export function getClubFund(fundId) {
  return request({
    url: '/club/fund/' + fundId,
    method: 'get'
  })
}

// 新增经费申请
export function addClubFund(data) {
  return request({
    url: '/club/fund',
    method: 'post',
    data: data
  })
}

// 修改经费申请
export function updateClubFund(data) {
  return request({
    url: '/club/fund',
    method: 'put',
    data: data
  })
}

// 删除经费申请
export function delClubFund(fundId) {
  return request({
    url: '/club/fund/' + fundId,
    method: 'delete'
  })
}

// 审核经费申请
export function approveClubFund(data) {
  return request({
    url: '/club/fund/approve',
    method: 'put',
    data: data
  })
}
