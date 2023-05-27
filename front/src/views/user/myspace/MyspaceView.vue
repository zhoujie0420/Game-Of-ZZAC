<template>
  <div>

    <div>
      <img class="tar" :src="$store.state.user.photo" alt="">
      <h1 style="float:left;color:#FFFFFF;margin-left:15px;margin-top:20px"> {{ $store.state.user.username }}</h1>
      <button type="button" class="logout btn btn-danger" @click="logout()">退出</button>
    </div>
    <div>&nbsp;</div>
    <div style="clear: both">

    </div>
  </div>


  <div style="margin-top:10px; clear: both; border-top: 1px solid #e2e2ee;">
    <div>&nbsp;</div>
    <div class="mypost">My Post</div>
  </div>


  <textarea v-model="content" class="form-control" id="edit" rows="3" style="border-color: #ff8200"
    placeholder="说些什么吧！">
  </textarea>

  <button type="button" @click="post_a_post" class="btn btn-primary btn-sm">
    发贴
  </button>

  <van-card v-for="post in posts.posts" :key="post.id"  >
    <template #tags>
      <h3>{{ post.content }}</h3>
    </template>
    <template #footer>
      <van-button style= "background-color:red" size="mini" @click="delete_a_post(post.id)">删除</van-button>
    </template>
  </van-card>


  <div style="clear:both;">

  </div>
  <div>
  </div>

</template>
<style scoped>
.logout {
  float: right;
  margin-top: 30px;
  margin-right: 20px;
}

.mypost {
  margin-left: 20px;
  Font-weight: bold;
  margin-top: 5px;
  font-size: 20px;
  color: #000000;

}

.tar {
  margin-top: 20px;
  height: 70px;
  width: 70px;
  border-radius: 50%;
  float: left;
  margin-left: 10px;
}
</style>


<script>

import { ref, nextTick, reactive,provide } from "vue";
import {  useRoute } from 'vue-router'
import { computed ,inject } from 'vue'
import { useStore } from 'vuex';
import $ from "jquery";

export default {
  components: {

  },
  setup() {
  


    let content = ref("");
    const refresh = inject("reload");

    const isRouterAlive = ref(true);
    const route = useRoute();
    let route_name = computed(() => route.name);
    const store = useStore();
    const posts = reactive({});

    const delete_a_post = (id) => {
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/post/remove",
          type: "post",
          data: {
            id: id,
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success (resp) {
            console.log(resp);
            refreshPost();
          },
        });

      };

    const refreshPost = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/post/getlistbyid",
        type: "post",
        data: {
          id: store.state.user.id,
        },
        headers: {
          "Authorization": "Bearer " + store.state.user.token,
        },
        success (resp) {
            posts.posts = resp;
        }
      })
    }
    refreshPost();
    
    const logout = () => {
      store.dispatch("logout");
      window.location.href = "/user/account/login/";
    }

    const post_a_post = () => {

     
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/post/add",
          type: "post",
          data: {
            content: content.value,
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success (resp) {
            if (resp.error_message ==="success") {
              content.value = "";
              refreshPost();
            }
          },
        });

        }


    const reload = () => {
      isRouterAlive.value = false;
      nextTick(() => {
        isRouterAlive.value = true;
      });
    };

    provide("reload", reload);
    return {
      content,
      isRouterAlive,
      route_name,
      logout,
      post_a_post,
      refresh,
      posts,
      delete_a_post,
    };
  }
}

</script>

