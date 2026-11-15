<template>
  <section class="section">
    <div class="section-title">
      <h2>社团列表</h2>
      <p>发现你感兴趣的社团，查看详情后提交入社申请。</p>
    </div>

    <div class="filters">
      <div class="input-group">
        <label>社团名称</label>
        <input v-model="filters.clubName" type="text" placeholder="请输入名称" />
      </div>
      <div class="input-group">
        <label>分类</label>
        <select v-model="filters.categoryId">
          <option value="">全部</option>
          <option v-for="item in categories" :key="item.categoryId" :value="item.categoryId">
            {{ item.categoryName }}
          </option>
        </select>
      </div>
      <button class="btn primary" @click="handleSearch">搜索</button>
    </div>

    <div v-if="loading" class="empty-state">加载中...</div>
    <div v-else-if="clubs.length === 0" class="empty-state">暂无社团数据</div>
    <div v-else class="grid">
      <div v-for="club in clubs" :key="club.clubId" class="card club-card">
        <div class="card-head">
          <img v-if="club.clubLogo" :src="resolveAsset(club.clubLogo)" alt="logo" />
          <div>
            <h3>{{ club.clubName }}</h3>
            <p class="muted">分类：{{ categoryMap[club.categoryId] || club.categoryId }}</p>
          </div>
        </div>
        <p class="line-clamp">{{ club.clubIntro || '暂无简介' }}</p>
        <div class="card-actions">
          <router-link class="btn ghost" :to="`/clubs/${club.clubId}`">查看详情</router-link>
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
import { computed, onMounted, ref } from 'vue'
import { listClubs, listCategories } from '../api/club'
import { resolveAsset } from '../utils/url'

const clubs = ref([])
const categories = ref([])
const loading = ref(false)
const total = ref(0)
const pageNum = ref(1)
const pageSize = 12

const filters = ref({
  clubName: '',
  categoryId: ''
})

const categoryMap = computed(() => {
  return Object.fromEntries(categories.value.map(item => [item.categoryId, item.categoryName]))
})

async function fetchCategories() {
  try {
    const res = await listCategories()
    categories.value = res.data || []
  } catch (err) {
    categories.value = []
  }
}

async function fetchClubs() {
  loading.value = true
  try {
    const res = await listClubs({
      pageNum: pageNum.value,
      pageSize,
      clubName: filters.value.clubName || undefined,
      categoryId: filters.value.categoryId || undefined
    })
    clubs.value = res.rows || []
    total.value = res.total || 0
  } catch (err) {
    clubs.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageNum.value = 1
  fetchClubs()
}

function changePage(delta) {
  pageNum.value += delta
  fetchClubs()
}

onMounted(() => {
  fetchCategories()
  fetchClubs()
})
</script>
