import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import TaskDetail from '@/components/TaskDetail.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/task'
    },
    {
      path: '/task',
      name: 'task',
      component: HomeView
    },
    {
      path: '/task/:taskId',
      name: 'TaskDetail',
      component: TaskDetail
    },
  ]
})

export default router
