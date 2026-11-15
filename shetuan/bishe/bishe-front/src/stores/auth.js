import { reactive } from 'vue'
import { getToken, setToken, clearToken } from '../utils/auth'
import { login, getInfo, logout } from '../api/auth'

const state = reactive({
  token: getToken(),
  user: null,
  loading: false
})

export function useAuth() {
  return state
}

export async function fetchUser() {
  if (!state.token) return null
  const res = await getInfo()
  state.user = res.user || res.data || null
  return state.user
}

export async function signIn(payload) {
  state.loading = true
  try {
    const res = await login(payload)
    if (res.token) {
      setToken(res.token)
      state.token = res.token
    }
    await fetchUser()
    return res
  } finally {
    state.loading = false
  }
}

export async function signOut() {
  try {
    if (state.token) {
      await logout()
    }
  } finally {
    clearToken()
    state.token = null
    state.user = null
  }
}
