<template>
  <v-form
    ref="form"
    v-model="valid"
    lazy-validation
  >
    <v-text-field
      v-model="userName"
      :counter="20"
      :rules="nameRules"
      label="用户名"
      required
    ></v-text-field>

    <v-text-field
      v-model="userPhone"
      :counter="11"
      :rules="nameRules"
      label="手机号码"
      required
    ></v-text-field>

    <v-text-field
      v-model="email"
      label="邮箱"
      required
    ></v-text-field>


    <v-radio-group v-model="sex" row>
      <v-radio label="女" value="0"></v-radio>
      <v-radio label="男" value="1"></v-radio>
    </v-radio-group>

    <v-btn
      :disabled="isClick"
      color="success"
      @click="addUser"
    >
      提交
    </v-btn>

    <v-btn
      color="error"
      @click="reset"
    >
      重置
    </v-btn>


  </v-form>
</template>
<script >
    import { post,get } from '../../assets/js/axiosUtil';
    export default {
      metaInfo: {
        title: '用户添加',
      },
      data: () =>({

          userName:"",
          nameRules: [
            v => !!v || '姓名未必填项',
            v => (v && v.length <= 10) || '长度不能超过10个字符'
          ],
          userPhone:"",
          sex:"0",
          // emailRules: [
          //   v => !!v || 'E-mail is required',
          //   v => /.+@.+/.test(v) || 'E-mail must be valid'
          // ],
          isClick:false,   ///防止重复点击标识

      }),
      created () {

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
