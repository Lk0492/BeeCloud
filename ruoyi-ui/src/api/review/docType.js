import request from '@/utils/request'

export function listDocType(query) {
  return request({
    url: '/review/docType/list',
    method: 'get',
    params: query
  })
}

export function getDocType(typeId) {
  return request({
    url: '/review/docType/' + typeId,
    method: 'get'
  })
}

export function addDocType(data) {
  return request({
    url: '/review/docType',
    method: 'post',
    data: data
  })
}

export function updateDocType(data) {
  return request({
    url: '/review/docType',
    method: 'put',
    data: data
  })
}

export function delDocType(typeIds) {
  return request({
    url: '/review/docType/' + typeIds,
    method: 'delete'
  })
}

export function getDocTypeOptions() {
  return request({
    url: '/review/docType/optionselect',
    method: 'get'
  })
}
