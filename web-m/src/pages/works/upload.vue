<template>
  <v-form
    ref="form"
    lazy-validation
  >
    <UploadComp :uploadUrl="uploadImgUrl" :selImgUrl.sync="selImgUrl"></UploadComp>
    <v-btn
      color="success"
      @click="test"
    >
      test
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
        selImgUrl:""
      }),
      created () {

      },
      methods:{
        test:function (){
            console.log(this.selImgUrl);
        },

      },
      components:{
          UploadComp
      }
  }
</script>
