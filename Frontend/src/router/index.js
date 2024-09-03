import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import TaskDetail from '@/components/TaskDetail.vue'
import EditPopup from '@/components/EditPopup.vue'
import EditStatusPopup from '@/components/EditStatusPopup.vue'

import Notfound from '@/views/Notfound.vue'
import StatusView from '@/views/StatusView.vue'
import LoginView from '@/views/LoginView.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login',
      name: 'Login',
      component: LoginView,
    },
    {
      path: '/',
      name: 'RootPath',
      redirect: { name: 'Login' }
    },
    { path: '/task', name: 'Home', component: HomeView,
      children: [
        { path: ':detailId', name: 'TaskDetail', component: TaskDetail, },
        { path: ':editId/edit', name: 'EditPopup', component: EditPopup, },
      ]
    },
    { path: '/status', name: 'StatusView', component: StatusView,
      children: [
        { path: ':editStatusId/edit', name: 'EditStatusPopup', component: EditStatusPopup },
      ],
    },
    {
      path: '/404',
      name: 'Notfound',
      component: Notfound,
    },
  ],
})

// Check token expired
function isTokenExpired(payload) {
  // JWT token exp time is seconds
  const currentTime = Math.floor(Date.now() / 1000)
  console.log('now time', currentTime)
  console.log('exp time', payload.exp)
  if(payload.exp < currentTime) {
    return true // Already expired
  } else {
    return false
  }
}

// Navigation Guard
router.beforeEach((to, from, next) => {
  const token = JSON.parse(localStorage.getItem('token'))
  const payload = JSON.parse(localStorage.getItem('payload'))
  if(token && payload) {
    if(isTokenExpired(payload)) {
      // If token expired remove from localStorage
      localStorage.removeItem('token')
      localStorage.removeItem('payload')
      next({ name: 'Login' })
      return;
    }
  }
  if (to.name !== 'Login' && !localStorage.getItem('token')) {
    next({ name: 'Login' })
  }
  else next()
})

export default router
