<template>
  <div v-loading="loading">
    <!-- 4 个统计卡片 -->
    <el-row :gutter="20">
      <el-col :xs="12" :sm="6" v-for="card in cards" :key="card.label">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-row">
            <div :class="['stat-icon', card.cls]">
              <el-icon :size="22"><component :is="card.icon" /></el-icon>
            </div>
            <div>
              <div class="stat-label">{{ card.label }}</div>
              <div class="stat-value">{{ card.value }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 两个图表 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :xs="24" :md="14">
        <el-card shadow="hover">
          <template #header>各分类产品数</template>
          <div ref="barRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="10">
        <el-card shadow="hover">
          <template #header>询价状态分布</template>
          <div ref="pieRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>近 7 天询价趋势</template>
          <div ref="lineRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, markRaw } from 'vue'
import * as echarts from 'echarts'
import { getStats } from '../api/stats'

const loading = ref(true)
const cards = ref([
  { label: '产品总数', value: 0, icon: 'Goods',   cls: 'icon-blue' },
  { label: '分类总数', value: 0, icon: 'Menu',    cls: 'icon-green' },
  { label: '询价总数', value: 0, icon: 'Tickets', cls: 'icon-orange' },
  { label: '待处理',   value: 0, icon: 'Bell',    cls: 'icon-red' },
])

const barRef = ref()
const pieRef = ref()
const lineRef = ref()

// 图表实例（用 markRaw 避免被 Vue 包成响应式对象，节省开销）
let barChart = null
let pieChart = null
let lineChart = null

// 状态值的中文映射
const STATUS_LABEL = { PENDING: '待处理', CONTACTED: '已联系', CLOSED: '已成交' }

function renderBar(data) {
  barChart = markRaw(echarts.init(barRef.value))
  barChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 50, right: 20, top: 20, bottom: 80 },
    xAxis: {
      type: 'category',
      data: data.map((d) => d.name),
      axisLabel: { rotate: 35, interval: 0, fontSize: 11 },
    },
    yAxis: { type: 'value', minInterval: 1 },
    series: [
      {
        type: 'bar',
        data: data.map((d) => d.count),
        itemStyle: { color: '#409eff', borderRadius: [4, 4, 0, 0] },
        barMaxWidth: 32,
      },
    ],
  })
}

function renderPie(data) {
  pieChart = markRaw(echarts.init(pieRef.value))
  pieChart.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [
      {
        type: 'pie',
        radius: ['40%', '65%'],
        center: ['50%', '45%'],
        label: { formatter: '{b}: {c}' },
        data: data.map((d) => ({
          name: STATUS_LABEL[d.status] || d.status,
          value: d.count,
        })),
        color: ['#f56c6c', '#e6a23c', '#67c23a'],
      },
    ],
  })
}

function renderLine(data) {
  lineChart = markRaw(echarts.init(lineRef.value))
  lineChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 50, right: 20, top: 20, bottom: 40 },
    xAxis: { type: 'category', data: data.map((d) => d.date), boundaryGap: false },
    yAxis: { type: 'value', minInterval: 1 },
    series: [
      {
        type: 'line',
        smooth: true,
        data: data.map((d) => d.count),
        itemStyle: { color: '#67c23a' },
        areaStyle: { color: 'rgba(103,194,58,0.15)' },
      },
    ],
  })
}

function onResize() {
  barChart?.resize()
  pieChart?.resize()
  lineChart?.resize()
}

async function load() {
  loading.value = true
  try {
    const stats = await getStats()
    cards.value[0].value = stats.totalProducts
    cards.value[1].value = stats.totalCategories
    cards.value[2].value = stats.totalInquiries
    cards.value[3].value = stats.pendingInquiries
    renderBar(stats.productsByCategory || [])
    renderPie(stats.inquiryByStatus || [])
    renderLine(stats.inquiryTrend || [])
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  load()
  window.addEventListener('resize', onResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', onResize)
  barChart?.dispose()
  pieChart?.dispose()
  lineChart?.dispose()
})
</script>

<style scoped>
.stat-card {
  margin-bottom: 12px;
}
.stat-row {
  display: flex;
  align-items: center;
  gap: 14px;
}
.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}
.icon-blue   { background: #409eff; }
.icon-green  { background: #67c23a; }
.icon-orange { background: #e6a23c; }
.icon-red    { background: #f56c6c; }

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 4px;
}
.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}
.chart-box {
  width: 100%;
  height: 320px;
}
</style>
