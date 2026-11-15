<template>
  <section class="section">
    <div class="section-title">
      <h2>我的中心</h2>
      <p>个人信息维护、申请进度与履历一站管理。</p>
    </div>

    <div class="me-grid">
      <div class="card profile-card">
        <div class="card-header">
          <h3>个人信息</h3>
          <p class="subtitle">维护你的基本资料</p>
        </div>
        <div class="avatar-upload">
          <div class="avatar-preview" @click="$refs.fileInput.click()">
            <img v-if="avatarUrl" :src="avatarUrl" alt="avatar" />
            <div v-else class="avatar-placeholder">
              <span>点击上传</span>
            </div>
            <div class="avatar-overlay">更换头像</div>
          </div>
          <input
            ref="fileInput"
            type="file"
            accept="image/*"
            style="display: none"
            @change="onFileChange"
          />
        </div>
        <form @submit.prevent="handleProfileSave" class="profile-form">
          <div class="form-row">
            <div class="input-group">
              <label>昵称</label>
              <input v-model="profile.nickName" type="text" placeholder="请输入昵称" />
            </div>
            <div class="input-group">
              <label>手机号</label>
              <input v-model="profile.phonenumber" type="text" placeholder="请输入手机号" />
            </div>
          </div>
          <div class="form-row">
            <div class="input-group">
              <label>邮箱</label>
              <input v-model="profile.email" type="email" placeholder="请输入邮箱" />
            </div>
            <div class="input-group">
              <label>性别</label>
              <select v-model="profile.sex">
                <option v-for="item in sexOptions" :key="item.dictValue" :value="item.dictValue">
                  {{ item.dictLabel }}
                </option>
              </select>
            </div>
          </div>
          <button class="btn primary full" type="submit">保存修改</button>
        </form>
      </div>

      <div class="card">
        <h3>社团成立申请进度</h3>
        <div v-if="clubApplyList.length === 0" class="empty-state">暂无成立申请</div>
        <div v-else class="list">
          <div v-for="item in clubApplyList" :key="item.clubId" class="list-item">
            <div>
              <strong>{{ item.clubName || ('社团ID：' + item.clubId) }}</strong>
              <p class="muted">申请时间：{{ formatDate(item.applyTime) }}</p>
            </div>
            <span class="pill">{{ dictLabel(clubApplyStatus, item.applyStatus) }}</span>
          </div>
        </div>
      </div>

      <div class="card">
        <h3>入社申请进度</h3>
        <div v-if="clubLoading" class="empty-state">加载中...</div>
        <div v-else-if="clubApps.length === 0" class="empty-state">暂无入社申请</div>
        <div v-else class="list">
          <div v-for="item in clubApps" :key="item.memberId" class="list-item">
            <div>
              <strong>{{ item.clubName || ('社团ID：' + item.clubId) }}</strong>
              <p class="muted">申请时间：{{ formatDate(item.applyTime) }}</p>
            </div>
            <span class="pill">{{ dictLabel(memberStatus, item.status) }}</span>
          </div>
        </div>
      </div>

      <div class="card">
        <h3>活动报名进度</h3>
        <div v-if="signupLoading" class="empty-state">加载中...</div>
        <div v-else-if="signups.length === 0" class="empty-state">暂无活动报名</div>
        <div v-else class="list">
          <div v-for="item in signups" :key="item.signupId" class="list-item">
            <div>
              <strong>{{ item.activityTitle || ('活动ID：' + item.activityId) }}</strong>
              <p class="muted">报名时间：{{ formatDate(item.signupTime) }}</p>
            </div>
            <div class="pill-group">
              <span class="pill">{{ dictLabel(signupStatus, item.status) }}</span>
              <span class="pill ghost">{{ dictLabel(checkinStatus, item.checkinStatus) }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <h3>社团履历</h3>
        <div v-if="clubLoading" class="empty-state">加载中...</div>
        <div v-else-if="clubApps.length === 0" class="empty-state">暂无履历</div>
        <div v-else class="list">
          <div v-for="item in clubApps" :key="item.memberId" class="list-item">
            <div>
              <strong>{{ item.clubName || ('社团ID：' + item.clubId) }}</strong>
              <p class="muted">入社：{{ formatDate(item.joinTime) || '未入社' }}</p>
              <p class="muted">职务：{{ item.positionName || '暂无' }}</p>
            </div>
            <span class="pill">{{ item.status === '3' ? '已退社' : '在社' }}</span>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref, computed } from 'vue'
