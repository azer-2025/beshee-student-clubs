<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb8">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">社团总数</div>
            <div class="stat-value">{{ overview.clubTotal }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">成员总数</div>
            <div class="stat-value">{{ overview.memberTotal }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">活动总数</div>
            <div class="stat-value">{{ overview.activityTotal }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">报名总数</div>
            <div class="stat-value">{{ overview.signupTotal }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <div class="chart-title">总量分布</div>
          <div ref="totalChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div class="chart-title">待审核事项</div>
          <div ref="pendingChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="StatsOverview">
import { nextTick, onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import * as echarts from 'echarts'
import { getStatsOverview } from '@/api/stats/overview'

const overview = reactive({
  clubTotal: 0,
  clubPending: 0,
  memberTotal: 0,
  activityTotal: 0,
  activityPending: 0,
  signupTotal: 0,
  fundingPending: 0,
  venuePending: 0
})

const totalChart = ref(null)
const pendingChart = ref(null)
let totalInstance = null
let pendingInstance = null

function renderCharts() {
  if (!totalChart.value || !pendingChart.value) return
  if (!totalInstance) {
    totalInstance = echarts.init(totalChart.value)
  }
  if (!pendingInstance) {
    pendingInstance = echarts.init(pendingChart.value)
  }

  totalInstance.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 30, right: 20, top: 20, bottom: 30 },
    xAxis: {
      type: 'category',
      data: ['社团', '成员', '活动', '报名']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        type: 'bar',
        data: [overview.clubTotal, overview.memberTotal, overview.activityTotal, overview.signupTotal],
        itemStyle: { color: '#409EFF' },
        barWidth: 40
      }
    ]
  })

  pendingInstance.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 30, right: 20, top: 20, bottom: 30 },
    xAxis: {
      type: 'category',
      data: ['社团审核', '活动审核', '经费申请', '场地申请']
    },
    yAxis: { type: 'value' },
    series: [
      {
        type: 'bar',
        data: [overview.clubPending, overview.activityPending, overview.fundingPending, overview.venuePending],
        itemStyle: { color: '#67C23A' },
        barWidth: 40
      }
    ]
  })
}

function resizeCharts() {
  totalInstance && totalInstance.resize()
  pendingInstance && pendingInstance.resize()
}

async function loadOverview() {
  const res = await getStatsOverview()
  Object.assign(overview, res.data || {})
  await nextTick()
  renderCharts()
}

onMounted(async () => {
  await loadOverview()
  window.addEventListener('resize', resizeCharts)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts)
  totalInstance && totalInstance.dispose()
  pendingInstance && pendingInstance.dispose()
})
</script>

<style scoped>
.stat-card {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stat-label {
  color: #909399;
  font-size: 14px;
}

.stat-value {
  font-size: 26px;
  font-weight: 600;
  color: #303133;
}

.chart-title {
  font-weight: 600;
  margin-bottom: 12px;
}

.chart-box {
  height: 320px;
}
</style>
