import Vue from 'vue'
import Vuex from 'vuex'
import config from "../assets/js/config"
import user from './user.js'
import worksImg from './worksImg.js'
const {baseUrl} = config;

Vue.use(Vuex)

export default new Vuex.Store({
		state: {
				all: null,
		},
		modules: {
				user,worksImg
		},
		mutations: {

		},
		actions: {

		}
	})
