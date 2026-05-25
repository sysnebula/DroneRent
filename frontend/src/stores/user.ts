import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, getUserInfo, logout as logoutApi } from '@/api/auth'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<any>(null)

  // 登录
  async function login(username: string, password: string) {
    try {
      const res = await loginApi({ username, password })
      token.value = res.data.token
      localStorage.setItem('token', res.data.token)
      
      // 获取用户信息
      await fetchUserInfo()
      
      return res
    } catch (error) {
      return Promise.reject(error)
    }
  }

  // 获取用户信息
  async function fetchUserInfo() {
    try {
      const res = await getUserInfo()
      userInfo.value = res.data
      return res
    } catch (error) {
      return Promise.reject(error)
    }
  }

  // 登出
  async function logout() {
    try {
      await logoutApi()
    } finally {
      token.value = ''
      userInfo.value = null
      localStorage.removeItem('token')
      router.push('/login')
    }
  }

  return {
    token,
    userInfo,
    login,
    fetchUserInfo,
    logout
  }
})
