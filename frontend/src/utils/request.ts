import axios from 'axios'
import { ElMessage, ElLoading } from 'element-plus'
import type { LoadingInstance } from 'element-plus/es/components/loading/src/loading'

// 创建 axios 实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Loading 实例管理
let loadingInstance: LoadingInstance | null = null
let requestCount = 0

/**
 * 显示 Loading
 */
const showLoading = () => {
  if (requestCount === 0) {
    loadingInstance = ElLoading.service({
      lock: true,
      text: '加载中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
  }
  requestCount++
}

/**
 * 隐藏 Loading
 */
const hideLoading = () => {
  requestCount--
  if (requestCount <= 0) {
    requestCount = 0
    loadingInstance?.close()
    loadingInstance = null
  }
}

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 显示 Loading（可以通过 config.hideLoading 控制）
    if (!config.hideLoading) {
      showLoading()
    }
    
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    hideLoading()
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    // 隐藏 Loading
    hideLoading()
    
    const res = response.data
    
    // 如果返回的状态码不是 200，则认为是错误
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      
      // 401: 未授权，跳转到登录页
      if (res.code === 401) {
        localStorage.removeItem('token')
        window.location.href = '/login'
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    } else {
      return res
    }
  },
  (error) => {
    // 隐藏 Loading
    hideLoading()
    
    console.error('响应错误:', error)
    
    let message = '网络错误，请稍后重试'
    
    if (error.response) {
      switch (error.response.status) {
        case 401:
          message = '未授权，请重新登录'
          localStorage.removeItem('token')
          window.location.href = '/login'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求的资源不存在'
          break
        case 500:
          message = '服务器错误'
          break
        default:
          message = error.response.data?.message || '请求失败'
      }
    }
    
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default service
