<template>
<router-link :to="msg.path">
    <v-card flat tile class="d-flex">
        <v-img :src="msg.imgUrl" aspect-ratio="1" class="grey lighten-2">
            <template v-slot:placeholder>
            <v-layout
              fill-height
              align-center
              justify-center
              ma-0
            >
              <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
            </v-layout>
          </template>
        </v-img>
    </v-card>
</router-link>
</template>
<script >
// import config from "../../assets/js/config";
// const {uploadImgUrl} = config;
export default {
    props: {
        msg: {
            type: Object
        }
    },
    data: function() {
        return {}
    },
    computed: {

    },
    created() {

    },

    methods: {

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
