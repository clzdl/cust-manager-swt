<template>
  <div>
    <transition name="viewf">
        <router-view class="router-view-container"></router-view>
    </transition>


    
    <v-card class ="app-footer-container" flat>
     <v-bottom-nav
       :active.sync="linkUrl"
       :value="true"
       absolute
       fixed="true"
       color="transparent">
         <v-btn
           color="teal"
           value="/web/user/list">

           <span>用户</span>
           <v-icon>history</v-icon>
         </v-btn>

         <v-btn
           color="teal"
           flat
           value="/web/card/list">

         <span>会员卡</span>
         <v-icon>favorite</v-icon>
       </v-btn>

     </v-bottom-nav>
   </v-card>
</div>
</template>

<script>
    export default {
      data () {
        return {
          linkUrl: '',
        }
      },

      watch: {
        linkUrl(){
               this.$router.push(this.linkUrl);
             }
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
.app-footer-container {
   position: fixed;
   width: 750px;
   z-index: 10;
   bottom: 0;
   left: 50%;
   transform: translate(-50%, 0);
   display: flex;
   justify-content: space-between;
}

</style>
