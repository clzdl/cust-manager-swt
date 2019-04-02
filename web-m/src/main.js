import Vue from 'vue'
import App from './App.vue'
import router from "./router/index"
import store from "./store/index"
import regPulgins from "./pulgins"
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import VueAxios from 'vue-axios'


Vue.use(VueAxios, axios);
Vue.use(ElementUI);


regPulgins(Vue);
Vue.config.productionTip = false
new Vue({
  router,
  store,
  render: h => h("App"),
  components: {App}
}).$mount('#app')