import { request } from '../utils/request'
import { myClubApplications, myClubApplyList } from '../api/club'
import { mySignups } from '../api/activity'
import { getDict, dictLabel } from '../utils/dict'
import { formatDate } from '../utils/format'
import { resolveAsset } from '../utils/url'
import { useAuth } from '../stores/auth'

const auth = useAuth()
const fileInput = ref(null)
const profile = reactive({
  nickName: '',
  phonenumber: '',
  email: '',
  sex: '0',
  avatar: ''
})

const avatarUrl = computed(() => resolveAsset(profile.avatar))

const sexOptions = ref([])
const clubApps = ref([])
const clubApplyList = ref([])
const signups = ref([])
const clubApplyStatus = ref([])
const memberStatus = ref([])
const signupStatus = ref([])
const checkinStatus = ref([])
const clubLoading = ref(false)
const signupLoading = ref(false)

async function loadProfile() {
  const res = await request('/system/user/profile', { method: 'get' })
  const user = res.data || res.user || {}
  profile.nickName = user.nickName || ''
  profile.phonenumber = user.phonenumber || ''
  profile.email = user.email || ''
  profile.sex = user.sex || '0'
  profile.avatar = user.avatar || ''
}

async function onFileChange(e) {
  const file = e.target.files[0]
  if (!file) return
  
  const formData = new FormData()
  formData.append('avatarfile', file)
  
  try {
    const res = await request('/system/user/profile/avatar', {
      method: 'POST',
      body: formData,
      json: false // Don't set content-type to json
    })
    profile.avatar = res.imgUrl
    alert('头像上传成功')
  } catch (err) {
    alert(err.message || '头像上传失败')
  }
}

async function handleProfileSave() {
  try {
    await request('/system/user/profile', {
      method: 'put',
      body: JSON.stringify(profile)
    })
    alert('保存成功')
  } catch (err) {
    alert(err.message || '保存失败')
  }
}

async function loadMyClubs() {
  clubLoading.value = true
  try {
    const res = await myClubApplications({ pageNum: 1, pageSize: 50 })
    clubApps.value = res.rows || []
  } catch (err) {
    clubApps.value = []
  } finally {
    clubLoading.value = false
  }
}

async function loadMyClubApplies() {
  try {
    const res = await myClubApplyList({ pageNum: 1, pageSize: 20 })
    clubApplyList.value = res.rows || []
  } catch (err) {
    clubApplyList.value = []
  }
}

async function loadMySignups() {
  signupLoading.value = true
  try {
    const res = await mySignups({ pageNum: 1, pageSize: 50 })
    signups.value = res.rows || []
  } catch (err) {
    signups.value = []
  } finally {
    signupLoading.value = false
  }
}

onMounted(async () => {
  if (!auth.user) {
    try {
      await request('/getInfo', { method: 'get' })
    } catch (err) {
      // ignore
    }
  }
  sexOptions.value = await getDict('sys_user_sex')
  clubApplyStatus.value = await getDict('club_apply_status')
  memberStatus.value = await getDict('club_member_status')
  signupStatus.value = await getDict('activity_signup_status')
  checkinStatus.value = await getDict('activity_checkin_status')
  await loadProfile()
  await loadMyClubs()
  await loadMySignups()
  await loadMyClubApplies()
})
</script>

<style scoped>
.profile-card {
  grid-column: 1 / -1;
  background: #fff;
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

@media (min-width: 1024px) {
  .profile-card {
    grid-column: span 2;
    grid-template-columns: 200px 1fr;
    align-items: start;
  }
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px 0;
}

.avatar-preview {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  position: relative;
  cursor: pointer;
  background: var(--bg);
  border: 2px solid var(--line);
  transition: all 0.3s ease;
}

.avatar-preview:hover {
  border-color: var(--accent);
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  color: var(--muted);
  font-size: 14px;
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  color: #fff;
  display: grid;
  place-items: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  font-size: 13px;
  font-weight: 600;
}

.avatar-preview:hover .avatar-overlay {
  opacity: 1;
}

.card-header {
  grid-column: 1 / -1;
}

.card-header h3 {
  margin: 0 0 4px 0;
}

.subtitle {
  color: var(--muted);
  font-size: 13px;
  margin: 0;
}

.profile-form {
  display: grid;
  gap: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}
</style>
