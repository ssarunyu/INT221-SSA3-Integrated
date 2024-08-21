import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

// Import Font Awesome core
import { library } from '@fortawesome/fontawesome-svg-core';

// Import Font Awesome icon component
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

// Import specific icons
import { faSortDown, faSortUp, faSort, faXmark } from '@fortawesome/free-solid-svg-icons';

// Add icons to the library
library.add(faSortDown, faSortUp, faSort, faXmark);

const pinia = createPinia()
const app = createApp(App)

app.component('font-awesome-icon', FontAwesomeIcon)
app.use(router)
app.use(pinia)

app.mount('#app')
