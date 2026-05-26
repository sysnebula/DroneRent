import request from '@/utils/request'

// 订单列表（分页）
export function getOrderList(params: {
  pageNum: number
  pageSize: number
  status?: string
  customerId?: number
}) {
  return request({
    url: '/orders/page',
    method: 'get',
    params
  })
}

// 获取订单详情
export function getOrderById(id: number) {
  return request({
    url: `/orders/${id}`,
    method: 'get'
  })
}

// 创建订单
export function createOrder(data: any) {
  return request({
    url: '/orders',
    method: 'post',
    data
  })
}

// 更新订单
export function updateOrder(id: number, data: any) {
  return request({
    url: `/orders/${id}`,
    method: 'put',
    data
  })
}

// 删除订单
export function deleteOrder(id: number) {
  return request({
    url: `/orders/${id}`,
    method: 'delete'
  })
}

// 支付订单
export function payOrder(id: number, paymentMethod: string = 'CASH') {
  return request({
    url: `/orders/${id}/pay`,
    method: 'post',
    params: { paymentMethod }
  })
}

// 续租订单
export function extendOrder(id: number, newEndDate: string) {
  return request({
    url: `/orders/${id}/extend`,
    method: 'post',
    params: { newEndDate }
  })
}

// 归还订单
export function returnOrder(id: number, returnPerson?: string, returnPhone?: string) {
  return request({
    url: `/orders/${id}/return`,
    method: 'post',
    params: { returnPerson, returnPhone }
  })
}

// 取消订单
export function cancelOrder(id: number, cancelReason: string) {
  return request({
    url: `/orders/${id}/cancel`,
    method: 'post',
    params: { cancelReason }
  })
}
