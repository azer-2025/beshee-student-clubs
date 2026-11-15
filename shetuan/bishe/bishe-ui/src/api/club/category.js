import request from '@/utils/request'

// 查询社团分类列表
export function listClubCategory(query) {
  return request({
    url: '/club/category/list',
    method: 'get',
    params: query
  })
}

// 查询社团分类详情
export function getClubCategory(categoryId) {
  return request({
    url: '/club/category/' + categoryId,
    method: 'get'
  })
}

// 新增社团分类
export function addClubCategory(data) {
  return request({
    url: '/club/category',
    method: 'post',
    data: data
  })
}

// 修改社团分类
export function updateClubCategory(data) {
  return request({
    url: '/club/category',
    method: 'put',
    data: data
  })
}

// 删除社团分类
export function delClubCategory(categoryId) {
  return request({
    url: '/club/category/' + categoryId,
    method: 'delete'
  })
}
