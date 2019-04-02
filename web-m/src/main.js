import Vue from 'vue'
import App from './App.vue'
import router from "./router/index"
import store from "./store/index"
import regPulgins from "./pulgins"
import axios from 'axios'
import VueAxios from 'vue-axios'
import 'amfe-flexible';

Vue.use(VueAxios, axios);

regPulgins(Vue);
Vue.config.productionTip = false
new Vue({
  router,
  store,
  render: h => h("App"),
  components: {App}
}).$mount('#app')
