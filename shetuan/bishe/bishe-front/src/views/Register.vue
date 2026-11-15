<template>
  <section class="auth">
    <div class="auth-card">
      <h2>注册学生账号</h2>
      <p class="muted">创建账号后即可申请加入社团。</p>
      <form @submit.prevent="handleRegister">
        <div class="input-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" placeholder="请输入用户名" />
        </div>
        <div class="input-group">
          <label>密码</label>
          <input v-model="form.password" type="password" placeholder="请输入密码" />
        </div>
        <div class="input-group">
          <label>确认密码</label>
          <input v-model="form.confirm" type="password" placeholder="请再次输入密码" />
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
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>
      <div class="auth-footer">
        <span>已有账号？</span>
        <router-link to="/login">去登录</router-link>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getCaptcha, register } from '../api/auth'

const router = useRouter()
const loading = ref(false)
const captchaImg = ref('')
const captchaEnabled = ref(true)

const form = reactive({
  username: '',
  password: '',
  confirm: '',
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

async function handleRegister() {
  if (!form.username || !form.password) return
  if (form.password !== form.confirm) {
    alert('两次密码不一致')
    return
  }
  loading.value = true
  try {
    await register({
      username: form.username,
      password: form.password,
      code: form.code,
      uuid: form.uuid
    })
    alert('注册成功，请登录')
    router.push('/login')
  } catch (err) {
    alert(err.message || '注册失败')
    await loadCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(loadCaptcha)
</script>
