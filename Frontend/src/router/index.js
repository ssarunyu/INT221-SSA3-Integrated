import { createRouter, createWebHistory } from 'vue-router';
import { getData } from '@/lib/fetchMethod';
import HomeView from '@/views/HomeView.vue';
import TaskDetail from '@/components/TaskDetail.vue';
import AddPopup from '@/components/AddPopup.vue';
import EditPopup from '@/components/EditPopup.vue';
import AddStatusPopup from '@/components/AddStatusPopup.vue';
import EditStatusPopup from '@/components/EditStatusPopup.vue';
import Notfound from '@/views/Notfound.vue';
import StatusView from '@/views/StatusView.vue';
import LoginView from '@/views/LoginView.vue';
import BoardView from '@/views/BoardView.vue';
import AddBoardPopup from '@/components/AddBoardPopup.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', name: 'Login', component: LoginView },
    { path: '/', name: 'RootPath', redirect: { name: 'Login' } },
    { path: '/board', name: 'Board', component: BoardView },
    { path: '/404', name: 'Notfound', component: Notfound },
    {
      path: '/board/:boardId',
      name: 'Home',
      component: HomeView,
      beforeEnter: checkBoardAccess,
    },
    {
      path: '/board/add',
      name: 'AddBoard',
      component: AddBoardPopup,
      beforeEnter: checkBoardAccess,
    },
    {
      path: '/board/:boardId/task/:detailId',
      name: 'TaskDetail',
      component: TaskDetail,
      beforeEnter: checkBoardAccess 
    },
    {
      path: '/board/:boardId/task/add',
      name: 'AddTask',
      component: AddPopup,
      beforeEnter: checkBoardAccess,
    },
    {
      path: '/board/:boardId/task/:taskId/edit',
      name: 'EditTask',
      component: EditPopup,
      beforeEnter: checkBoardAccess,
    },
    {
      path: '/board/:boardId/status',
      name: 'StatusView',
      component: StatusView,
      beforeEnter: checkBoardAccess,
    },
    {
      path: '/board/:boardId/status/add',
      name: 'AddStatus',
      component: AddStatusPopup,
      beforeEnter: checkBoardAccess,
    },
    {
      path: '/board/:boardId/status/:editStatusId/edit',
      name: 'EditStatusPopup',
      component: EditStatusPopup,
      beforeEnter: checkBoardAccess,
    },
  ],
});

// Utility function to check board access
async function checkBoardAccess(to, from, next) {
  try {
    const token = JSON.parse(localStorage.getItem('token'));
    const responseBoardDetail = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${to.params.boardId}`)
    const userPayload = JSON.parse(localStorage.getItem('payload'));
    console.log('payload', userPayload)
    console.log('from fetch', responseBoardDetail)

    // Auth user owner that board permission
    // If mean auth and owner
    // Else mean auth but not the owner
    if(token && userPayload) {
      if(responseBoardDetail.owner.userId === userPayload.oid) {
        to.meta.isOwner = true
      } else {
        to.meta.isOwner = false
      }
      return next()
    }

    const allowRoute = ['Home', 'StatusView', 'TaskDetail']
    // Un-auth user control
    if(!userPayload || !token) {
      if(responseBoardDetail.visibility === 'PUBLIC') {
        to.meta.isOwner = false
        // Check path first
        if(allowRoute.includes(to.name)) {
          return next()
        } 
        else {
            window.alert('Access denied, you do not have permission to view this page.')
            router.push({ name: 'Login' })
            return;
        }
      }
    }

    return next()
    

  } catch (error) {
    console.error('Error fetching board details:', error);
    // Control yellow error
    return next()
  }
}

// Check token expired
function isTokenExpired(payload) {
  const currentTime = Math.floor(Date.now() / 1000);
  return payload.exp < currentTime;
}

// Navigation Guard
// router.beforeEach((to, from, next) => {
//   const token = JSON.parse(localStorage.getItem('token'));
//   const payload = JSON.parse(localStorage.getItem('payload'));

//   // Check if user is logged in
//   if (token && payload) {
//     // Check if token has expired
//     if (isTokenExpired(payload)) {
//       localStorage.removeItem('token');
//       localStorage.removeItem('payload');
//       next({ name: 'Login' }); // If token expired, go to login
//       return;
//     }
//   }

//   // Allow public routes and existing logic for authentication checks
//   if (to.name === 'Login' || to.name === 'Notfound') {
//     next(); // Allow access
//   } else {
//     // If it's a board-related route, additional checks are already handled in beforeEnter
//     next();
//   }
// });

export default router;