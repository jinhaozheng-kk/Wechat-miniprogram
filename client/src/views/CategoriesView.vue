<template>
  <el-card v-loading="loading">
    <el-table :data="rows" stripe>
      <el-table-column label="图标" width="80" align="center">
        <template #default="{ row }">
          <div class="icon-box" :style="{ background: row.bgColor, color: row.color }">
            {{ row.icon }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="编码" width="160" />
      <el-table-column prop="name" label="名称" width="200" />
      <el-table-column prop="description" label="简介" min-width="280" show-overflow-tooltip />
      <el-table-column prop="productCount" label="产品数" width="100" align="center" />
      <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listCategories } from '../api/categories'

const loading = ref(true)
const rows = ref([])

onMounted(async () => {
  try {
    rows.value = await listCategories()
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.icon-box {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}
</style>
