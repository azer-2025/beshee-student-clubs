<template>
  <section class="section">
    <div class="section-title">
      <h2>社团成立申请</h2>
      <p>填写社团基本信息与章程，提交后进入指导老师审核与管理员终审流程。</p>
    </div>

    <div class="card">
      <form @submit.prevent="handleSubmit" class="form-grid">
        <div class="input-group">
          <label>社团名称</label>
          <input v-model="form.clubName" type="text" placeholder="请输入社团名称" required />
        </div>
        <div class="input-group">
          <label>社团分类</label>
          <select v-model="form.categoryId" required>
            <option value="">请选择分类</option>
            <option v-for="item in categories" :key="item.categoryId" :value="item.categoryId">
              {{ item.categoryName }}
            </option>
          </select>
        </div>
        <div class="input-group">
          <label>社团Logo（可选）</label>
          <div class="image-uploader" @click="$refs.logoInput.click()">
            <img v-if="form.clubLogo" :src="resolveAsset(form.clubLogo)" alt="logo" />
            <div v-else class="uploader-placeholder">点击上传Logo</div>
          </div>
          <input ref="logoInput" type="file" accept="image/*" style="display: none" @change="e => onFileChange(e, 'clubLogo')" />
        </div>
        <div class="input-group">
          <label>纳新海报（可选）</label>
          <div class="image-uploader" @click="$refs.posterInput.click()">
            <img v-if="form.recruitPoster" :src="resolveAsset(form.recruitPoster)" alt="poster" />
            <div v-else class="uploader-placeholder">点击上传海报</div>
          </div>
          <input ref="posterInput" type="file" accept="image/*" style="display: none" @change="e => onFileChange(e, 'recruitPoster')" />
        </div>
        <div class="input-group full">
          <label>社团简介</label>
          <textarea v-model="form.clubIntro" rows="4" placeholder="请输入社团简介"></textarea>
        </div>
        <div class="input-group full">
          <label>社团章程</label>
          <textarea v-model="form.charterContent" rows="6" placeholder="请输入社团章程"></textarea>
        </div>
        <div class="input-group full">
          <label>荣誉墙（可选）</label>
          <textarea v-model="form.honorWall" rows="4" placeholder="可填写主要荣誉或获奖情况"></textarea>
        </div>
        <div class="input-group">
          <label>指导老师用户ID</label>
          <input v-model="form.teacherUserId" type="text" placeholder="请输入指导老师用户ID" />
        </div>
        <div class="input-group">
          <label>负责人用户ID（可选）</label>
          <input v-model="form.leaderUserId" type="text" placeholder="默认当前登录用户" />
        </div>
        <div class="form-actions full">
          <button class="btn primary" type="submit" :disabled="submitting">
            {{ submitting ? '提交中...' : '提交申请' }}
          </button>
          <router-link class="btn ghost" to="/me">查看进度</router-link>
        </div>
      </form>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { applyClub, listCategories } from '../api/club'
import { request } from '../utils/request'
import { resolveAsset } from '../utils/url'

const logoInput = ref(null)
const posterInput = ref(null)
const categories = ref([])
const submitting = ref(false)
const form = reactive({
  clubName: '',
  categoryId: '',
  clubLogo: '',
  recruitPoster: '',
  clubIntro: '',
  charterContent: '',
  honorWall: '',
  teacherUserId: '',
  leaderUserId: ''
})

async function onFileChange(e, field) {
  const file = e.target.files[0]
  if (!file) return
  
  const formData = new FormData()
  formData.append('file', file)
  
  try {
    const res = await request('/common/upload', {
      method: 'POST',
      body: formData,
      json: false
    })
    form[field] = res.fileName
  } catch (err) {
    alert(err.message || '图片上传失败')
  }
}

async function loadCategories() {
  try {
    const res = await listCategories()
    categories.value = res.data || []
  } catch (err) {
    categories.value = []
  }
}

async function handleSubmit() {
  submitting.value = true
  try {
    const payload = { ...form }
    if (!payload.teacherUserId) delete payload.teacherUserId
    if (!payload.leaderUserId) delete payload.leaderUserId
    await applyClub(payload)
    alert('已提交成立申请，请在“我的”查看审核进度')
  } catch (err) {
    alert(err.message || '提交失败')
  } finally {
    submitting.value = false
  }
}

onMounted(loadCategories)
</script>

<style scoped>
.image-uploader {
  height: 120px;
  border: 2px dashed var(--line);
  border-radius: 12px;
  cursor: pointer;
  display: grid;
  place-items: center;
  overflow: hidden;
  background: #fff;
  transition: all 0.3s ease;
}

.image-uploader:hover {
  border-color: var(--accent);
}

.image-uploader img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.uploader-placeholder {
  color: var(--muted);
  font-size: 13px;
}
</style>
