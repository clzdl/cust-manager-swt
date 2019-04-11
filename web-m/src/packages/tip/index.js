import Vue from "vue"
import tip from "./tip.vue"
let uuid = 0
const exTip = () => {
    return new Vue({
        name: "global-comp-exTip",
        data() {
            return {
                show: false,
                text: "",
                type: "warning",
                uuid: ++uuid,
                time: 1500
            }
        },
        components: {
            tip
        },
        mounted() {
            this.show = true
            setTimeout(() => {
                this.show = false
            }, this.time)
        },
        render(h) {
            const _this = this
            if (_this.show === false) {
                return h("transition", {
                    props: {
                        "name": "tip-fade"
                    }
                })
            } else {
                return h("transition", {
                    props: {
                        "name": "tip-fade"
                    },
                }, [
                    h("tip", {
                        props: {
                            show: _this.show,
                            text: _this.text,
                            type: _this.type
                        }
                    })
                ])
            }
        }
    })
}
export default exTip