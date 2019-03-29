<template>
    <transition name="dialog-fade">
    <div class="spread-main-box">
            <div class="all-sort" @click="selectAll" :class="{active: idList === ''}" v-if="hasAll">
                全部
            </div>
            <spread-comp
                    v-for="item, index in list"
                    :list="item"
                    :key="index"
                    :idList="idList"
            >
            </spread-comp>
    </div>
    </transition>
</template>
<script>
    import SpreadComp from "./spreadComp.vue"
    export default {
    	name: "spreadMain",
    	props: {
    		list: {
    			type: Array
            },
            value: {
    			type: String
            },
            hasAll: {
    			type: Boolean,
                default: true
            }
        },
        watch: {
    		value () {
    			this.idList = this.value
            }
        },
        data () {
    		return {
    			idList: this.value
            }
        },
        created () {
    		this.$on('son:tg', (e) => {
    			const {close, idList, v}  = e
			    if (close) {
				    this.idList = e.idList
				    this.$emit('input', e.idList)
                    this.$emit('submit', v)
			    } else  {
    				this.sendInfiniteCd("spreadComp", {
    					event: 'sp:child',
                        playLoad: idList
                    })
                }
            })
            this.$nextTick(() => {
    			const {idList} = this
	            this.sendInfiniteCd("spreadComp", {
		            event: 'sp:child',
		            playLoad: idList
	            })
            })
        },
        mounted () {
    		window.document.body.style.overflow = 'hidden'
        },
        destroyed () {
	        window.document.body.style.overflow = ''
        },
        methods: {
	        selectAll () {
	        	this.idList = ''
		        this.$emit('input', '')
		        this.$emit('submit', '')
	        }
        },
        components: {SpreadComp}
    }
</script>
<style lang="scss">
    .spread-main-box {
        position: fixed;
        width: 10rem;
        top: 0;
        height: 100%;
        left: 50%;
        transform: translate(-50%, 0);
        background: #f3f1f1;
        z-index: 999;
        padding: 0 20px;
        overflow: auto;
        .all-sort {
            height: 80px;
            line-height: 80px;
            font-size: 30px;; /*px*/
            margin-bottom: 10px;
            background: #fff;
            padding-left: 20px;
            &.active {
                color: #c70002;
            }
        }
    }
</style>