import request from '@/utils/request'

export function listNotice(query) {
  return request({
    url: '/review/notice/list',
    method: 'get',
    params: query
  })
}

export function getUnreadCount() {
  return request({
    url: '/review/notice/unreadCount',
    method: 'get'
  })
}

export function getNotice(noticeId) {
  return request({
    url: '/review/notice/' + noticeId,
    method: 'get'
  })
}

export function markRead(noticeId) {
  return request({
    url: '/review/notice/read/' + noticeId,
    method: 'put'
  })
}

export function markAllRead() {
  return request({
    url: '/review/notice/readAll',
    method: 'put'
  })
}

export function delNotice(noticeIds) {
  return request({
    url: '/review/notice/' + noticeIds,
    method: 'delete'
  })
}
