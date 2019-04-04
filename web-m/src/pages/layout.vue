<template>
  <div>
      <transition name="viewf">
          <router-view class="router-view-container"></router-view>
      </transition>
    <app-footer></app-footer>
  </div>
</template>

<script>
    import appFooter from "../components/layout/appFooter.vue"
    export default {
        components: {
        	appFooter
        }
    }
</script>
<style>
body {
  font-size: 24px;
  background-color: #f8f8f8;
  -webkit-font-smoothing: antialiased;
}

.router-view-container {
    padding: 0 0 180px 0;
    min-height: 100vh;
}
</style>
