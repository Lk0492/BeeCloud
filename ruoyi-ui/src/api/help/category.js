import request from '@/utils/request'

// 查询分类列表
export function listCategory(query) {
  return request({
    url: '/help/category/list',
    method: 'get',
    params: query
  })
}

// 查询分类详情
export function getCategory(categoryId) {
  return request({
    url: '/help/category/' + categoryId,
    method: 'get'
  })
}

// 新增分类
export function addCategory(data) {
  return request({
    url: '/help/category',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateCategory(data) {
  return request({
    url: '/help/category',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delCategory(categoryIds) {
  return request({
    url: '/help/category/' + categoryIds,
    method: 'delete'
  })
}

// 导出分类
export function exportCategory(query) {
  return request({
    url: '/help/category/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 获取分类下拉树列表
export function treeselect() {
  return request({
    url: '/help/category/treeselect',
    method: 'get'
  })
}
