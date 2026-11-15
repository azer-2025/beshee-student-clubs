import request from '@/utils/request'

// 查询活动报名列表
export function listActivitySignup(query) {
  return request({
    url: '/activity/signup/list',
    method: 'get',
    params: query
  })
}

// 查询活动报名详情
export function getActivitySignup(signupId) {
  return request({
    url: '/activity/signup/' + signupId,
    method: 'get'
  })
}

// 新增活动报名
export function addActivitySignup(data) {
  return request({
    url: '/activity/signup',
    method: 'post',
    data: data
  })
}

// 修改活动报名
export function updateActivitySignup(data) {
  return request({
    url: '/activity/signup',
    method: 'put',
    data: data
  })
}

// 删除活动报名
export function delActivitySignup(signupId) {
  return request({
    url: '/activity/signup/' + signupId,
    method: 'delete'
  })
}

// 审核活动报名
export function approveActivitySignup(data) {
  return request({
    url: '/activity/signup/approve',
    method: 'put',
    data: data
  })
}
