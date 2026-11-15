import request from '@/utils/request'

// 查询轮播图列表
export function listCarousel(query) {
  return request({
    url: '/content/carousel/list',
    method: 'get',
    params: query
  })
}

// 查询轮播图详情
export function getCarousel(carouselId) {
  return request({
    url: '/content/carousel/' + carouselId,
    method: 'get'
  })
}

// 新增轮播图
export function addCarousel(data) {
  return request({
    url: '/content/carousel',
    method: 'post',
    data: data
  })
}

// 修改轮播图
export function updateCarousel(data) {
  return request({
    url: '/content/carousel',
    method: 'put',
    data: data
  })
}

// 删除轮播图
export function delCarousel(carouselId) {
  return request({
    url: '/content/carousel/' + carouselId,
    method: 'delete'
  })
}
