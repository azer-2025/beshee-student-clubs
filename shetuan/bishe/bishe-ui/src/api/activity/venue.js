import request from '@/utils/request'

// 查询场地申请列表
export function listVenueApply(query) {
  return request({
    url: '/activity/venue/list',
    method: 'get',
    params: query
  })
}

// 查询场地申请详情
export function getVenueApply(venueApplyId) {
  return request({
    url: '/activity/venue/' + venueApplyId,
    method: 'get'
  })
}

// 新增场地申请
export function addVenueApply(data) {
  return request({
    url: '/activity/venue',
    method: 'post',
    data: data
  })
}

// 修改场地申请
export function updateVenueApply(data) {
  return request({
    url: '/activity/venue',
    method: 'put',
    data: data
  })
}

// 删除场地申请
export function delVenueApply(venueApplyId) {
  return request({
    url: '/activity/venue/' + venueApplyId,
    method: 'delete'
  })
}

// 审核场地申请
export function approveVenueApply(data) {
  return request({
    url: '/activity/venue/approve',
    method: 'put',
    data: data
  })
}
