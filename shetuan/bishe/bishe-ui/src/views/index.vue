<template>
  <div class="app-container home">
    <!-- Top Statistics Cards -->
    <el-row :gutter="20" class="mb-4">
      <el-col :xs="24" :sm="12" :md="6" class="stats-card-col">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon-wrapper icon-blue">
            <el-icon><user-filled /></el-icon>
          </div>
          <div class="stats-content">
            <div class="stats-title">社团总数</div>
            <div class="stats-number">{{ stats.clubTotal || 0 }}</div>
            <div class="stats-change text-muted">
              <span v-if="stats.clubPending > 0" class="text-warning">待审核: {{ stats.clubPending }}</span>
              <span v-else>运行中</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" class="stats-card-col">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon-wrapper icon-green">
            <el-icon><list /></el-icon>
          </div>
          <div class="stats-content">
            <div class="stats-title">活动总数</div>
            <div class="stats-number">{{ stats.activityTotal || 0 }}</div>
            <div class="stats-change text-muted">
              累计发布
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" class="stats-card-col">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon-wrapper icon-orange">
            <el-icon><user /></el-icon>
          </div>
          <div class="stats-content">
            <div class="stats-title">注册成员</div>
            <div class="stats-number">{{ stats.memberTotal || 0 }}</div>
            <div class="stats-change text-muted">
              活跃成员
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" class="stats-card-col">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon-wrapper icon-purple">
            <el-icon><bell /></el-icon>
          </div>
          <div class="stats-content">
            <div class="stats-title">待办审批</div>
            <div class="stats-number">{{ (stats.clubPending || 0) + (stats.activityPending || 0) + (stats.fundingPending || 0) + (stats.venuePending || 0) }}</div>
            <div class="stats-change text-warning">
              需处理事项
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Charts Section -->
    <el-row :gutter="20" class="mb-4">
      <el-col :xs="24" :lg="16">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="clearfix">
              <span>热门社团排行 (Top 5)</span>
            </div>
          </template>
          <div ref="barChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="clearfix">
              <span>社团分类分布</span>
            </div>
          </template>
          <div ref="pieChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="clearfix">
              <span>近期活动参与趋势</span>
            </div>
          </template>
          <div ref="lineChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import * as echarts from 'echarts'
import { onMounted, onUnmounted, ref, nextTick, reactive } from 'vue'
import { getStatsOverview, getStatsChart } from '@/api/stats/overview'

const barChartRef = ref(null)
const pieChartRef = ref(null)
const lineChartRef = ref(null)
let barChart = null
let pieChart = null
let lineChart = null

const stats = ref({})

const initCharts = (chartData) => {
  if (!chartData) return

  // Bar Chart
  if (barChartRef.value) {
    barChart = echarts.init(barChartRef.value)
    const topClubs = chartData.topClubs || []
    barChart.setOption({
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'value',
        boundaryGap: [0, 0.01]
      },
      yAxis: {
        type: 'category',
        data: topClubs.map(item => item.name)
      },
      series: [
        {
          name: '成员数',
          type: 'bar',
          data: topClubs.map(item => item.value),
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          },
          emphasis: {
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#2378f7' },
                { offset: 0.7, color: '#2378f7' },
                { offset: 1, color: '#83bff6' }
              ])
            }
          },
        }
      ]
    })
  }

  // Pie Chart
  if (pieChartRef.value) {
    pieChart = echarts.init(pieChartRef.value)
    const categories = chartData.categories || []
    pieChart.setOption({
      tooltip: {
        trigger: 'item'
      },
      legend: {
        top: '5%',
        left: 'center'
      },
      series: [
        {
          name: '社团分类',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 20,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: categories
        }
      ]
    })
  }

  // Line Chart
  if (lineChartRef.value) {
    lineChart = echarts.init(lineChartRef.value)
    const trends = chartData.trends || []
    lineChart.setOption({
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['活动发布数', '参与人数']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: trends.map(item => item.month)
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '活动发布数',
          type: 'line',
          stack: 'Total',
          data: trends.map(item => item.activityCount),
          smooth: true
        },
        {
          name: '参与人数',
          type: 'line',
          stack: 'Total',
          data: trends.map(item => item.signupCount),
          smooth: true,
          areaStyle: {}
        }
      ]
    })
  }
}

const resizeCharts = () => {
  barChart?.resize()
  pieChart?.resize()
  lineChart?.resize()
}

onMounted(() => {
  // Fetch Stats
  getStatsOverview().then(res => {
    stats.value = res.data
  })

  // Fetch Charts
  getStatsChart().then(res => {
    nextTick(() => {
      initCharts(res.data)
      window.addEventListener('resize', resizeCharts)
    })
  })
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeCharts)
  barChart?.dispose()
  pieChart?.dispose()
  lineChart?.dispose()
})
</script>

<style scoped lang="scss">
.home {
  padding: 20px;
  background-color: #f5f7f9;
  min-height: 100vh;

  .mb-4 {
    margin-bottom: 20px;
  }

  .stats-card-col {
    margin-bottom: 10px;
  }

  .stats-card {
    border: none;
    border-radius: 8px;
    
    :deep(.el-card__body) {
      display: flex;
      align-items: center;
      padding: 20px;
    }
  }

  .stats-icon-wrapper {
    width: 60px;
    height: 60px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 20px;
    font-size: 30px;
    color: #fff;

    &.icon-blue {
      background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
      box-shadow: 0 4px 10px rgba(24, 144, 255, 0.3);
    }
    &.icon-green {
      background: linear-gradient(135deg, #52c41a 0%, #95de64 100%);
      box-shadow: 0 4px 10px rgba(82, 196, 26, 0.3);
    }
    &.icon-orange {
      background: linear-gradient(135deg, #faad14 0%, #ffc53d 100%);
      box-shadow: 0 4px 10px rgba(250, 173, 20, 0.3);
    }
    &.icon-purple {
      background: linear-gradient(135deg, #722ed1 0%, #b37feb 100%);
      box-shadow: 0 4px 10px rgba(114, 46, 209, 0.3);
    }
  }

  .stats-content {
    flex: 1;

    .stats-title {
      font-size: 14px;
      color: #8c8c8c;
      margin-bottom: 8px;
    }

    .stats-number {
      font-size: 24px;
      font-weight: 700;
      color: #303133;
      margin-bottom: 4px;
    }

    .stats-change {
      font-size: 12px;
      display: flex;
      align-items: center;

      &.text-success { color: #67c23a; }
      &.text-danger { color: #f56c6c; }
      &.text-warning { color: #e6a23c; }
    }
  }

  .chart-card {
    border-radius: 8px;
    border: none;
    margin-bottom: 10px;

    :deep(.el-card__header) {
      padding: 15px 20px;
      border-bottom: 1px solid #f0f0f0;
      font-weight: 600;
      font-size: 16px;
    }
  }
}
</style>