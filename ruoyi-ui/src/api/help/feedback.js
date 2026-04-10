import request from '@/utils/request'

// 查询反馈列表
export function listFeedback(query) {
  return request({
    url: '/help/feedback/list',
    method: 'get',
    params: query
  })
}

// 查询反馈详情
export function getFeedback(feedbackId) {
  return request({
    url: '/help/feedback/' + feedbackId,
    method: 'get'
  })
}

// 回复反馈
export function replyFeedback(data) {
  return request({
    url: '/help/feedback',
    method: 'put',
    data: data
  })
}

// 删除反馈
export function delFeedback(feedbackIds) {
  return request({
    url: '/help/feedback/' + feedbackIds,
    method: 'delete'
  })
}

// 导出反馈
export function exportFeedback(query) {
  return request({
    url: '/help/feedback/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 获取待回复数量
export function getPendingCount() {
  return request({
    url: '/help/feedback/pendingCount',
    method: 'get'
  })
}
