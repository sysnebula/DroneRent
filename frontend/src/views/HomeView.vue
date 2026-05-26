<template>
  <div class="home">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card" v-loading="loading">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#409eff"><VideoCamera /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalDrones }}</div>
              <div class="stat-label">无人机总数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card" v-loading="loading">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#67c23a"><CircleCheck /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.idleDrones }}</div>
              <div class="stat-label">空闲设备</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card" v-loading="loading">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#e6a23c"><Document /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.activeOrders }}</div>
              <div class="stat-label">进行中订单</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card" v-loading="loading">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#f56c6c"><User /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.customerCount }}</div>
              <div class="stat-label">客户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="welcome-card" style="margin-top: 20px">
      <h2>欢迎使用无人机租赁管理系统</h2>
      <p>这是一个专业的无人机租赁管理平台，帮助您高效管理设备、订单和客户。</p>

      <el-divider />

      <h3>主要功能</h3>
      <ul>
        <li>✅ 无人机设备管理 - 添加、编辑、删除无人机设备</li>
        <li>✅ 订单管理 - 创建、续租、归还、取消订单</li>
        <li>✅ 客户管理 - 管理客户信息和信用评分</li>
        <li>✅ 状态跟踪 - 实时跟踪设备和订单状态</li>
      </ul>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { VideoCamera, CircleCheck, Document, User } from '@element-plus/icons-vue'
import { getFinanceStatistics } from '@/api/finance'

const loading = ref(true)

const stats = reactive({
  totalDrones: 0,
  idleDrones: 0,
  activeOrders: 0,
  customerCount: 0
})

const fetchStats = async () => {
  loading.value = true
  try {
    const res = await getFinanceStatistics()
    stats.totalDrones = res.data.totalDrones || 0
    stats.idleDrones = res.data.idleDrones || 0
    stats.activeOrders = res.data.activeOrders || 0
    stats.customerCount = res.data.customerCount || 0
  } catch (error) {
    console.error('获取统计数据失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
.home {
  padding: 20px;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  font-size: 48px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}

.welcome-card h2 {
  margin-top: 0;
  color: #333;
}

.welcome-card h3 {
  color: #666;
}

.welcome-card ul {
  list-style: none;
  padding: 0;
}

.welcome-card li {
  padding: 8px 0;
  color: #666;
}
</style>
