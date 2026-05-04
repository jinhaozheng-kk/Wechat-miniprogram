<template>
  <el-card v-loading="loading">
    <div class="toolbar">
      <el-select
        v-model="filterStatus"
        placeholder="按状态筛选"
        clearable
        style="width: 200px"
        @change="load"
      >
        <el-option label="待处理" value="PENDING" />
        <el-option label="已联系" value="CONTACTED" />
        <el-option label="已成交" value="CLOSED" />
      </el-select>
      <el-button @click="load"><el-icon><Refresh /></el-icon>刷新</el-button>
    </div>

    <el-table :data="rows" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="contactName" label="联系人" width="100" />
      <el-table-column prop="contactPhone" label="电话" width="130" />
      <el-table-column prop="city" label="城市" width="90" />
      <el-table-column prop="projectName" label="工程" min-width="200" show-overflow-tooltip />
      <el-table-column prop="productNames" label="产品" min-width="240" show-overflow-tooltip />
      <el-table-column prop="requirement" label="需求" min-width="220" show-overflow-tooltip />
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="STATUS_TYPE[row.status]" effect="plain">
            {{ STATUS_LABEL[row.status] || row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="提交时间" width="170">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-dropdown @command="(cmd) => updateStatus(row, cmd)" trigger="click">
            <el-button size="small">
              更新状态<el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="PENDING" :disabled="row.status === 'PENDING'">
                  待处理
                </el-dropdown-item>
                <el-dropdown-item command="CONTACTED" :disabled="row.status === 'CONTACTED'">
                  已联系
                </el-dropdown-item>
                <el-dropdown-item command="CLOSED" :disabled="row.status === 'CLOSED'">
                  已成交
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listInquiries, updateInquiryStatus } from '../api/inquiries'

const STATUS_LABEL = { PENDING: '待处理', CONTACTED: '已联系', CLOSED: '已成交' }
const STATUS_TYPE = { PENDING: 'danger', CONTACTED: 'warning', CLOSED: 'success' }

const loading = ref(true)
const rows = ref([])
const filterStatus = ref('')

async function load() {
  loading.value = true
  try {
    rows.value = await listInquiries({ status: filterStatus.value || undefined })
  } finally {
    loading.value = false
  }
}

async function updateStatus(row, status) {
  await updateInquiryStatus(row.id, status)
  ElMessage.success('状态已更新')
  row.status = status
}

/** 把 ISO 时间格式化成 YYYY-MM-DD HH:mm */
function formatTime(s) {
  if (!s) return ''
  const d = new Date(s)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
}

onMounted(load)
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}
</style>
