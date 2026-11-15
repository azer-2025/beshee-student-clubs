import request from '@/utils/request'

// 查询活动列表
export function listActivityInfo(query) {
  return request({
    url: '/activity/info/list',
    method: 'get',
    params: query
  })
}

// 查询活动审核列表
export function listActivityApprove(query) {
  return request({
    url: '/activity/info/approve/list',
    method: 'get',
    params: query
  })
}

// 查询活动详情
export function getActivityInfo(activityId) {
  return request({
    url: '/activity/info/' + activityId,
    method: 'get'
  })
}

// 新增活动
export function addActivityInfo(data) {
  return request({
    url: '/activity/info',
    method: 'post',
    data: data
  })
}

// 修改活动
export function updateActivityInfo(data) {
  return request({
    url: '/activity/info',
    method: 'put',
    data: data
  })
}

// 删除活动
export function delActivityInfo(activityId) {
  return request({
    url: '/activity/info/' + activityId,
    method: 'delete'
  })
}

// 审核活动
export function approveActivity(data) {
  return request({
    url: '/activity/info/approve',
    method: 'put',
    data: data
  })
}
