import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import TaskDetail from '@/components/TaskDetail.vue'
import EditPopup from '@/components/EditPopup.vue'
import Notfound from '@/views/Notfound.vue'

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
      component: HomeView,
      children: [
        {
          path: '/:id/edit',
          name: 'EditPopup',
          component: EditPopup,
          props: true
        }
      ]
    },
    {
      path: '/:taskId',
      name: 'TaskDetail',
      component: TaskDetail
    },
    {
      path: "/error",
      name: "Notfound",
      component: Notfound,
    },
  ]
})

export default router
