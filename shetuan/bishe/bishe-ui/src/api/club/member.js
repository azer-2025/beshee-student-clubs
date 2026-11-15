import request from '@/utils/request'

// 查询成员列表
export function listClubMember(query) {
  return request({
    url: '/club/member/list',
    method: 'get',
    params: query
  })
}

// 审核成员
export function approveClubMember(data) {
  return request({
    url: '/club/member/approve',
    method: 'put',
    data: data
  })
}

// 修改成员信息
export function updateClubMember(data) {
  return request({
    url: '/club/member',
    method: 'put',
    data: data
  })
}

// 删除成员
export function delClubMember(memberId) {
  return request({
    url: '/club/member/' + memberId,
    method: 'delete'
  })
}
