import request from '@/utils/request'

export function listRecord(query) {
  return request({
    url: '/review/auditRecord/list',
    method: 'get',
    params: query
  })
}

export function getRecord(recordId) {
  return request({
    url: '/review/auditRecord/' + recordId,
    method: 'get'
  })
}

export function getRecordsByFormId(formId) {
  return request({
    url: '/review/auditRecord/form/' + formId,
    method: 'get'
  })
}
