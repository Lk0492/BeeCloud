import request from '@/utils/request'

export function getCurrentForm() {
  return request({
    url: '/review/submit/current',
    method: 'get'
  })
}

export function submitForm(data) {
  return request({
    url: '/review/submit/doSubmit',
    method: 'post',
    data: data
  })
}

export function resubmitForm(data) {
  return request({
    url: '/review/submit/resubmit',
    method: 'post',
    data: data
  })
}

export function getHistory() {
  return request({
    url: '/review/submit/history',
    method: 'get'
  })
}

export function getStatusCounts() {
  return request({
    url: '/review/submit/statusCounts',
    method: 'get'
  })
}
