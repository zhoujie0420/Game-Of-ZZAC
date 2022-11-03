<template>
    <div class="container body-base">
      <div class="row">
        <div
          class="card col-xs-12 col-sm-8 col-md-8"
          style="margin: 0 auto; margin-top: 20px; padding-bottom: 20px"
        >
          <div class="card-header card-header-name">讨论分享</div>
  
          <ContentFiled v-for="post in postlist" :key="post.id">
            <div class="post-user-info">
              <img
                @click="to_user_profile(post.userId)"
                class="post-user-avatar"
                :src="post.photo"
                style="cursor: pointer"
              />
              <div class="post-username">{{ post.username }}</div>
              <div class="post-time">发帖时间 {{ post.createtime }}</div>
            </div>
  
            <div class="card" style="margin-top: 20px">
              <div class="card-body">{{ post.content }}</div>
            </div>
          </ContentFiled>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import ContentFiled from '../../components/ContentField.vue'
  import $ from 'jquery'
  import { ref } from 'vue'
  import router from "@/router/index";
  import { useStore } from "vuex";
  export default {
    name: "DynamicsView",
    components: {
      ContentFiled,
    },
    setup () {
      const store = useStore();
  
      let postlist = ref([]);
      const allPosts = () => {
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/post/getlist/",
          type: "post",
          success (resp) {
            postlist.value = resp;
          },
        })
      }
      allPosts();
  
      const to_user_profile = (userId) => {
        if (store.state.user.is_login) {
          if (userId === store.state.user.id) {
            router.push({
              name: "myspace_index",
            });
          } else {
            router.push({
              name: "diaplay_view",
              params: {
                userId: userId,
              },
            });
          }
        } else {
          router.push({ name: "login_view" });
        }
      };
  
      return {
        postlist,
        to_user_profile
      }
    }
  }
  </script>
  
  <style scoped>
  .card-header-name {
    font-weight: bold;
    font-size: 24px;
  }
  .post-user-info {
    width: 100%;
    height: 5vh;
  }
  .post-user-avatar {
    float: left;
    height: 5vh;
    border-radius: 100%;
  }
  .post-username {
    float: left;
    line-height: 5vh;
    margin-left: 1vh;
    font-weight: bold;
  }
  .post-time {
    float: right;
    font-size: 14px;
    line-height: 5vh;
  }
  </style>
  