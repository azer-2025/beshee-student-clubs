import request from '@/utils/request'

// 查询社团公告列表
export function listClubNotice(query) {
  return request({
    url: '/club/notice/list',
    method: 'get',
    params: query
  })
}

// 查询社团公告详情
export function getClubNotice(noticeId) {
  return request({
    url: '/club/notice/' + noticeId,
    method: 'get'
  })
}

// 新增社团公告
export function addClubNotice(data) {
  return request({
    url: '/club/notice',
    method: 'post',
    data: data
  })
}

// 修改社团公告
export function updateClubNotice(data) {
  return request({
    url: '/club/notice',
    method: 'put',
    data: data
  })
}

// 删除社团公告
export function delClubNotice(noticeId) {
  return request({
    url: '/club/notice/' + noticeId,
    method: 'delete'
  })
}
