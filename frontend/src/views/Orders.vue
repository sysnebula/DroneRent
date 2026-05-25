<template>
  <div class="orders">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>订单管理</h3>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增订单
          </el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待支付" value="PENDING" />
            <el-option label="已支付" value="PAID" />
            <el-option label="租赁中" value="RENTING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="orderNo" label="订单编号" width="180" />
        <el-table-column prop="customerId" label="客户ID" width="100" />
        <el-table-column prop="droneNo" label="设备编号" width="120" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="totalAmount" label="总金额" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="300">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">详情</el-button>
            <el-button 
              v-if="row.status === 'RENTING'" 
              size="small" 
              type="success"
              @click="handleReturn(row)"
            >
              归还
            </el-button>
            <el-button 
              v-if="row.status === 'PENDING'" 
              size="small" 
              type="danger"
              @click="handleCancel(row)"
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchData"
        @current-change="fetchData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 订单详情对话框 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="600px">
      <el-descriptions :column="2" border v-if="currentOrder">
        <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentOrder.status)">
            {{ getStatusText(currentOrder.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="客户ID">{{ currentOrder.customerId }}</el-descriptions-item>
        <el-descriptions-item label="设备编号">{{ currentOrder.droneNo }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ currentOrder.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ currentOrder.endDate }}</el-descriptions-item>
        <el-descriptions-item label="租赁天数">{{ currentOrder.rentalDays }}天</el-descriptions-item>
        <el-descriptions-item label="日租金">¥{{ currentOrder.dailyPrice }}</el-descriptions-item>
        <el-descriptions-item label="总金额">¥{{ currentOrder.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="押金">¥{{ currentOrder.depositAmount }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentOrder.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getOrderList, returnOrder, cancelOrder } from '@/api/order'

const loading = ref(false)
const detailVisible = ref(false)
const currentOrder = ref<any>(null)

const searchForm = reactive({
  status: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getOrderList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    })
    tableData.value = res.data.list
    pagination.total = res.data.total
  } catch (error) {
    console.error('获取数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

// 重置
const handleReset = () => {
  searchForm.status = ''
  handleSearch()
}

// 新增（简化版，实际应该跳转到表单页面）
const handleAdd = () => {
  ElMessage.info('请使用后端 API 创建订单')
}

// 查看详情
const handleView = (row: any) => {
  currentOrder.value = row
  detailVisible.value = true
}

// 归还
const handleReturn = (row: any) => {
  ElMessageBox.confirm('确定要归还该订单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await returnOrder(row.id)
      ElMessage.success('归还成功')
      fetchData()
    } catch (error) {
      console.error('归还失败:', error)
    }
  })
}

// 取消
const handleCancel = (row: any) => {
  ElMessageBox.prompt('请输入取消原因', '取消订单', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /.+/,
    inputErrorMessage: '请输入取消原因'
  }).then(async ({ value }) => {
    try {
      await cancelOrder(row.id, value)
      ElMessage.success('订单已取消')
      fetchData()
    } catch (error) {
      console.error('取消失败:', error)
    }
  })
}

// 获取状态类型
const getStatusType = (status: string) => {
  const types: Record<string, any> = {
    PENDING: 'info',
    PAID: 'primary',
    RENTING: 'warning',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return types[status] || ''
}

// 获取状态文本
const getStatusText = (status: string) => {
  const texts: Record<string, string> = {
    PENDING: '待支付',
    PAID: '已支付',
    RENTING: '租赁中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return texts[status] || status
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.orders {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
}

.search-form {
  margin-bottom: 20px;
}
</style>
