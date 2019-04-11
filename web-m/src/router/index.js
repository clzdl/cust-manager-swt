import Vue from "vue"
import VueRouter from "vue-router"

Vue.use(VueRouter)

const MainLayout = () =>
    import ("../pages/layout.vue");
const Index = () =>
    import ("../pages/index.vue");
const UserAdd = () =>
    import ("../pages/user/add.vue");
const UserList = () =>
    import ("../pages/user/list.vue");
const UploadWork = () =>
    import ("../pages/works/upload.vue");
const ListWork = () =>
    import ("../pages/works/list.vue");

const routes = [{
    path: "/web",
    component: MainLayout,
    children: [{
            path: "",
            component: Index,
            name: ""
        },
        {
            path: "user/add",
            component: UserAdd,
            name: "/user/add"
        },
        {
            path: "user/list",
            component: UserList,
            name: "/user/list"
        },
        {
            path: "works/upload",
            component: UploadWork,
            name: "/works/upload"
        },
        {
            path: "works/list",
            component: ListWork,
            name: "/works/list"
        },
    ]
}, ]
const router = new VueRouter({
    routes,
    mode: "history"
})

export default router