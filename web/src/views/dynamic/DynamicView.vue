<template>
  <div class="container body-base">
    <div class="row">
      <div class="card col-xs-12 col-sm-8 col-md-8" style="margin: 0 auto; margin-top: 20px; padding-bottom: 20px">
        <div class="card-header card-header-name">讨论分享</div>

        <ContentFiled v-for="post in postlist" :key="post.id">
          <div class="post-user-info">
            <img @click="to_user_profile(post.userId)" class="post-user-avatar" :src="post.photo"
              style="cursor: pointer" />
            <div class="post-username">{{ post.username }}</div>
            <div class="post-time">发帖时间 {{ post.createtime }}</div>
          </div>
          <div class="card-body">{{ post.content }}</div>

          <div @click="inputFocus" class="my-reply">

            <el-avatar class="header-img" :size="40" :src="$store.state.user.photo"></el-avatar>
       
            <div class="reply-info">
              <input tabindex="0" placeholder="输入评论..." v-model="commentinput[post.id]"
                class="reply-input">
            </div>
            
            <div class="reply-btn-box">
              <el-button type="button" class="reply-btn" size="medium" @click="add_comment(post.id)">发表评论</el-button>
            </div>
          </div>
          <div class="error-message">{{ error_message }}</div>
          <button type="button" class="opencomment-btn" @click="getcommentlist(post.id)">展开</button>

          <div class="author-title reply-father" v-for="comment in commentlist" :key="comment.id">
            <!-- <el-avatar class="header-img" :size="40" :src="comment.photo"></el-avatar> -->
            <div class="author-info">
                <span class="author-name">{{comment.username}}</span>
                <span class="author-time">{{comment.createtime}}</span>
            </div>
            <div class="icon-btn">
                <span @click="getcommentlist(post.id)">
                    <i class="iconfont el-icon-s-comment"></i>{{comment.username}}</span>
                <i class="iconfont el-icon-caret-top"></i>{{comment.username}}
            </div>

            <div class="talk-box">
                <p>
                    <span class="reply">{{comment.content}}</span>
                </p>
            </div>

            <div class="reply-box">
                  <div class="author-title" v-for="childcomment in comment.childrenComment" :key="childcomment.id">
                    <!-- <el-avatar class="header-img" :size="40" :src="comment.photo"></el-avatar> -->
                    <div class="author-info">
                        <span class="author-name">{{childcomment.username}}</span>
                        <span class="author-time">{{childcomment.createtime}}</span>
                    </div>
                    <div class="icon-btn">
                        <!-- <span @click="showReplyInput(i,reply.from,reply.id)"><i class="iconfont el-icon-s-comment"></i>{{reply.commentNum}}</span>
                        <i class="iconfont el-icon-caret-top"></i>{{reply.like}} -->
                    </div>
                    <div class="talk-box">
                        <p>
                            <span>回复 {{reply.to}}:</span>
                            <span class="reply">{{childcomment.content}}</span>
                        </p>
                    </div>
                    <div class="reply-box">

                    </div>
                </div>
            </div>
            <!-- <div v-if="post.id === comment.postId">
              <div class="post-user-info">
                <div class="comment-username">
                  {{ comment.username }}
                </div>
                <div class="comment-time"> {{ comment.createtime }}
                  <button v-if="$store.state.user.username == comment.username" type="button"
                    class="comment-click btn btn-danger" @click="remove_comment(comment.id)">删除</button>
                  <button type="button" class=" comment-click btn btn-primary"
                    @click="add_child_comment(comment.id)">回复</button>
                </div>
              </div>
              <div class="comment-content">
                {{ comment.content }}
              </div>

              <br /> -->

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
            <!-- </div> -->

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
          postId: id,
          content: commentinput.value[id],
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


    const add_child_comment = (id) => {
      error_message.value = "";
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/comment/addChild/",
        type: "post",
        data: {
          commentId: id,
          content: commentinput.value[id],
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

    const getcommentlist = (id) => {
      $.ajax({
        url: "http://localhost:3000/api/user/comment/getlist/",
        type: "post",
        data: {
          postId: id,
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
      add_child_comment,
      stateinput,
      useStore,
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

.comment-click {
  float: right;
  margin-top: 5vh;
  margin-right: 2vh;
  margin-bottom: 2vh;
  font-size: 12px;
  line-height: 2vh;

  cursor: pointer;
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


.my-reply {
  padding: 10px;
  background-color: #fafbfc;
}

.my-reply .header-img {
  display: inline-block;
  vertical-align: top;
}

.my-reply .reply-info {
  display: inline-block;
  margin-left: 5px;
  width: 90%;
}

@media screen and (max-width: 1200px) {
  .my-reply .reply-info {
    width: 80%;
  }
}

.my-reply .reply-info .reply-input {
  min-height: 20px;
  line-height: 22px;
  padding: 10px 10px;
  color: #ccc;
  background-color: #fff;
  border-radius: 5px;
}

.my-reply .reply-info .reply-input:empty:before {
  content: attr(placeholder);
}

.my-reply .reply-info .reply-input:focus:before {
  content: none;
}

.my-reply .reply-info .reply-input:focus {
  padding: 8px 8px;
  border: 2px solid #00f;
  box-shadow: none;
  outline: none;
}

.my-reply .reply-btn-box {
  height: 25px;
  margin: 10px 0;
  
}

.my-reply .reply-btn-box .reply-btn {
  position: relative;
  float: right;
  margin-right: 15px;
}

.my-comment-reply {
  margin-left: 50px;
}

.my-comment-reply .reply-input {
  width: flex;
}

.author-title:not(:last-child) {
  border-bottom: 1px solid rgba(178, 186, 194, 0.3);
}

.author-title {
  margin-top: 1rem;
  padding: 10px;
}

.author-title .header-img {
  display: inline-block;
  vertical-align: top;
}

.author-title .author-info {
  display: inline-block;
  margin-left: 5px;
  width: 60%;
  height: 40px;
  line-height: 20px;
}

.author-title .author-info>span {
  display: block;
  cursor: pointer;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.author-title .author-info .author-name {
  color: #000;
  font-size: 18px;
  font-weight: bold;
}

.author-title .author-info .author-time {
  font-size: 14px;
}

.author-title .icon-btn {
  width: 30%;
  padding: 0 !important;
  float: right;
}

@media screen and (max-width: 1200px) {
  .author-title .icon-btn {
    width: 20%;
    padding: 7px;
  }
}

.author-title .icon-btn>span {
  cursor: pointer;
}

.author-title .icon-btn .iconfont {
  margin: 0 5px;
}

.author-title .talk-box {
  margin: 0 50px;
}

.author-title .talk-box>p {
  margin: 0;
}

.author-title .talk-box .reply {
  font-size: 16px;
  color: #000;
}

.author-title .reply-box {
  margin: 10px 0 0 50px;
  background-color: #efefef;
}
</style>
  