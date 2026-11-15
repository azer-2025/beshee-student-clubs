<template>
  <section class="hero">
    <div class="hero-content">
      <h1>用一站式社团平台，连接每一个热爱。</h1>
      <p>浏览社团、报名活动、申请加入、查看进度与履历，都在这里完成。</p>
      <div class="hero-actions">
        <router-link class="btn primary" to="/clubs">去逛社团</router-link>
        <router-link class="btn ghost" to="/activities">看活动</router-link>
      </div>
      <div class="hero-stats">
        <div>
          <strong>3</strong>
          <span>角色覆盖</span>
        </div>
        <div>
          <strong>全流程</strong>
          <span>社团生命周期</span>
        </div>
        <div>
          <strong>可追踪</strong>
          <span>申请进度与履历</span>
        </div>
      </div>
    </div>
    <div class="hero-card">
      <div class="card-title">本周推荐</div>
      <div class="card-body">
        <div class="tag">社团</div>
        <h3>学术科技类 · 机器人社</h3>
        <p>欢迎有兴趣的同学报名，体验项目实践与赛事训练。</p>
        <div class="divider"></div>
        <div class="tag accent">活动</div>
        <h3>社团嘉年华</h3>
        <p>集章打卡、现场互动、学长学姐分享。</p>
      </div>
    </div>
  </section>

  <section class="section">
    <div class="section-title">
      <h2>校园资讯与轮播</h2>
      <p>最新通知公告与校园风采，快速了解社团动态。</p>
    </div>
    <div class="media-grid">
      <div class="carousel-wrap">
        <div v-if="carouselList.length === 0" class="empty-state">暂无轮播图</div>
        <div v-else class="carousel" @mouseenter="stopAuto" @mouseleave="startAuto">
          <div class="carousel-track" :style="{ transform: `translateX(-${currentIndex * 100}%)` }">
            <div v-for="item in carouselList" :key="item.carouselId" class="carousel-slide">
              <img :src="resolveAsset(item.imageUrl)" :alt="item.title || '轮播图'" />
              <div class="carousel-caption">
                <h3>{{ item.title || '校园风采' }}</h3>
                <a v-if="item.linkUrl" class="btn ghost" :href="item.linkUrl" target="_blank" rel="noopener">查看详情</a>
              </div>
            </div>
          </div>
          <div class="carousel-dots">
            <button
              v-for="(item, index) in carouselList"
              :key="item.carouselId"
              class="dot"
              :class="{ active: index === currentIndex }"
              @click="setCurrent(index)"
            ></button>
          </div>
        </div>
      </div>

      <div class="notice-wrap">
        <div class="notice-list">
          <button
            v-for="item in notices"
            :key="item.noticeId"
            class="notice-item"
            :class="{ active: selectedNotice && selectedNotice.noticeId === item.noticeId }"
            @click="selectNotice(item)"
          >
            <div>
              <h4>{{ item.noticeTitle }}</h4>
              <span class="muted">{{ formatDate(item.createTime) }}</span>
            </div>
            <span class="pill">{{ item.noticeType === '1' ? '通知' : '公告' }}</span>
          </button>
        </div>
        <div class="notice-detail card">
          <h3>{{ selectedNotice?.noticeTitle || '暂无公告' }}</h3>
          <div v-if="selectedNotice" class="muted">{{ formatDate(selectedNotice.createTime) }}</div>
          <div v-if="selectedNotice" v-html="selectedNotice.noticeContent || '暂无内容'"></div>
          <div v-else class="empty-state">暂无公告内容</div>
        </div>
      </div>
    </div>
  </section>

  <section class="section">
    <div class="section-title">
      <h2>你能在这里完成的事</h2>
      <p>学生端聚焦“加入社团”和“参与活动”。</p>
    </div>
    <div class="grid">
      <div class="card">
        <h3>社团浏览与检索</h3>
        <p>按名称与分类快速定位社团，查看简介、章程与荣誉墙。</p>
      </div>
      <div class="card">
        <h3>加入申请与进度</h3>
        <p>在线提交入社申请，实时查看审核状态。</p>
      </div>
      <div class="card">
        <h3>活动报名与签到</h3>
        <p>在线报名、签到，报名进度清晰可见。</p>
      </div>
      <div class="card">
        <h3>个人社团履历</h3>
        <p>留存入社时间、职务、荣誉与退社记录。</p>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import { listCarousels, listNotices } from '../api/content'
import { resolveAsset } from '../utils/url'
import { formatDate } from '../utils/format'

const carouselList = ref([])
const notices = ref([])
const selectedNotice = ref(null)
const currentIndex = ref(0)
let timer = null

function startAuto() {
  stopAuto()
  if (carouselList.value.length <= 1) return
  timer = setInterval(() => {
    currentIndex.value = (currentIndex.value + 1) % carouselList.value.length
  }, 4000)
}

function stopAuto() {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

function setCurrent(index) {
  currentIndex.value = index
  startAuto()
}

function selectNotice(item) {
  selectedNotice.value = item
}

async function loadCarousel() {
  const res = await listCarousels()
  carouselList.value = res.data || []
  if (carouselList.value.length > 0) {
    currentIndex.value = 0
  }
  startAuto()
}

async function loadNotices() {
  const res = await listNotices({ pageNum: 1, pageSize: 6 })
  notices.value = res.rows || []
  selectedNotice.value = notices.value[0] || null
}

onMounted(() => {
  loadCarousel().catch(() => {})
  loadNotices().catch(() => {})
})

onBeforeUnmount(() => {
  stopAuto()
})
</script>
