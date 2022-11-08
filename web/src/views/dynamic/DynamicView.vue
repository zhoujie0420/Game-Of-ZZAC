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

              <!-- Button trigger modal -->
                  <button type="button" 
                  class="btn btn-primary reply-btn" 
                  data-bs-toggle="modal" 
                  :data-bs-target="'#reply-post-' + post.id"
                  >
                    评论
                  </button>

                  <!-- Modal -->
                  <div class="modal fade " 
                  :id="'reply-post-' + post.id" 
                  tabindex="-1" 
                  >
                    <div class="modal-dialog">
                      <div class="modal-content">
                        <div class="modal-header">
                          <h5 >评论</h5>
                          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                          <input type="text" v-model="replypost.content" >
                        </div>
                        <div class="modal-footer">
                          <div class="error-msg">{{ bot.error_message }}</div>
                          <button type="button" 
                          class="btn btn-secondary" 
                          data-bs-dismiss="modal" 
                          >Close</button>
                          <!-- <button type="button" class="btn btn-primary"  @click="reply_post(post.id,post.username)">Sure</button> -->
                        </div>
                      </div>
                    </div>
                  </div>


                  <div v-for="post in postlist.children" :key="post.id"></div>
            </div>
          </ContentFiled>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import ContentFiled from '../../components/ContentField.vue'
  import $ from 'jquery'
  import { ref ,reactive } from 'vue'
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

      const replypost = reactive({
        content: '',
        error_message : '',
      })

      // const reply_post = () => {
      //       bot.error_message = "";
           
      //       $.ajax({
      //           url: "http://127.0.0.1:3000/api/user/reply/add/",
      //           type: "post",
      //           data: {
      //             foreignId: bot.id,
      //             content: bot.title,
      //             pid: bot.description,
      //             target: bot.target,
      //           },
      //           headers: {
      //               Authorization: "Bearer " + store.state.user.token,
      //           },
      //           success(resp) {
      //               if (resp.error_message === "success") {
      //                   bot.title = "";
      //                   bot.description = "";
      //                   bot.content = "";
      //                   Modal.getInstance('#update-bot-modal-' + bot.id).hide();

      //               } else {
      //                   bot.error_message = resp.error_message;
      //                   setTimeout(() => { bot.error_message = ""; }, 2000);
      //                   Modal.getInstance('#update-bot-modal-' + post.id).show();
      //               }
      //               refresh_bots();
      //           }
      //       })
      //   }


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
        to_user_profile,
        // reply_post,
        replypost,
      }
    }
  }
  </script>
  
  <style scoped>
  .error-msg {
  margin-right: 20px;
  font-size: 16px;
  color: #c3404b;
  font-family: Verdana, Geneva, Tahoma, sans-serif;
}
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
  .reply-btn  {
    margin-left: 88%;
    margin-right: 3%;
    margin-top: 10px;
    margin-bottom: 10px;

  }
  </style>
  