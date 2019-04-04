<template>
<div class ="activity-page-index-container"
     v-infinite-scroll="loadMore"
     infinite-scroll-disabled="busy"
     infinite-scroll-distance="30"
     infinite-scroll-immediate-check="false"
>
  <v-layout row>
    <v-flex xs12 sm6 offset-sm3>
      <v-card>
        <v-toolbar color="cyan" dark>
          <v-toolbar-side-icon></v-toolbar-side-icon>

          <v-toolbar-title>用户列表</v-toolbar-title>

          <v-spacer></v-spacer>

          <v-btn icon>
            <v-icon>search</v-icon>
          </v-btn>
        </v-toolbar>

        <v-list two-line>
          <template v-for="(item, index) in itemList">
            <v-list-tile
              :key="index"
              avatar
              @click=""
            >
              <v-list-tile-avatar>
                <img :src="require('../../assets/img/user-face.jpg')">
              </v-list-tile-avatar>

              <v-list-tile-content>
                <v-list-tile-title v-html="item.userName"></v-list-tile-title>
                <v-list-tile-sub-title v-html="item.userPhone"></v-list-tile-sub-title>
              </v-list-tile-content>

            </v-list-tile>

          </template>
        </v-list>
      </v-card>
    </v-flex>
  </v-layout>
</div>
</template>

<script>
  export default {
    data () {
      return {
        pageIndex: 1,
        pageSize: 10,
        count:0,
        busy:false,
        list: [],
      }
    },
    computed:{
      end () {
        return this.count === this.list.length;
      },
      itemList(){
        return this.list.map(item => {
          const {id,userName,userPhone} = item;
          return {
                id,
                userName,
                userPhone,
                path: `/user/${id}`,
          }
        })
      },
    },

    created(){
      this.getList();
    },
    methods:{
      getList(){
        this.busy = true;
        this.$store.dispatch("user/list",
          {
            "pageIndex":this.pageIndex,
            "pageSize":this.pageSize
          }
        ).then((data) => {
            	const {list, count} = data
              this.list.push(...list)
              this.count = count;
              this.busy = false;
              this.pageIndex++;
        }).catch(({errMsg}) => {
          this.busy = false;
        });
      },
      loadMore () {
        if (this.busy === false && !this.end) {
          this.busy = true;
          this.getList()
        }
      }
    }

  }
</script>
