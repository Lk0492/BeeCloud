import request from '@/utils/request'

export function listAttachment(formId) {
  return request({
    url: '/review/attachment/list/' + formId,
    method: 'get'
  })
}

export function addAttachment(data) {
  return request({
    url: '/review/attachment',
    method: 'post',
    data: data
  })
}

export function batchAddAttachment(data) {
  return request({
    url: '/review/attachment/batch',
    method: 'post',
    data: data
  })
}

export function delAttachment(detailIds) {
  return request({
    url: '/review/attachment/' + detailIds,
    method: 'delete'
  })
}

export function getCategoryCount(formId, category) {
  return request({
    url: '/review/attachment/count/' + formId + '/' + category,
    method: 'get'
  })
}
