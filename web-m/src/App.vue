<template>
    <div class="web-app-container">
        <transition name="view">
            <router-view></router-view>
        </transition>
    </div>
</template>

<style lang="scss">
@import "./assets/scss/_reset.scss";
@import "./assets/scss/_mian.scss";
html {
    width: 100%;
    height: 100%;
}
body {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: auto;
}
</style>
