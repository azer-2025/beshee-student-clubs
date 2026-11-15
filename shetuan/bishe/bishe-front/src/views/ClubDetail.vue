<template>
  <section class="section">
    <div v-if="loading" class="empty-state">加载中...</div>
    <div v-else-if="!club" class="empty-state">未找到社团信息</div>
    <div v-else class="detail">
      <div class="detail-header">
        <div class="detail-title">
          <img v-if="club.clubLogo" :src="resolveAsset(club.clubLogo)" alt="logo" />
          <div>
            <h2>{{ club.clubName }}</h2>
            <p class="muted">分类ID：{{ club.categoryId }} · 负责人ID：{{ club.leaderUserId }}</p>
          </div>
        </div>
        <button class="btn primary" @click="handleApply">申请加入</button>
      </div>

      <div class="detail-grid">
        <div class="detail-card">
          <h3>社团简介</h3>
          <p>{{ club.clubIntro || '暂无简介' }}</p>
        </div>
        <div class="detail-card">
          <h3>社团章程</h3>
          <div v-html="club.charterContent || '暂无章程'"></div>
        </div>
        <div class="detail-card">
          <h3>荣誉墙</h3>
          <div v-html="club.honorWall || '暂无荣誉'"></div>
        </div>
        <div class="detail-card">
          <h3>纳新海报</h3>
          <img v-if="club.recruitPoster" :src="resolveAsset(club.recruitPoster)" alt="poster" class="poster" />
          <p v-else class="muted">暂无海报</p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getClubDetail, applyJoinClub } from '../api/club'
import { resolveAsset } from '../utils/url'
import { useAuth } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuth()

const loading = ref(false)
const club = ref(null)

async function fetchDetail() {
  loading.value = true
  try {
    const res = await getClubDetail(route.params.id)
    club.value = res.data || null
  } catch (err) {
    club.value = null
  } finally {
    loading.value = false
  }
}

async function handleApply() {
  if (!auth.token) {
    router.push({ name: 'login', query: { redirect: route.fullPath } })
    return
  }
  if (!club.value) return
  try {
    await applyJoinClub({ clubId: club.value.clubId, remark: '学生端申请加入' })
    alert('已提交申请，请在“我的”中查看进度')
  } catch (err) {
    alert(err.message || '申请失败')
  }
}

onMounted(fetchDetail)
</script>
