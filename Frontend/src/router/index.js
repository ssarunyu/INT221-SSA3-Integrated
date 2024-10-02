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

    // Dynamic routes
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
    { path: '/board/:boardId/:detailId', name: 'TaskDetail', component: TaskDetail },
    {
      path: '/board/:boardId/task/add',
      name: 'AddTask',
      component: AddPopup,
      beforeEnter: checkBoardAccess,
    },
    {
      path: '/board/:boardId/:taskId/edit',
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
// Utility function to check board access
async function checkBoardAccess(to, from, next) {
  const payload = JSON.parse(localStorage.getItem('payload'));

  try {
    const responseBoardDetail = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${to.params.boardId}`);
    console.log(responseBoardDetail);

    // Ensure responseBoardDetail is defined and has the expected structure
    if (!responseBoardDetail) {
      window.alert('Error of fetch board detail for un-auth user i dont know how to fix');
      return next({ name: 'Login' }); // Redirect to login if board is not found
    }

    // Check if the user is the owner
    if (payload && responseBoardDetail.owner.userId === payload.oid) {
      to.meta.isOwner = true;
      next(); // User is the owner, continue to the route
    } else if (responseBoardDetail.visibility === 'PUBLIC') {
      to.meta.isOwner = false;
      next(); // Public board access granted
    } else {
      window.alert('Access denied, you do not have permission to view this page.');
      next({ name: 'Login' }); // Redirect to login for private boards
    }
  } catch (error) {
    console.error('Error fetching board details:', error);
    window.alert('Error fetching board details. Redirecting to login.');
    next({ name: 'Login' });
  }
}


// Check token expired
function isTokenExpired(payload) {
  const currentTime = Math.floor(Date.now() / 1000);
  return payload.exp < currentTime;
}

// Navigation Guard
router.beforeEach((to, from, next) => {
  const token = JSON.parse(localStorage.getItem('token'));
  const payload = JSON.parse(localStorage.getItem('payload'));
  if (token && payload) {
    if (isTokenExpired(payload)) {
      localStorage.removeItem('token');
      localStorage.removeItem('payload');
      next({ name: 'Login' });
      return;
    }
  }
  next(); // Allow access to all routes for unauthenticated users
});

export default router;
