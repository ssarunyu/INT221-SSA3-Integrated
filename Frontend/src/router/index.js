import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import TaskDetail from '@/components/TaskDetail.vue'
import EditPopup from '@/components/EditPopup.vue'
import EditStatusPopup from '@/components/EditStatusPopup.vue'

import Notfound from '@/views/Notfound.vue'
import StatusView from '@/views/StatusView.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'RootPath',
      redirect: { name: 'Home' }
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

export default router