<template>
<v-container grid-list-xl>
    <v-layout column>
        <v-flex xs6 sm4 v-show='imageUrl'>
            <v-img :src="imageUrl" />
        </v-flex>
        <v-flex xs6 sm4>
            <uploader :options="options" @file-complete="fileComplete" @complete="complete" @file-success="fileSuccess" @file-error="fileError" class="uploader">
                <uploader-btn :attrs="attrs">请选择</uploader-btn>
            </uploader>
        </v-flex>
    </v-layout>
</v-container>
</template>
<script >
// import config from "../../assets/js/config";
// const {uploadImgUrl} = config;
export default {
    props: {
        uploadUrl: "",
        selImgUrl: "",
    },
    data: function() {
        return {
            options: {
                target: this.uploadUrl,
                singleFile: true,
                testChunks: false
            },
            attrs: {
                accept: 'image/*'
            },
            imageUrl: this.selImgUrl,
        }
    },
    computed: {

    },
    created() {

    },

    methods: {
        complete() {
            console.log('complete', arguments)
        },
        fileComplete() {
            console.log('file complete', arguments)
        },
        fileError(rootFile, file, message, chunk) {
            console.log(message);
        },
        fileSuccess(rootFile, file, message, chunk) {
            let dataResp = JSON.parse(message);
            if (!dataResp.flag || dataResp.flag == 0) {
                this.$globalTip({
                    type: "error",
                    text: `上传失败!`
                });
            } else {
                this.imageUrl = dataResp.data.path;
                this.$emit("update:selImgUrl", this.imageUrl);
                this.$emit("submit");
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
