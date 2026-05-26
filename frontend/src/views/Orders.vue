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
        <el-table-column prop="customerName" label="客户姓名" width="100" />
        <el-table-column prop="droneNo" label="设备编号" width="120" />
        <el-table-column prop="droneModel" label="设备型号" width="120" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="totalAmount" label="总金额" width="100" />
        <el-table-column prop="userName" label="经办人" width="100" />
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
              v-if="row.status === 'PENDING'"
              size="small"
              type="primary"
              @click="handlePay(row)"
            >
              支付
            </el-button>
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
        <el-descriptions-item label="客户姓名">{{ currentOrder.customerName }}</el-descriptions-item>
        <el-descriptions-item label="设备编号">{{ currentOrder.droneNo }}</el-descriptions-item>
        <el-descriptions-item label="设备型号">{{ currentOrder.droneModel }}</el-descriptions-item>
        <el-descriptions-item label="经办人">{{ currentOrder.userName }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ currentOrder.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ currentOrder.endDate }}</el-descriptions-item>
        <el-descriptions-item label="租赁天数">{{ currentOrder.rentalDays }}天</el-descriptions-item>
        <el-descriptions-item label="日租金">¥{{ currentOrder.dailyPrice }}</el-descriptions-item>
        <el-descriptions-item label="总金额">¥{{ currentOrder.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="押金">¥{{ currentOrder.depositAmount }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentOrder.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 新增订单对话框 -->
    <el-dialog v-model="createVisible" title="新增订单" width="600px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="客户" prop="customerId">
          <el-select v-model="form.customerId" placeholder="请选择客户" filterable style="width: 100%">
            <el-option
              v-for="c in customerList"
              :key="c.id"
              :label="`${c.name} (${c.phone})`"
              :value="c.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="无人机" prop="droneId">
          <el-select v-model="form.droneId" placeholder="请选择无人机" filterable style="width: 100%">
            <el-option
              v-for="d in droneList"
              :key="d.id"
              :label="`${d.droneNo} - ${d.brand} ${d.model} (¥${d.dailyRentalPrice}/天)`"
              :value="d.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
            v-model="form.startDate"
            type="date"
            placeholder="选择开始日期"
            value-format="YYYY-MM-DD"
            :disabled-date="disabledStartDate"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker
            v-model="form.endDate"
            type="date"
            placeholder="选择结束日期"
            value-format="YYYY-MM-DD"
            :disabled-date="disabledEndDate"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="取机人">
          <el-input v-model="form.pickupPerson" placeholder="取机人姓名" />
        </el-form-item>

        <el-form-item label="取机人电话">
          <el-input v-model="form.pickupPhone" placeholder="取机人电话" />
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="订单备注" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="createVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确认创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getOrderList, returnOrder, cancelOrder, payOrder, createOrder } from '@/api/order'
import { getCustomerPage } from '@/api/customer'
import { getDroneList } from '@/api/drone'

const loading = ref(false)
const submitting = ref(false)
const detailVisible = ref(false)
const createVisible = ref(false)
const currentOrder = ref<any>(null)
const formRef = ref<FormInstance>()
const customerList = ref<any[]>([])
const droneList = ref<any[]>([])

const form = reactive({
  customerId: null as number | null,
  droneId: null as number | null,
  startDate: '',
  endDate: '',
  pickupPerson: '',
  pickupPhone: '',
  remark: ''
})

const rules: FormRules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  droneId: [{ required: true, message: '请选择无人机', trigger: 'change' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
}

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

// 新增
const handleAdd = () => {
  fetchCustomers()
  fetchIdleDrones()
  createVisible.value = true
}

const resetForm = () => {
  formRef.value?.resetFields()
  form.customerId = null
  form.droneId = null
  form.startDate = ''
  form.endDate = ''
  form.pickupPerson = ''
  form.pickupPhone = ''
  form.remark = ''
}

const disabledStartDate = (date: Date) => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return date.getTime() < today.getTime()
}

const disabledEndDate = (date: Date) => {
  if (!form.startDate) {
    const today = new Date()
    today.setHours(0, 0, 0, 0)
    return date.getTime() < today.getTime()
  }
  const start = new Date(form.startDate)
  start.setHours(0, 0, 0, 0)
  return date.getTime() <= start.getTime()
}

const handleSubmit = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    await createOrder({
      customerId: form.customerId,
      droneId: form.droneId,
      startDate: form.startDate,
      endDate: form.endDate,
      pickupPerson: form.pickupPerson || undefined,
      pickupPhone: form.pickupPhone || undefined,
      remark: form.remark || undefined
    })
    ElMessage.success('订单创建成功')
    createVisible.value = false
    resetForm()
    fetchData()
  } catch (error) {
    console.error('创建订单失败:', error)
  } finally {
    submitting.value = false
  }
}

const fetchCustomers = async () => {
  try {
    const res = await getCustomerPage({ pageNum: 1, pageSize: 1000 })
    customerList.value = res.data.list || []
  } catch (error) {
    console.error('获取客户列表失败:', error)
  }
}

const fetchIdleDrones = async () => {
  try {
    const res = await getDroneList({ pageNum: 1, pageSize: 1000, status: 'IDLE' })
    droneList.value = res.data.list || []
  } catch (error) {
    console.error('获取无人机列表失败:', error)
  }
}

// 查看详情
const handleView = (row: any) => {
  currentOrder.value = row
  detailVisible.value = true
}

// 支付
const handlePay = (row: any) => {
  ElMessageBox.confirm('确认将该订单标记为已支付吗？', '支付确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  }).then(async () => {
    try {
      await payOrder(row.id)
      ElMessage.success('支付成功')
      fetchData()
    } catch (error) {
      console.error('支付失败:', error)
    }
  })
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
