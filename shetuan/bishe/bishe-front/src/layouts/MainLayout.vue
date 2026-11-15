<template>
  <div class="layout">
    <header class="site-header">
      <div class="brand">
        <div class="brand-mark">社团+</div>
        <div class="brand-text">
          <span class="brand-title">大学生社团管理系统</span>
          <span class="brand-sub">学生端</span>
        </div>
      </div>
      <nav class="nav-links">
        <router-link to="/">首页</router-link>
        <router-link to="/clubs">社团</router-link>
        <router-link to="/activities">活动</router-link>
        <router-link to="/clubs/apply">成立申请</router-link>
        <router-link to="/me">我的</router-link>
      </nav>
      <div class="nav-actions">
        <template v-if="auth.token">
          <span class="user-pill">{{ auth.user?.nickName || auth.user?.userName || '已登录' }}</span>
          <button class="btn ghost" @click="handleLogout">退出</button>
        </template>
        <template v-else>
          <router-link class="btn ghost" to="/login">登录</router-link>
          <router-link class="btn primary" to="/register">注册</router-link>
        </template>
      </div>
    </header>

    <main class="site-main">
      <router-view />
    </main>

    <footer class="site-footer">
      <div>社团管理系统 · 学生端</div>
      <div>© 2026 校园社团服务平台</div>
    </footer>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useAuth, fetchUser, signOut } from '../stores/auth'

const auth = useAuth()

async function handleLogout() {
  await signOut()
}

onMounted(() => {
  if (auth.token && !auth.user) {
    fetchUser().catch(() => {})
  }
})
</script>
