<template>
  <div class="drones">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>无人机管理</h3>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增无人机
          </el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="品牌">
          <el-input v-model="searchForm.brand" placeholder="请输入品牌" clearable />
        </el-form-item>
        <el-form-item label="型号">
          <el-input v-model="searchForm.model" placeholder="请输入型号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="空闲" value="IDLE" />
            <el-option label="出租中" value="RENTED" />
            <el-option label="维修中" value="MAINTENANCE" />
            <el-option label="报废" value="SCRAPPED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="droneNo" label="设备编号" width="120" />
        <el-table-column prop="brand" label="品牌" width="100" />
        <el-table-column prop="model" label="型号" width="150" />
        <el-table-column prop="serialNumber" label="序列号" width="150" />
        <el-table-column prop="dailyRentalPrice" label="日租金" width="100" />
        <el-table-column prop="depositAmount" label="押金" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="250">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-form-item label="设备编号" prop="droneNo">
          <el-input v-model="formData.droneNo" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="formData.brand" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="formData.model" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="序列号">
          <el-input v-model="formData.serialNumber" placeholder="请输入序列号" />
        </el-form-item>
        <el-form-item label="日租金" prop="dailyRentalPrice">
          <el-input-number
            v-model="formData.dailyRentalPrice"
            :min="0.01"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="押金金额" prop="depositAmount">
          <el-input-number
            v-model="formData.depositAmount"
            :min="0"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入描述信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getDroneList, createDrone, updateDrone, deleteDrone } from '@/api/drone'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()

const searchForm = reactive({
  brand: '',
  model: '',
  status: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])

const formData = reactive({
  id: null,
  droneNo: '',
  brand: '',
  model: '',
  serialNumber: '',
  dailyRentalPrice: 0,
  depositAmount: 0,
  description: ''
})

const formRules = {
  droneNo: [{ required: true, message: '请输入设备编号', trigger: 'blur' }],
  brand: [{ required: true, message: '请输入品牌', trigger: 'blur' }],
  model: [{ required: true, message: '请输入型号', trigger: 'blur' }],
  dailyRentalPrice: [{ required: true, message: '请输入日租金', trigger: 'blur' }],
  depositAmount: [{ required: true, message: '请输入押金金额', trigger: 'blur' }]
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getDroneList({
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
  searchForm.brand = ''
  searchForm.model = ''
  searchForm.status = ''
  handleSearch()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增无人机'
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑无人机'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该无人机吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDrone(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (formData.id) {
          await updateDrone(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await createDrone(formData)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        fetchData()
      } catch (error) {
        console.error('提交失败:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.keys(formData).forEach(key => {
    // @ts-ignore
    formData[key] = key === 'id' ? null : ''
  })
  formData.dailyRentalPrice = 0
  formData.depositAmount = 0
}

// 获取状态类型
const getStatusType = (status: string) => {
  const types: Record<string, any> = {
    IDLE: 'success',
    RENTED: 'warning',
    MAINTENANCE: 'info',
    SCRAPPED: 'danger'
  }
  return types[status] || ''
}

// 获取状态文本
const getStatusText = (status: string) => {
  const texts: Record<string, string> = {
    IDLE: '空闲',
    RENTED: '出租中',
    MAINTENANCE: '维修中',
    SCRAPPED: '报废'
  }
  return texts[status] || status
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.drones {
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
