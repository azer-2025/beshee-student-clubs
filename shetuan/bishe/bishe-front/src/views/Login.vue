<template>
  <section class="auth">
    <div class="auth-card">
      <h2>登录学生端</h2>
      <p class="muted">使用你的账号进入社团系统。</p>
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" placeholder="请输入用户名" />
        </div>
        <div class="input-group">
          <label>密码</label>
          <input v-model="form.password" type="password" placeholder="请输入密码" />
        </div>
        <div class="captcha-row" v-if="captchaEnabled">
          <div class="input-group">
            <label>验证码</label>
            <input v-model="form.code" type="text" placeholder="请输入验证码" />
          </div>
          <div class="captcha-box" @click="loadCaptcha">
            <img v-if="captchaImg" :src="captchaImg" alt="captcha" />
            <span v-else>获取验证码</span>
          </div>
        </div>
        <button class="btn primary full" type="submit" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>
      <div class="auth-footer">
        <span>没有账号？</span>
        <router-link to="/register">去注册</router-link>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCaptcha } from '../api/auth'
import { signIn } from '../stores/auth'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const captchaImg = ref('')
const captchaEnabled = ref(true)

const form = reactive({
  username: '',
  password: '',
  code: '',
  uuid: ''
})

async function loadCaptcha() {
  try {
    const res = await getCaptcha()
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      captchaImg.value = `data:image/gif;base64,${res.img}`
      form.uuid = res.uuid
    } else {
      captchaImg.value = ''
      form.code = ''
      form.uuid = ''
    }
  } catch (err) {
    captchaImg.value = ''
    captchaEnabled.value = true
  }
}

async function handleLogin() {
  if (!form.username || !form.password) return
  loading.value = true
  try {
    await signIn({
      username: form.username,
      password: form.password,
      code: form.code,
      uuid: form.uuid
    })
    const redirect = route.query.redirect || '/'
    router.replace(redirect)
  } catch (err) {
    alert(err.message || '登录失败')
    await loadCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(loadCaptcha)
</script>
