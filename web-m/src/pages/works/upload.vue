<template>
  <v-form
    ref="form"
    lazy-validation
  >
    <UploadComp :uploadUrl="uploadImgUrl" :selImgUrl.sync="selImgUrl"></UploadComp>
    <v-btn
      :disabled="isClick"
      color="success"
      @click="add"
    >
      提交
    </v-btn>
  </v-form>

</template>
<script >
    import config from "../../assets/js/config";
    const {uploadImgUrl} = config;
    import UploadComp from "../../components/util/uploadComp.vue";
    export default {
      data: () =>({
        uploadImgUrl,
        selImgUrl:"",
        isClick:false,   ///防止重复点击标识
      }),
      created () {

      },
      methods:{
        add:function (){
          if(this.isClick === true){
            return ;
          }

          if(this.selImgUrl == ""){
            this.$globalTip({
               type: "warning",
               text: "请先选择图片"
            });
            return ;
          }

          this.isClick = true;
          this.$store.dispatch("worksImg/add",
            {
              "imgUrl":this.selImgUrl,
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
        },

      },
      components:{
          UploadComp
      }
  }
</script>
