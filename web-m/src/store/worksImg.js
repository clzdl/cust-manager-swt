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
            return get(`${baseUrl}/worksimg/add.json`, data);
        },
        list({
            commit,
            state
        }, data) {
            return get(`${baseUrl}/worksimg/list.json`, data);
        }

    }
}