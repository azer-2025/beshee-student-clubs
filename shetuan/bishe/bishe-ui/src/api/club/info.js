import request from '@/utils/request'

// 查询社团列表
export function listClubInfo(query) {
  return request({
    url: '/club/info/list',
    method: 'get',
    params: query
  })
}

// 查询社团申请列表
export function listClubApply(query) {
  return request({
    url: '/club/info/apply/list',
    method: 'get',
    params: query
  })
}

// 查询社团详情
export function getClubInfo(clubId) {
  return request({
    url: '/club/info/' + clubId,
    method: 'get'
  })
}

// 新增社团
export function addClubInfo(data) {
  return request({
    url: '/club/info',
    method: 'post',
    data: data
  })
}

// 修改社团
export function updateClubInfo(data) {
  return request({
    url: '/club/info',
    method: 'put',
    data: data
  })
}

// 删除社团
export function delClubInfo(clubId) {
  return request({
    url: '/club/info/' + clubId,
    method: 'delete'
  })
}

// 审核社团申请
export function approveClub(data) {
  return request({
    url: '/club/info/approve',
    method: 'put',
    data: data
  })
}
