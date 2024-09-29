import { createRouter, createWebHistory } from 'vue-router'
import { jwtDecode } from "jwt-decode";

import HomeView from '@/views/HomeView.vue'
import TaskDetail from '@/components/TaskDetail.vue'
import AddPopup from '@/components/AddPopup.vue'
import EditPopup from '@/components/EditPopup.vue'

import AddStatusPopup from '@/components/AddStatusPopup.vue'
import EditStatusPopup from '@/components/EditStatusPopup.vue'

import Notfound from '@/views/Notfound.vue'
import StatusView from '@/views/StatusView.vue'

import LoginView from '@/views/LoginView.vue'

import BoardView from '@/views/BoardView.vue'
import AddBoardPopup from '@/components/AddBoardPopup.vue'
import BoardDetail from '@/views/BoardDetail.vue'

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
    {
      path: '/board',
      children: [
        // Initial page of board (that show all board)
        { path: '', name: 'Board', component: BoardView },
        { path: 'add', name: 'AddBoard', component: AddBoardPopup },
        { path: ':boardId', name: 'Home', component: HomeView,
          children: [
            // 1. First child is task
            { path: 'task',
              children: [
                { path: 'add', name: 'AddTask', component: AddPopup },
                { path: ':taskId/edit', name: 'EditTask', component: EditPopup }
              ]
            },
            // 2. Second child is status
            // TODO: More method of status
            { path: 'status', name: 'StatusView', component: StatusView }
          ]
        }
      ]
    },
    // { path: '/status', name: 'StatusView', component: StatusView,
    //   children: [
    //     { path: 'add', name: 'AddStatus', component: AddStatusPopup},
    //     { path: ':editStatusId/edit', name: 'EditStatusPopup', component: EditStatusPopup },
    //   ],
    // },
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
  if(payload.exp < currentTime) {
    return true // Already expired
  } else {
    return false
  }
}

// Navigation Guard
// TODO: Not-Well form
// router.beforeEach((to, from, next) => {
//   const token = JSON.parse(localStorage.getItem('token'))
//   const payload = JSON.parse(localStorage.getItem('payload'))
  
//   if(token && payload) {
//     if(isTokenExpired(payload)) {
//       // If token expired remove from localStorage
//       localStorage.removeItem('token')
//       localStorage.removeItem('payload')
//       next({ name: 'Login' })
//       return;
//     }
//   }
//   if (to.name !== 'Login' && !token) {
//     next({ name: 'Login' })
//   } else next()
// })

export default router