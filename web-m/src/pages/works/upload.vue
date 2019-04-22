<template>
<v-layout column>

    <v-form ref="form" lazy-validation>
        <v-flex xs12 sm6 offset-sm3>
            <UploadComp :uploadUrl="uploadImgUrl" :selImgUrl.sync="selImgUrl" :fileName.sync="selFileName"></UploadComp>
        </v-flex>
        <v-btn :disabled="isClick" color="success" @click="add">
            提交
        </v-btn>
    </v-form>

</v-layout>
</template>
<script >
import config from "../../assets/js/config";
const {
    uploadImgUrl
} = config;
import UploadComp from "../../components/util/uploadComp.vue";
export default {
    data: () => ({
        uploadImgUrl,
        selImgUrl: "",
        selFileName: "",
        isClick: false, ///防止重复点击标识
    }),
    created() {

    },
    methods: {
        add: function() {
            if (this.isClick === true) {
                return;
            }

            if (this.selImgUrl == "") {
                this.$globalTip({
                    type: "warning",
                    text: "请先选择图片"
                });
                return;
            }

            this.isClick = true;
            this.$store.dispatch("worksImg/add", {
                "imgUrl": this.selFileName,
            }).then((data) => {
                this.$globalTip({
                    type: "success",
                    text: `添加成功!`
                });
                this.isClick = false;
            }).catch(({
                errMsg
            }) => {
                this.$globalTip({
                    type: "warning",
                    text: "errMsg"
                });
                this.isClick = false;
            });
        },

    },
    components: {
        UploadComp
    }
}
</script>
