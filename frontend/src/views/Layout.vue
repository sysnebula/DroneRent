<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="220px">
      <div class="logo">
        <el-icon :size="28"><Platform /></el-icon>
        <h3>无人机租赁系统</h3>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item index="/">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        
        <el-sub-menu index="business">
          <template #title>
            <el-icon><Briefcase /></el-icon>
            <span>业务管理</span>
          </template>
          <el-menu-item index="/drones">
            <el-icon><VideoCamera /></el-icon>
            <span>无人机管理</span>
          </el-menu-item>
          <el-menu-item index="/orders">
            <el-icon><Document /></el-icon>
            <span>订单管理</span>
          </el-menu-item>
          <el-menu-item index="/customers">
            <el-icon><User /></el-icon>
            <span>客户管理</span>
          </el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="maintenance">
          <template #title>
            <el-icon><Tools /></el-icon>
            <span>维护管理</span>
          </template>
          <el-menu-item index="/maintenance">
            <el-icon><Setting /></el-icon>
            <span>维修记录</span>
          </el-menu-item>
          <el-menu-item index="/inventory">
            <el-icon><Box /></el-icon>
            <span>库存日志</span>
          </el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/users">
            <el-icon><UserFilled /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/roles">
            <el-icon><Lock /></el-icon>
            <span>角色管理</span>
          </el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/finance">
          <el-icon><Money /></el-icon>
          <span>财务统计</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主体内容 -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header>
        <div class="header-content">
          <div class="left-section">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item v-if="currentRouteTitle">{{ currentRouteTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="right-section">
            <el-badge :value="0" :hidden="true">
              <el-icon :size="20" class="icon-btn"><Bell /></el-icon>
            </el-badge>
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-avatar :size="32" :icon="UserFilled" />
                <span class="user-name">{{ userStore.userInfo?.realName || '管理员' }}</span>
                <el-tag size="small" type="success">{{ userStore.userInfo?.role || 'ADMIN' }}</el-tag>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <!-- 主要内容区域 -->
      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  HomeFilled,
  VideoCamera,
  Document,
  User,
  UserFilled,
  SwitchButton,
  Platform,
  Briefcase,
  Tools,
  Setting,
  Box,
  Lock,
  Money,
  Bell
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const currentRouteTitle = computed(() => route.meta.title as string || '')

// 处理下拉菜单命令
const handleCommand = (command: string) => {
  if (command === 'logout') {
    userStore.logout()
  } else if (command === 'profile') {
    // TODO: 跳转到个人中心
    console.log('个人中心')
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.el-aside {
  background-color: #304156;
  color: #fff;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  background-color: #2b3a4b;
  color: #fff;
}

.logo h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.left-section {
  display: flex;
  align-items: center;
}

.right-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.icon-btn {
  cursor: pointer;
  transition: color 0.3s;
}

.icon-btn:hover {
  color: #409eff;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.user-name {
  font-weight: 500;
  color: #303133;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}

/* 路由过渡动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-10px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(10px);
}
</style>
