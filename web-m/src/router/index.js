import Vue from "vue"
import VueRouter from "vue-router"

Vue.use(VueRouter)

const MainLayout = () => import("../pages/layout.vue");
const Index = () => import("../pages/index.vue");
const HelloWorld = ()=> import("../pages/HelloWorld.vue");
const HelloWorld0 = ()=> import("../pages/HelloWorld0.vue");

const routes = [
	{
		path: "/web", component: MainLayout,
		children: [
			{ path: "", component: Index, name: "Index"},
			{ path: "/h1", component: HelloWorld, name: "h1"},
			{ path: "/h0", component: HelloWorld, name: "h0"},

		]
	},

]
const router = new VueRouter({
	routes,
	mode: "history"
})

export default router
