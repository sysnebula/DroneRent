import request from '@/utils/request'

// 无人机列表（分页）
export function getDroneList(params: {
  pageNum: number
  pageSize: number
  brand?: string
  model?: string
  status?: string
}) {
  return request({
    url: '/drones/page',
    method: 'get',
    params
  })
}

// 获取无人机详情
export function getDroneById(id: number) {
  return request({
    url: `/drones/${id}`,
    method: 'get'
  })
}

// 创建无人机
export function createDrone(data: any) {
  return request({
    url: '/drones',
    method: 'post',
    data
  })
}

// 更新无人机
export function updateDrone(id: number, data: any) {
  return request({
    url: `/drones/${id}`,
    method: 'put',
    data
  })
}

// 删除无人机
export function deleteDrone(id: number) {
  return request({
    url: `/drones/${id}`,
    method: 'delete'
  })
}

// 批量删除无人机
export function batchDeleteDrones(ids: number[]) {
  return request({
    url: '/drones/batch',
    method: 'delete',
    data: ids
  })
}

// 更新无人机状态
export function updateDroneStatus(id: number, newStatus: string) {
  return request({
    url: `/drones/${id}/status`,
    method: 'patch',
    params: { newStatus }
  })
}
