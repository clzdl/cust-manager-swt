import {
    get,
    post
} from "../assets/js/axiosUtil"
import config from "../assets/js/config"
const {
    baseUrl
} = config
export default {
    namespaced: true,
    state: {},
    actions: {
        add({
            commit,
            state
        }, data) {
            return get(`${baseUrl}/user/add.json`, data);
        },
        list({
            commit,
            state
        }, data) {
            return get(`${baseUrl}/user/list.json`, data);
        }
    }
}