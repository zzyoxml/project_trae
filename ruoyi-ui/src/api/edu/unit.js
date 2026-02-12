import request from '@/utils/request'

export function listUnit(query) {
  return request({
    url: '/edu/unit/list',
    method: 'get',
    params: query
  })
}

export function listUnitByCourse(courseId) {
  return request({
    url: '/edu/unit/byCourse/' + courseId,
    method: 'get'
  })
}

export function getUnit(unitId) {
  return request({
    url: '/edu/unit/' + unitId,
    method: 'get'
  })
}

export function addUnit(data) {
  return request({
    url: '/edu/unit',
    method: 'post',
    data: data
  })
}

export function updateUnit(data) {
  return request({
    url: '/edu/unit',
    method: 'put',
    data: data
  })
}

export function delUnit(unitIds) {
  return request({
    url: '/edu/unit/' + unitIds,
    method: 'delete'
  })
}