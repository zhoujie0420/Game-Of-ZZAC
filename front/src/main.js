import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import { Tabbar, TabbarItem } from 'vant';

 
import vant from 'vant';
import 'vant/lib/index.css';

createApp(App).use(vant).use(Tabbar).use(TabbarItem).use(store).use(router).mount('#app')
