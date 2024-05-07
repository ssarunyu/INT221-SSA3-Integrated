import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import TaskDetail from '@/components/TaskDetail.vue'
import EditPopup from '@/components/EditPopup.vue'
import Notfound from '@/views/Notfound.vue'
import { getDataById } from '@/lib/fetchMethod.js'

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
          path: ':editId/edit',
          name: 'EditPopup',
          component: EditPopup,
          props: true,
          async beforeEnter(to) {
            const result = await getDataById(import.meta.env.VITE_URL, to.params.editId)
            if(result.status === 404) {
              alert('The requested task does not exist')
              router.go(-1)
            }
          }
        },
        {
          path: ':detailId',
          name: 'TaskDetail',
          component: TaskDetail,
          props: true,
          async beforeEnter(to) {
            const result = await getDataById(import.meta.env.VITE_URL, to.params.detailId)
            if(result.status === 404) {
              alert('The requested task does not exist')
              router.push('/404')
              setTimeout(() => {
                router.push('/task')
              }, 3000)
            }
          }
        },
      ]
    },
    {
      path: '/404',
      name: 'Notfound',
      component: Notfound
    }
  ]
})

export default router
