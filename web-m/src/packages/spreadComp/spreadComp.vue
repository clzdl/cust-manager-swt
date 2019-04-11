<template>
<section class="spread-comp-child">
    <template v-if="list.child">
            <div class="spread-comp-child-item-title" @click="selectTg" :class="{active: isToggle}">{{list.name}}</div>
            <spread-transition>
                <div class="spread-comp-child-item-con" v-if="isToggle">
                    <div v-for="item, index in list.child " :key="index" v-if="item.child">
                        <spread-comp
                                :list="item"
                                :idList="idList"
                                :fId= `${cfId}${list.id}`>
                        </spread-comp>
                    </div>
                    <div  :key="index"
                          class="spread-comp-child-item-select"
                          v-else
                          @click="selectEnd(item.id, item.name)"
                          :class="{active: idList === `${cfId}${list.id}-${item.id}`}"
                    >
                        {{item.name}}
                    </div>
                </div>
            </spread-transition>
        </template>
    <template v-else>
            <div
                    class="spread-comp-child-item-select"
                    @click="selectEnd(false, item.name)"
                    :class="{active: idList === `${cfId}${list.id}`}"
            >
                {{item.name}}
            </div>
        </template>
</section>
</template>
<script>
import spreadTransition from "../spread/spreadTransition.vue"
export default {
    components: {
        spreadTransition
    },
    name: 'spreadComp',
    props: {
        list: {
            type: Object
        },
        idList: {
            type: String
        },
        fId: {
            default: function() {
                return ''
            }
        }
    },
    data() {
        return {
            isToggle: false
        }
    },
    created() {
        // 监听子事件
        this.$on('sp:child', (idList) => {
            const {
                cfId,
                list,
                isToggle
            } = this
            if (new RegExp(`${cfId}${list.id}`).exec(idList)) {
                if (isToggle) {
                    this.isToggle = false
                } else {
                    this.isToggle = true
                }
            } else {
                this.isToggle = false
            }
        })
    },
    computed: {
        cfId() {
            return this.fId === '' ? '' : `${this.fId}-`
        },
        isFtg() {
            const {
                idList,
                cfId,
                list
            } = this
            return new RegExp(`${cfId}${list.id}`).exec(idList)
        }
    },
    methods: {
        selectEnd(id, v) {
            if (id === false) {
                this.sendFather('spreadMain', {
                    event: "son:tg",
                    playLoad: {
                        idList: `${this.cfId}${this.list.id}`.replace(/^-/, ''),
                        close: true,
                        v
                    }
                })
            } else {
                this.sendFather('spreadMain', {
                    event: "son:tg",
                    playLoad: {
                        idList: `${this.cfId}${this.list.id}-${id}`.replace(/^-/, ''),
                        close: true,
                        v
                    }
                })
            }

        },
        selectTg() {
            this.sendFather('spreadMain', {
                event: "son:tg",
                playLoad: {
                    idList: `${this.cfId}${this.list.id}`.replace(/^-/, ''),
                    close: false
                }
            })
        }
    }
}
</script>
<style lang="scss">
.spread-comp-child {
    font-size: 30px;
    /*px*/
    color: #333333;
    .spread-comp-child-item-con {
        padding-left: 20px;
    }
    .spread-comp-child-item-select,
    .spread-comp-child-item-title {
        position: relative;
        height: 70px;
        line-height: 70px;
        text-align: left;
        margin-top: 10px;
        background: #fff;
        padding-left: 20px;
    }
    .spread-comp-child-item-select {
        &.active {
            color: #c70002;
        }
    }
    .spread-comp-child-item-title {
        &::after {
            position: absolute;
            content: '';
            display: block;
            top: 50%;
            right: 20px;
            width: 36px;
            height: 36px;
            background: url("../../assert/img/arrow-gray.png") 0 0 no-repeat;
            background-size: 36px 36px;
            transform: translate(0, -50%);
            transition: all ease 300ms;
        }
        &.active {
            &::after {
                transform: translate(0, -50%) rotate(-180deg);
            }
        }
    }
}
</style>
