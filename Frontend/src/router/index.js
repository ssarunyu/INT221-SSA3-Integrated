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
async function checkBoardAccess(to, from, next) {
  try {
    const responseBoardDetail = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${to.params.boardId}`);
    
    // ตรวจสอบว่า Board เป็น Public หรือไม่
    if (responseBoardDetail.visibility === 'PUBLIC') {
      to.meta.isOwner = false;
      next(); // ให้เข้าถึงได้ถ้า Board เป็น Public
    } else {
      // ถ้าไม่ใช่ Public ให้ตรวจสอบ token และ owner
      const payload = JSON.parse(localStorage.getItem('payload'));

      if (payload && responseBoardDetail.owner.userId === payload.oid) {
        to.meta.isOwner = true;
        next(); // User เป็นเจ้าของบอร์ด ให้เข้าถึงได้
      } else {
        next({ name: 'Login' }); // ไม่สามารถเข้าถึงได้ ให้ไปหน้า Login
      }
    }
  } catch (error) {
    console.error('Error fetching board details:', error);
    next({ name: 'Notfound' }); // ถ้ามีข้อผิดพลาด ให้ redirect ไปหน้า 404
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

  // ถ้ามี token และ payload
  if (token && payload) {
    // ตรวจสอบว่า token หมดอายุหรือไม่
    if (isTokenExpired(payload)) {
      localStorage.removeItem('token');
      localStorage.removeItem('payload');
      next({ name: 'Login' }); // ถ้า token หมดอายุ ให้ไปหน้า login
      return;
    }
  }

  // ตรวจสอบว่าเส้นทางเป็น public หรือไม่ (เช่น '/login' หรือ 'public board')
  if (to.name === 'Login' || to.name === 'Notfound' || to.meta.isPublic) {
    next(); // ให้ผ่านได้ถ้าเป็นเส้นทางที่ไม่ต้องการ auth
  } else {
    next(); // ให้ผ่านสำหรับเส้นทางที่ต้องการ auth แต่ token ไม่หมดอายุ
  }
});

export default router;
