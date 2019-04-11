<template>
<div class="global-common-tip" :class="type">
    {{text}}
</div>
</template>
<script>
export default {
    props: {
        text: {
            required: true,
            type: String
        },
        type: {
            required: true,
            type: String
        }
    },
    destroyed() {
        this.$parent.$emit("son::destroyed", true)
    }
}
</script>
<style lang="scss" scoped>
.global-common-tip {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: #fff;
    font-size: 32px;
    padding: 10px;
    text-align: center;
    font-size: 24px;
    z-index: 999999;
    max-width: 300px;
    line-height: 1.5;
    box-shadow: 0 0 3px #333;
    &.warning {
        background: #c70002;
    }
    &.success {
        background: #45c38a;
    }
}
</style>
