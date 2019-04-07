import Vue from 'vue'
import App from './App.vue'
import router from "./router/index"
import store from "./store/index"

import axios from 'axios'
import VueAxios from 'vue-axios'
import 'amfe-flexible';

Vue.use(VueAxios, axios);
////下滑分页
import infiniteScroll from "vue-infinite-scroll";
Vue.use(infiniteScroll);
import regPulgins from "./pulgins"
regPulgins(Vue);

import 'material-design-icons-iconfont/dist/material-design-icons.css' // Ensure you are using css-loader
import 'vuetify/dist/vuetify.min.css'
import Vuetify from 'vuetify'
Vue.use(Vuetify,{iconfont: 'md'})




Vue.config.productionTip = true;


new Vue({
  router,
  store,
  render: h => h("App"),
  components: {App}
}).$mount('#app')
