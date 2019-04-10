import Vue from "vue"
import VueRouter from "vue-router"

Vue.use(VueRouter)

const MainLayout = () => import("../pages/layout.vue");
const Index = () => import("../pages/index.vue");
const HelloWorld = ()=> import("../pages/HelloWorld.vue");
const HelloWorld0 = ()=> import("../pages/HelloWorld0.vue");
const UserAdd = ()=> import("../pages/user/add.vue");
const UserList = ()=> import("../pages/user/list.vue");
const UploadWork = ()=> import("../pages/works/upload.vue");


const routes = [
	{
		path: "/web", component: MainLayout,
		children: [
			{ path: "", component: Index, name: ""},
			{ path: "h1", component: HelloWorld, name: "/h1"},
			{ path: "h0", component: HelloWorld0, name: "/h0"},
			{ path: "user/add", component: UserAdd, name: "/user/add"},
			{ path: "user/list", component: UserList, name: "/user/list"},
			{ path: "works/upload", component: UploadWork, name: "/works/upload"},
		]
	},
]
const router = new VueRouter({
	routes,
	mode: "history"
})

export default router
