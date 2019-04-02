<template>
    <div class="index-page-container container">
        <div class="cg-sign-title">添加</div>
        <div class="cg-sign-container">
            <div class="cg-input-container"><input v-model="userName" placeholder="请输入姓名"/></div>
            <div class="cg-input-container"><input v-model="userPhone" placeholder="请输入姓名"/></div>
            <div class="cg-sign-btn" @click="addUser">提交</div>
        </div>
    </div>
</template>
<script >

  import { post,get } from '../../assets/js/axiosUtil'
  export default {
    metaInfo: {
	    title: '用户添加',
    },
  	data () {
  		return {
              userName:"",
              userPhone:"",
              sex:0,
              isClick:false,   ///防止重复点击标识
          }
      },
      created () {

      },

      methods: {
        addUser() {
            if(this.isClick === true){
              return ;
            }

            if(this.userName == "" || this.userPhone == ""){
              this.$globalTip({
                 type: "warning",
                 text: "请先填写用户名、手机号码"
              });
              return ;
            }

            this.isClick = true;
            this.$store.dispatch("user/add",
              {
                "userName":this.userName,
                "userPhone":this.userPhone
              }
            ).then((data) => {
              this.$globalTip({
                  type: "success",
                  text: `添加成功!`
              });
              this.isClick = false;
            }).catch(({errMsg}) => {
              this.$globalTip({
                type: "warning",
                text: "errMsg"
              });
              this.isClick = false;
            });


          }
      }
  }
</script>

<style lang="scss">
.cg-sign-title{
    margin-top:64px;
    text-align: center;
    font-size: 36px;

}
.cg-sign-container{
    display: flex;
    flex-direction: column;
    margin-top: 64px;
    .cg-input-container{
        height: 70px;
        line-height: 70px;
        width: 60%;
        margin:0 auto;
        margin-bottom: 32px;
        display: flex;
        overflow: hidden;
        align-items: center;
        border:solid #e2e2e2 1px;
        border-radius: 8px;
        input{
            height: 70px;
            line-height: 70px;
            padding-left: 16px;
            width: 100%;
            border: none;
            outline:none
        }
    }
    .cg-sign-btn{
        height: 70px;
        line-height: 70px;
        text-align: center;
        background-color: #ff0000;
        width: 60%;
        margin: 0 auto;
        color: #fff;
        font-size: 28px;
        border-radius: 8px;
        margin-bottom: 32px;
    }
}
</style>
