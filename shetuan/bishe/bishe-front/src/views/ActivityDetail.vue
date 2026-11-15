<template>
  <section class="section">
    <div v-if="loading" class="empty-state">加载中...</div>
    <div v-else-if="!activity" class="empty-state">未找到活动信息</div>
    <div v-else class="detail">
      <div class="detail-header">
        <div class="detail-title">
          <div>
            <h2>{{ activity.title }}</h2>
            <p class="muted">社团ID：{{ activity.clubId }} · 地点：{{ activity.location || '待定' }}</p>
          </div>
        </div>
        <button class="btn primary" @click="handleSignup">报名参加</button>
      </div>

      <div class="detail-grid">
        <div class="detail-card">
          <h3>活动时间</h3>
          <p>开始：{{ formatDate(activity.startTime) }}</p>
          <p>结束：{{ formatDate(activity.endTime) }}</p>
          <p>人数上限：{{ activity.capacity || 0 }}</p>
        </div>
        <div class="detail-card">
          <h3>活动内容</h3>
          <div v-html="activity.content || '暂无内容'"></div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getActivityDetail, signupActivity } from '../api/activity'
import { formatDate } from '../utils/format'
import { useAuth } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuth()

const loading = ref(false)
const activity = ref(null)

async function fetchDetail() {
  loading.value = true
  try {
    const res = await getActivityDetail(route.params.id)
    activity.value = res.data || null
  } catch (err) {
    activity.value = null
  } finally {
    loading.value = false
  }
}

async function handleSignup() {
  if (!auth.token) {
    router.push({ name: 'login', query: { redirect: route.fullPath } })
    return
  }
  if (!activity.value) return
  try {
    await signupActivity({ activityId: activity.value.activityId, status: '0' })
    alert('报名成功，请在“我的”中查看进度')
  } catch (err) {
    alert(err.message || '报名失败')
  }
}

onMounted(fetchDetail)
</script>
