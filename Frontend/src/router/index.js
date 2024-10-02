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
async function checkBoardAccess(to, from, next) {
  try {
      // Always try to get board details without requiring auth
      const responseBoardDetail = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${to.params.boardId}`, false);

      // Ensure responseBoardDetail is valid and has the visibility property
      if (responseBoardDetail && responseBoardDetail.visibility) {
          // Check if the board is PUBLIC
          if (responseBoardDetail.visibility === 'PUBLIC') {
              to.meta.isOwner = false;
              next(); // Allow access if the board is public
          } else {
              // If not public, check if the user is authenticated
              const token = JSON.parse(localStorage.getItem('token'));
              console.log('Token:', token); // Debugging output
              
              if (!token) {
                  console.log('No token found, redirecting to login.'); // Debugging output
                  next({ name: 'Login' }); // If no token, redirect to login
              } else {
                  // Check ownership if the user is authenticated
                  const payload = JSON.parse(localStorage.getItem('payload'));
                  console.log('Payload:', payload); // Debugging output
                  
                  if (payload && responseBoardDetail.owner && responseBoardDetail.owner.userId === payload.oid) {
                      to.meta.isOwner = true;
                      console.log('Owner access granted.'); // Debugging output
                      next(); // User is the owner, allow access
                  } else {
                      console.log('Not authorized, redirecting to login.'); // Debugging output
                      next({ name: 'Login' }); // Not authorized, redirect to login
                  }
              }
          }
      } else {
          console.log('Board not found or visibility is missing, redirecting to 404.'); // Debugging output
          next({ name: 'Notfound' }); // Board not found or visibility is missing, redirect to 404
      }
  } catch (error) {
      console.error('Error fetching board details:', error);
      next({ name: 'Notfound' }); // Handle fetch error, redirect to 404
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

  // Check if user is logged in
  if (token && payload) {
    // Check if token has expired
    if (isTokenExpired(payload)) {
      localStorage.removeItem('token');
      localStorage.removeItem('payload');
      next({ name: 'Login' }); // If token expired, go to login
      return;
    }
  }

  // Allow public routes and existing logic for authentication checks
  if (to.name === 'Login' || to.name === 'Notfound') {
    next(); // Allow access
  } else {
    // If it's a board-related route, additional checks are already handled in beforeEnter
    next();
  }
});

export default router;
