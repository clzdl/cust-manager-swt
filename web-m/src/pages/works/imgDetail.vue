<template>
<v-img :aspect-ratio="16/9" :src="imgUrl">
</v-img>
</template>
<script >
export default {
    data: () => ({
        imgUrl: "",
    }),
    created() {
        this.getById();
    },
    methods: {
        getById() {
            this.$store.dispatch("worksImg/info", {
                "id": this.$route.params.id,
            }).then((data) => {
                const {
                    imgUrl
                } = data;
                this.imgUrl = imgUrl;
            }).catch(({
                errMsg
            }) => {
                console.log(errMsg);
            });
        }

    },
    components: {

    }
}
</script>
