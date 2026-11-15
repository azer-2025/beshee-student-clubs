import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '../utils/auth'

const Home = () => import('../views/Home.vue')
const Clubs = () => import('../views/Clubs.vue')
const ClubDetail = () => import('../views/ClubDetail.vue')
const ClubApply = () => import('../views/ClubApply.vue')
const Activities = () => import('../views/Activities.vue')
const ActivityDetail = () => import('../views/ActivityDetail.vue')
const Login = () => import('../views/Login.vue')
const Register = () => import('../views/Register.vue')
const Me = () => import('../views/Me.vue')
const NotFound = () => import('../views/NotFound.vue')

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/clubs', name: 'clubs', component: Clubs },
    { path: '/clubs/apply', name: 'club-apply', component: ClubApply, meta: { requiresAuth: true } },
    { path: '/clubs/:id', name: 'club-detail', component: ClubDetail, props: true },
    { path: '/activities', name: 'activities', component: Activities },
    { path: '/activities/:id', name: 'activity-detail', component: ActivityDetail, props: true },
    { path: '/login', name: 'login', component: Login },
    { path: '/register', name: 'register', component: Register },
    { path: '/me', name: 'me', component: Me, meta: { requiresAuth: true } },
    { path: '/:pathMatch(.*)*', name: 'not-found', component: NotFound }
  ],
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to) => {
  if (!to.meta.requiresAuth) return true
  const token = getToken()
  if (token) return true
  return { name: 'login', query: { redirect: to.fullPath } }
})

export default router
