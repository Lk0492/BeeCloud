import request from '@/utils/request'

// 查询知识库列表
export function listArticle(query) {
  return request({
    url: '/help/article/list',
    method: 'get',
    params: query
  })
}

// 查询知识库详情
export function getArticle(articleId) {
  return request({
    url: '/help/article/' + articleId,
    method: 'get'
  })
}

// 新增知识库
export function addArticle(data) {
  return request({
    url: '/help/article',
    method: 'post',
    data: data
  })
}

// 修改知识库
export function updateArticle(data) {
  return request({
    url: '/help/article',
    method: 'put',
    data: data
  })
}

// 删除知识库
export function delArticle(articleIds) {
  return request({
    url: '/help/article/' + articleIds,
    method: 'delete'
  })
}

// 导出知识库
export function exportArticle(query) {
  return request({
    url: '/help/article/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 获取热门问题
export function getHotArticles() {
  return request({
    url: '/help/hot',
    method: 'get'
  })
}

// 关键词搜索文章
export function searchArticles(keyword) {
  return request({
    url: '/help/search',
    method: 'get',
    params: { keyword }
  })
}

// 获取文章详情（增加浏览量）
export function getArticleDetail(articleId) {
  return request({
    url: '/help/detail/' + articleId,
    method: 'get'
  })
}

// 获取所有分类
export function getCategories() {
  return request({
    url: '/help/categories',
    method: 'get'
  })
}

// 按分类查询文章
export function getArticlesByCategory(categoryId) {
  return request({
    url: '/help/byCategory/' + categoryId,
    method: 'get'
  })
}

// 提交问题反馈
export function submitFeedback(data) {
  return request({
    url: '/help/feedback',
    method: 'post',
    data: data
  })
}

// 获取我的反馈记录
export function getMyFeedback() {
  return request({
    url: '/help/myFeedback',
    method: 'get'
  })
}
