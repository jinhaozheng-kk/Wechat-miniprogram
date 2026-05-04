<template>
  <el-card v-loading="loading">
    <!-- 筛选条 -->
    <div class="toolbar">
      <el-select
        v-model="filter.categoryId"
        placeholder="选择分类"
        clearable
        style="width: 200px"
        @change="load"
      >
        <el-option
          v-for="c in categories"
          :key="c.id"
          :label="c.name"
          :value="c.id"
        />
      </el-select>
      <el-input
        v-model="filter.keyword"
        placeholder="搜索产品名称 / 型号"
        clearable
        style="width: 280px"
        @keyup.enter="load"
        @clear="load"
      >
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-button type="primary" @click="load">查询</el-button>
    </div>

    <!-- 产品列表 -->
    <el-table :data="rows" stripe>
      <el-table-column prop="id" label="编码" width="120" />
      <el-table-column label="分类" width="160">
        <template #default="{ row }">
          {{ categoryNameMap[row.categoryId] || row.categoryId }}
        </template>
      </el-table-column>
      <el-table-column prop="name" label="产品名称" min-width="240" show-overflow-tooltip />
      <el-table-column prop="model" label="型号" width="160" show-overflow-tooltip />
      <el-table-column label="规格" min-width="240">
        <template #default="{ row }">
          <el-tag
            v-for="(s, idx) in (row.specs || []).slice(0, 2)"
            :key="idx"
            type="info"
            size="small"
            style="margin-right: 4px"
          >
            {{ s.key }}: {{ s.value }}
          </el-tag>
          <span v-if="(row.specs || []).length > 2" class="more">…</span>
        </template>
      </el-table-column>
      <el-table-column label="标签" width="200">
        <template #default="{ row }">
          <el-tag v-for="t in row.tags || []" :key="t" size="small" style="margin-right: 4px">
            {{ t }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { listProducts } from '../api/products'
import { listCategories } from '../api/categories'

const loading = ref(true)
const rows = ref([])
const categories = ref([])
const filter = reactive({ categoryId: '', keyword: '' })

const categoryNameMap = computed(() => {
  const m = {}
  for (const c of categories.value) m[c.id] = c.name
  return m
})

async function load() {
  loading.value = true
  try {
    rows.value = await listProducts({
      categoryId: filter.categoryId || undefined,
      keyword: filter.keyword || undefined,
    })
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  // 分类与产品并发请求
  ;[categories.value] = await Promise.all([listCategories(), load()])
})
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}
.more {
  color: #909399;
}
</style>
