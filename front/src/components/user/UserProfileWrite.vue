
<template>
    <div class="card edit-filed">
      <div class="card-body">
        <div class="mb-3">
          <label
            for="edit"
            class="form-label"
            style="font-weight: 400; color: gray; user-select: none"
            >创建帖子</label
          >
          <textarea
            v-model="content"
            class="form-control"
            id="edit"
            rows="2"
            style="border-color: #ff8200"
            placeholder="有什么新鲜事想分享给大家?"
          >
          </textarea>
  
          <div
            class="modal fade"
            id="blank_notice"
            tabindex="-1"
            aria-labelledby="exampleModalLabel"
            aria-hidden="true"
          >
            <div class="modal-dialog modal-dialog-centered">
              <div
                class="modal-content"
                style="background-color: white; width: 340px; margin: 0 auto"
              >
                <div class="modal-header">
                  <img
                    src="https://cdn.acwing.com/media/article/image/2022/09/02/36510_233881192a-热门.png"
                    alt="警告!"
                    style="height: 20px; margin: 0 auto"
                  />
                </div>
                <div
                  class="modal-body notice_msg"
                  style="margin: 0 auto; color: #838383"
                >
                  发帖内容不能为空~
                </div>
                <div class="modal-footer" style="margin: 0 auto">
                  <button
                    type="button"
                    class="btn"
                    data-bs-dismiss="modal"
                    style="background-color: #f0f0f0; border-style: none"
                  >
                    确认
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
  
        <button type="button" @click="post_a_post" class="btn btn-primary btn-sm">
          发贴
        </button>
  
        <button
          hidden
          data-bs-toggle="modal"
          data-bs-target="#blank_notice"
          id="btn-blank"
        ></button>
      </div>
    </div>
  </template>
  
  <script>
  import $ from "jquery";
  import { useStore } from "vuex";
  import { useRouter, useRoute } from 'vue-router'
  import { computed, inject, ref } from 'vue'
  export default {
    name: "UserProfileWrite",
    setup () {
      const store = useStore();
      const router = useRouter();
      const route = useRoute();
      let content = ref("");
      let route_name = computed(() => route.name);
      const refresh = inject("reload");
      const post_a_post = () => {
        console.log(content.value);
        if (content.value === "") {
          $('#btn-blank').click();
          return false;
        }
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
              if (route_name.value !== "myspace_posts" && route_name.value !== "myspace_index") {
                router.push({ name: "myspace_posts" });
              } else {
                refresh();
              }
            }
          },
        });
      };
      return {
        content,
        post_a_post,
      };
    },
  };
  </script>
  
  <style scoped>
  .edit-filed {
    margin-top: 10px;
  }
  
  button:hover {
    scale: 1.1;
  }
  </style>
  