<template>
  <section class="section">
    <div class="section-title">
      <h2>活动列表</h2>
      <p>查看社团活动详情，在线报名参与。</p>
    </div>

    <div class="filters">
      <div class="input-group">
        <label>活动标题</label>
        <input v-model="filters.title" type="text" placeholder="请输入标题" />
      </div>
      <div class="input-group">
        <label>社团ID</label>
        <input v-model="filters.clubId" type="text" placeholder="请输入社团ID" />
      </div>
      <button class="btn primary" @click="handleSearch">搜索</button>
    </div>

    <div v-if="loading" class="empty-state">加载中...</div>
    <div v-else-if="activities.length === 0" class="empty-state">暂无活动数据</div>
    <div v-else class="grid">
      <div v-for="item in activities" :key="item.activityId" class="card activity-card">
        <div class="card-head">
          <div>
            <h3>{{ item.title }}</h3>
            <p class="muted">社团ID：{{ item.clubId }} · 地点：{{ item.location || '待定' }}</p>
          </div>
          <span class="pill">{{ dictLabel(activityStatus, item.status) }}</span>
        </div>
        <p class="line-clamp">{{ item.content ? stripHtml(item.content) : '暂无内容' }}</p>
        <div class="card-meta">
          <span>开始：{{ formatDate(item.startTime) }}</span>
          <span>结束：{{ formatDate(item.endTime) }}</span>
        </div>
        <div class="card-actions">
          <router-link class="btn ghost" :to="`/activities/${item.activityId}`">查看详情</router-link>
        </div>
      </div>
    </div>

    <div class="pager" v-if="total > pageSize">
      <button class="btn ghost" :disabled="pageNum === 1" @click="changePage(-1)">上一页</button>
      <span>第 {{ pageNum }} 页</span>
      <button class="btn ghost" :disabled="pageNum * pageSize >= total" @click="changePage(1)">下一页</button>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { listActivities } from '../api/activity'
import { getDict, dictLabel } from '../utils/dict'
import { formatDate } from '../utils/format'

const activities = ref([])
const activityStatus = ref([])
const loading = ref(false)
const total = ref(0)
const pageNum = ref(1)
const pageSize = 12

const filters = ref({
  title: '',
  clubId: ''
})

function stripHtml(html) {
  return html.replace(/<[^>]*>/g, '')
}

async function fetchActivities() {
  loading.value = true
  try {
    const res = await listActivities({
      pageNum: pageNum.value,
      pageSize,
      title: filters.value.title || undefined,
      clubId: filters.value.clubId || undefined
    })
    activities.value = res.rows || []
    total.value = res.total || 0
  } catch (err) {
    activities.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageNum.value = 1
  fetchActivities()
}

function changePage(delta) {
  pageNum.value += delta
  fetchActivities()
}

onMounted(async () => {
  activityStatus.value = await getDict('activity_status')
  fetchActivities()
})
</script>
