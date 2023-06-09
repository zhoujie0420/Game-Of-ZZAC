<template>
  <div class="container body-base">
    <div class="row">
      <div class="card" style="margin: 0 auto; margin-top: 20px; padding-bottom: 20px">
        <div class="card-header card-header-name">讨论分享</div>

          <ContentFiled v-for="(item, i) in postlist" :key="i" class="author-title reply-father">
      <el-avatar class="header-img" :size="40" :src="item.headImg"></el-avatar>
      <div class="author-info">
        <span class="author-name">{{ item.name }}</span>
        <span class="author-time">{{ item.createtime }}</span>
      </div>
      <div class="icon-btn">

        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal"
          data-bs-whatever="@mdo">回复</button>

        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">New Comment</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <form>
                  <div class="mb-3">
                    <label for="message-text" class="col-form-label">Comment :</label>
                    <textarea v-model="inputvalue[item.id]" class="form-control" id= item.id ></textarea>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" @click="add_comment(item.id,item.userId)">Send message</button>
              </div>
            </div>
          </div>
        </div>

        <!-- <i class="iconfont el-icon-caret-top"></i>{{item.like}} -->
      </div>
      <div class="talk-box">
        <p>
          <span class="reply">{{ item.content }}</span>
        </p>
      </div>
      <div class="reply-box">
        <div v-for="(reply, j) in item.comments" :key="j" class="author-title">
          <!-- <el-avatar class="header-img" :size="40" :src="reply.fromHeadImg"></el-avatar> -->
          <div class="author-info">
            <span class="author-name">{{ reply.username }}</span>
            <span class="author-time">{{ reply.createtime }}</span>
          </div>
   
          <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal"
          data-bs-whatever="@mdo">回复</button>

        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">New message</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <form>
                  <div class="mb-3">
                    <label for="message-text" class="col-form-label">Message:</label>
                    <textarea v-model="inputvalue[reply.id]" class="form-control" id= item.id ></textarea>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" @click="add_comment(item.id,item.userId)">Send message</button>
              </div>
            </div>
          </div>
        </div>
          <div class="talk-box">
            <p>
              <span>回复 {{ reply.targetUsername }}:</span>
              <span class="reply">{{ reply.content }}</span>
            </p>
          </div>
          <div class="reply-box">

          </div>
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

  components: {
    ContentFiled,
  },

  setup() {
    const store = useStore();
    const inputvalue = ref([]);
    let postlist = ref([]);
    let commentlist = ref([]);
    let error_message = ref("");

    const test = (t1,t2,t3) => { // 点击了具体某条消息
      console.log("点击了消息", t1 ,"+" , t2,"+" ,t3);
      console.log(inputvalue.value[t1]);
    }
    const add_comment = (id,targetuserid) => {
      error_message.value = "";
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/comment/add/",
        type: "post",
        data: {
          postId: id,
          content: inputvalue.value[id],
          targetUserId: targetuserid,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            inputvalue.value[id] = "";
            // getcommentlist(id);
          } else {
            error_message.value = resp.error_message;
          }
          allPosts();
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
        url: "http://localhost:3000/api/user/comment/getComment/",
        type: "post",
        data: {
          postId: id,
        },
        success(resp) {
          commentlist.value = resp;
          console.log(resp);
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
      add_comment,
      error_message,
      remove_comment,
      useStore,
      inputvalue,
      test,
    }
  }
}


</script>
  


<style scoped lang="css">


.header-img {
  display: inline-block;
  vertical-align: top;
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


.card-header-name {
  font-weight: bold;
  font-size: 24px;
}


.my-reply .header-img {
  display: inline-block;
  vertical-align: top;
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
  