<template>
    <footer class="app-footer-container">
        <section class="nav-join">
            <router-link to="" active-class="nav-active">
                入会
            </router-link>
        </section>
    </footer>
</template>
<script>

    export default {
    	data () {
    		return {
            }
        },
    }
</script>
<style lang="scss" scoped>
    .app-footer-container {
       position: fixed;
       width: 750px;
        z-index: 10;
       bottom: 0;
       left: 50%;
       transform: translate(-50%, 0);
       display: flex;
       justify-content: space-between;
        background: #c40100;
       section {
           position: relative;
           display: flex;
           flex: 1;
           justify-content: center;
           align-items: center;
           height: 96px;
           .nav-user-img-box {
               width: 152px;
               padding: 0 10px;
               img {
                   display: block;
                   width: 100%;
                   height: auto;
                   vertical-align: middle;
               }
           }
           a {
               position: relative;
               display: block;
               width: 100%;
               height: 100%;
               padding-top: 63px;
               background: 50% 8px no-repeat;
               background-size: 48px 45px;
               text-align: center;
               color: #fff;
               &.nav-active {
                   background-color: #ff0100;
               }
           }
           &.nav-join {
               a {
                    background-image: url(../../assets/img/nav/nav-join.png);
               }
           }

       }
    }
</style>
