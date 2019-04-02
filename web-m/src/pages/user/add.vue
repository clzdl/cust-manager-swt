<template>
  <div>
    <van-cell-group>
      <van-field
        v-model="userName"
        required
        clearable
        label="用户名"
        right-icon="question-o"
        placeholder="请输入用户名"
        @click-right-icon="$toast('question')"
      />

      <van-field
        v-model="userPhone"
        required
        label="电话"
        placeholder="请输入手机号码"
        required
      />

      <van-row type="flex" justify="center">
        <van-radio-group v-model="sex"
                class="demo-radio-group" >
                <van-col span="">
                  <van-radio  name="0">女</van-radio>
                </van-col>
                <van-col span="">
                  <van-radio  name="1">男</van-radio>
                </van-col>
        </van-radio-group>
      </van-row>
      <van-row type="flex" justify="center">
          <van-button type="primary" size="small" @click="addUser()">提交</van-button>
      </van-row>


    </van-cell-group>


 </div>
</template>
<script >
  import { Field,RadioGroup,Radio,CellGroup,Row, Col,Button } from 'vant';

  import { post,get } from '../../assets/js/axiosUtil';
  export default {
    metaInfo: {
	    title: '用户添加',
    },
  	data () {
  		return {
              userName:"",
              userPhone:"",
              sex:'0',
              isClick:false,   ///防止重复点击标识
          }
      },
      created () {

      },
      components:{
        [Field.name]: Field,
        [RadioGroup.name]: RadioGroup,
        [Radio.name] : Radio,
        [CellGroup.name]: CellGroup,
        [Row.name]:Row,
        [Col.name]:Col,
        [Button.name]:Button
      },
      methods: {
        addUser() {
          console.log("====================");
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
                "userPhone":this.userPhone,
                "sex":this.sex
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
<style lang="less">

}
