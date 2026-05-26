import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/Layout.vue'
import Login from '@/views/Login.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: Layout,
      redirect: '/home',
      meta: { requiresAuth: true },
      children: [
        {
          path: 'home',
          name: 'Home',
          component: () => import('@/views/HomeView.vue'),
          meta: { title: '首页' }
        },
        // 业务管理
        {
          path: 'drones',
          name: 'Drones',
          component: () => import('@/views/Drones.vue'),
          meta: { title: '无人机管理' }
        },
        {
          path: 'orders',
          name: 'Orders',
          component: () => import('@/views/Orders.vue'),
          meta: { title: '订单管理' }
        },
        {
          path: 'customers',
          name: 'Customers',
          component: () => import('@/views/Customers.vue'),
          meta: { title: '客户管理' }
        },
        // 维护管理
        {
          path: 'maintenance',
          name: 'Maintenance',
          component: () => import('@/views/MaintenanceRecords.vue'),
          meta: { title: '维修记录' }
        },
        {
          path: 'inventory',
          name: 'Inventory',
          component: () => import('@/views/InventoryLogs.vue'),
          meta: { title: '库存日志' }
        },
        // 系统管理
        {
          path: 'users',
          name: 'Users',
          component: () => import('@/views/Users.vue'),
          meta: { title: '用户管理' }
        },
        {
          path: 'roles',
          name: 'Roles',
          component: () => import('@/views/Roles.vue'),
          meta: { title: '角色管理' }
        },
        // 财务统计
        {
          path: 'finance',
          name: 'Finance',
          component: () => import('@/views/FinanceStats.vue'),
          meta: { title: '财务统计' }
        }
      ]
    }
  ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
