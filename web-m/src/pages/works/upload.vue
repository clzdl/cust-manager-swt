<template>
  <v-layout>
    <v-flex xs6 sm4 v-show='imgUrl'>
      <v-img :src="imgUrl"/>
    </v-flex>
    <v-flex xs6 sm4>
      <uploader :options="options"
                @file-complete="fileComplete"
                @complete="complete"
                @file-success="fileSuccess"
                @file-error="fileError"
                class="uploader">

        <uploader-btn :attrs="attrs">select images</uploader-btn>
      </uploader>
    </v-flex>
  </v-layout>

</template>
<script >
    import config from "../../assets/js/config";
    const {uploadImgUrl} = config;
    export default {
      metaInfo: {
        title: '作品上传',
      },
      data: () =>({
        options: {
          target: uploadImgUrl,
          singleFile : true,
          testChunks: false
        },
        attrs: {
          accept: 'image/*'
        },
        imgUrl:"",

      }),
      created () {

      },

      methods: {
        complete () {
           console.log('complete', arguments)
         },
         fileComplete () {
           console.log('file complete', arguments)
         },
         fileError(rootFile, file, message, chunk){
            console.log(message);
         },
         fileSuccess(rootFile, file, message, chunk){
           let dataResp = JSON.parse(message);
           if(!dataResp.flag || dataResp.flag == 0){
             this.$globalTip({
                 type: "error",
                 text: `上传失败!`
             });
           }else{
             this.imgUrl = dataResp.data.path;
             console.log( this.imgUrl );
           }
         }
      }
  }
</script>

<style>
  .uploader {
    width: 880px;
    padding: 15px;
    margin: 40px auto 0;
    font-size: 12px;
    /* box-shadow: 0 0 10px rgba(0, 0, 0, .4); */
  }
  .uploader .uploader-btn {
    margin-right: 4px;
  }

</style>
