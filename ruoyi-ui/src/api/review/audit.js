import request from '@/utils/request'

export function listAudit(query) {
  return request({
    url: '/review/audit/list',
    method: 'get',
    params: query
  })
}

export function getAuditInfo(formId) {
  return request({
    url: '/review/audit/' + formId,
    method: 'get'
  })
}

export function acceptForm(formId, data) {
  return request({
    url: '/review/audit/accept/' + formId,
    method: 'post',
    data: data
  })
}

export function rejectForm(formId, data) {
  return request({
    url: '/review/audit/reject/' + formId,
    method: 'post',
    data: data
  })
}

export function requestSupply(formId, data) {
  return request({
    url: '/review/audit/supply/' + formId,
    method: 'post',
    data: data
  })
}

export function batchAccept(data) {
  return request({
    url: '/review/audit/batchAccept',
    method: 'post',
    data: data
  })
}

export function batchReject(data) {
  return request({
    url: '/review/audit/batchReject',
    method: 'post',
    data: data
  })
}

export function assignReviewer(formId, data) {
  return request({
    url: '/review/audit/assign/' + formId,
    method: 'post',
    data: data
  })
}

export function getStatistics() {
  return request({
    url: '/review/audit/statistics',
    method: 'get'
  })
}

export function getAuditStatusCounts() {
  return request({
    url: '/review/audit/statusCounts',
    method: 'get'
  })
}
