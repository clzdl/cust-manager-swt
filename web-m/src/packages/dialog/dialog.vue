<template>
    <transition name="custom-dialog-fade">
        <div class="dialog-cmp-container" v-show="value">
            <i class="close" @click="close" v-if="isShow"></i>
            <div class="dialog-slot-container" ref="slot" v-click-out="clickOut">
                <slot>
                </slot>
            </div>
        </div>
    </transition>
</template>
<script>
    export default {
    	props: {
    		value: {
    			type: Boolean,
                required: true
            },
            isShow: {
    			type: Boolean,
                default: true
            }
        },
        methods: {
      		clickOut () {
      			this.$emit('input', false)
          },
          close () {
  			   console.log('close')
            this.$emit('input', false)
          }
        },
        mounted () {
        }
    }
</script>
<style lang="scss" scoped>
    .dialog-cmp-container {
        position: fixed;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.48);
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        z-index: 999;
        i.close {
            position: absolute;
            right: 10px;
            top: 10px;
            width: 45px;
            height: 45px;
            display: block;
            background: #fff url(../../assert/img/close-red.png) 50% 50% no-repeat;
            background-size: 80% 80%;
            border-radius: 100%;
            box-shadow: 0 0 3px #ccc;
        }
        .dialog-slot-container {
            position: absolute;
            width: 80%;
            background: white;
            max-height: 70%;
            overflow: auto;
            left: 50%;
            top: 50%;
            border-radius: 5px;
            transform: translate(-50%, -50%)
        }
    }
</style>
