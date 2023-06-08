<template>
  <div class="container body-base">
    <div class="row">
      <div class="card col-xs-12 col-sm-8 col-md-8" style="margin: 0 auto; margin-top: 20px; padding-bottom: 20px">
        <div class="card-header card-header-name">讨论分享</div>

        <ContentFiled v-for="post in postlist" :key="post.id">
          <div class="post-user-info">
            <img class="post-user-avatar" :src="post.photo"
              style="cursor: pointer" />
            <div class="post-username">{{ post.username }}</div>
            <div class="post-time">发帖时间 {{ post.createtime }}</div>
          </div>
          <div class="card-body">{{ post.content }}</div>


   

          <div class="card" v-for="comment in commentlist" :key="comment.id">

            <div v-if="post.id === comment.foreignId">
              <div class="post-user-info">
                <div class="comment-username">{{ comment.username }}
                  <button v-if="$store.state.user.username === comment.username" type="button" class="btn btn-danger"
                    @click="remove_comment(comment.id)">删除</button>
                
                </div>
                <div class="comment-time"> {{ comment.createtime }}</div>
              </div>
              <div class="comment-content">
                {{ comment.content }}
              </div>

              <br />

            <!-- 多级评论  未实现 -->
              <!-- <div class="childcomment-card" v-for="childcomment in comment.childrenComment" :key="childcomment.id">

                <div class="post-user-info">
                  <div class="childcomment-username">{{ childcomment.username }} 回复 {{ childcomment.target }}
                    <button v-if="$store.state.user.username === childcomment.username" type="button"
                      class="btn btn-danger" @click="remove_comment(childcomment.id)">删除</button>
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                      :data-bs-target="'#add-comment-modal-' + comment.id">回复</button>
                  </div>

                  <div class="childcomment-time"> {{ childcomment.createtime }}</div>
                </div>
                <span class="childcomment-content">{{ childcomment.content }}</span>
              </div> -->
            </div>

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
  setup() {
    const store = useStore();
    const commentinput = ref([]);
    let postlist = ref([]);
    let commentlist = ref([]);
    let error_message = ref("");
    let stateinput = ref("");

    const add_comment = (id) => {
      error_message.value = "";
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/comment/add/",
        type: "post",
        data: {
          foreignId: id,
          content: commentinput.value[id],
          pid: "0",
          target: ""
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {

            commentinput.value[id] = "";
            getcommentlist(id);

          } else {
            error_message.value = resp.error_message;

          }
        }
      })
    }


    const allPosts = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/post/getlist/",
        type: "post",
        success(resp) {
          postlist.value = resp;
        },
      })
    }

    const getcommentlist = (foreignId) => {
      $.ajax({
        url: "http://localhost:3000/api/user/comment/getlist/",
        type: "post",
        data: {
          foreignId: foreignId,
        },
        success(resp) {

          commentlist.value = resp;

        }
      });
    };



    const remove_comment = (id) => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/comment/remove/",
        type: "post",
        data: {
          id: id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            getcommentlist(id);
          } else {
            console.log(resp.error_message);
          }
        }
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
      commentlist,
      getcommentlist,
      commentinput,
      add_comment,
      error_message,
      remove_comment,
      stateinput,
    }
  }
}
</script>
  
<style scoped>
.error-message {
  color: red;
}

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
  Font-size: 30px;
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



.childcomment-card {
  margin-top: 2px;
  width: 80%;
  margin-left: 10%;
  background-color: antiquewhite;
  height: 8vh;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.comment-username {
  Font-size: 20px;
  float: left;
  line-height: 5vh;
  margin-left: 5vh;
  font-weight: bold;
}

.comment-time {
  float: right;
  margin-right: 2vh;
  font-size: 14px;
  line-height: 5vh;
}

.comment-content {
  float: left;
  margin-left: 5vh;
  font-size: 16px;
}



.childcomment-username {
  float: left;
  margin-left: 5vh;
  font-size: 16px;
  margin-top: 3px;
  font-weight: bold;
}

.childcomment-time {
  float: right;
  margin-right: 2vh;
  font-size: 14px;
  line-height: 5vh;
}

.childcomment-content {
  float: left;
  margin-left: 5vh;
  font-size: 16px;
}


.opencomment-btn {
  margin-left: 88%;
  margin-right: 3%;
  margin-top: 10px;
  margin-bottom: 10px;
  border-color: #ccc;
}

.reply-btn {
  margin-left: 85%;
  margin-right: 3%;
  margin-top: 10px;
  margin-bottom: 10px;
  background-color: rgb(165, 165, 228);
}
</style>
  