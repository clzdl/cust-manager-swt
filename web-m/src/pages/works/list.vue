<template>
<div class="activity-page-index-container" v-infinite-scroll="loadMore" infinite-scroll-disabled="busy" infinite-scroll-distance="30" infinite-scroll-immediate-check="false">
    <v-layout>
        <v-flex xs12 sm6 offset-sm3>
            <v-card>
                <v-toolbar color="cyan" dark>
                    <v-toolbar-side-icon></v-toolbar-side-icon>
                    <v-toolbar-title>作品列表</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn icon>
                        <router-link to="/web/works/upload">
                            <v-icon>file_upload</v-icon>
                        </router-link>

                    </v-btn>
                </v-toolbar>
                <v-container grid-list-sm fluid>
                    <v-layout row wrap>
                        <v-flex v-for="(item, index) in itemList" :key="index" xs4 d-flex>
                            <v-card flat tile class="d-flex">
                                <v-img :src="item.imgUrl" aspect-ratio="1" class="grey lighten-2">
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
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-card>
        </v-flex>
    </v-layout>
</div>
</template>
<script>
export default {
    data() {
        return {
            pageIndex: 1,
            pageSize: 10,
            count: 0,
            busy: false,
            list: [],
        }
    },
    computed: {
        end() {
            return this.count === this.list.length;
        },
        itemList() {
            return this.list.map(item => {
                const {
                    id,
                    name,
                    imgUrl
                } = item;
                return {
                    id,
                    name,
                    imgUrl,
                    path: `/worksImg/${id}`,
                }
            })
        },
    },
    created() {
        this.getList();
    },
    methods: {
        getList() {
            this.busy = true;
            this.$store.dispatch("worksImg/list", {
                "pageIndex": this.pageIndex,
                "pageSize": this.pageSize
            }).then((data) => {
                const {
                    list,
                    count
                } = data
                this.list.push(...list)
                this.count = count;
                this.busy = false;
                this.pageIndex++;
            }).catch(({
                errMsg
            }) => {
                this.busy = false;
            });
        },
        loadMore() {
            if (this.busy === false && !this.end) {
                this.busy = true;
                this.getList()
            }
        }
    }
}
</script>
