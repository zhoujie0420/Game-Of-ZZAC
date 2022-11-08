<template>
    <div class="card" style="margin-top: 20px">
      <div class="card-body">
        <button
          hidden
          data-bs-toggle="modal"
          data-bs-target="#change_avatar"
          id="btn-blank-avatar"
        ></button>
        <p class="pic">
          <img
            :src="$store.state.user.photo"
            style="
              width: 100%;
              cursor: pointer;
              position: absolute;
              top: 0;
              left: 0;
              height: 100%;
            "

            @click="change_avatar()"
          />
        </p>
  
        <hr />
        <div class="info-username">
          {{ $store.state.user.username }}
        </div>

        <div
          class="modal fade"
          id="change_avatar"
          tabindex="-1"
          aria-labelledby="exampleModalLabel"
          aria-hidden="true"
        >
          <div class="modal-dialog modal-dialog-centered">
            <div
              class="modal-content"
              style="background-color: white; width: 500px; margin: 0 auto"
            >
              <div class="modal-header" style="font-weight: bold">
                Update Photo
              </div>
              <div class="modal-body avatar-info">
                <img
                  :src="$store.state.user.photo"
                  style="
                    width: 100px;
                    height: 100px;
                    border-radius: 50px;
                    margin-left: 15px;
                  "
                />
  
                <div class="avatar-info-input col-8" style="margin-left: 35px">
                  <textarea
                    v-model="avatar_url"
                    class="form-control"
                    rows="3"
                    id="edit"
                    placeholder="请输入头像地址"
                  />
                </div>
              </div>
              <div class="avatar-msg">{{ avatar_msg }}</div>
              <div class="modal-footer" style="margin: 0 auto">
                <button
                  type="button"
                  class="btn"
                  style="
                    background-color: #198754;
                    border-style: none;
                    color: white;
                  "
                  @click="update_avatar()"
                >
                  确认
                </button>
              </div>
            </div>
          </div>
        </div>
  

      </div>
    </div>
  </template>
  
  <script>
  import { ref } from "vue";
  import { Modal } from 'bootstrap/dist/js/bootstrap'
  import $ from 'jquery'
  import { useStore } from 'vuex'
  export default {
    setup () {
      const store = useStore();
      let avatar_msg = ref("");
      let avatar_url = ref("");
      const change_avatar = () => {
        $('#btn-blank-avatar').click();
      }
      const update_avatar = () => {

        $.ajax({
          url: "http://127.0.0.1:3000/api/user/account/photo",
          type: "post",
          data: {
            photo: avatar_url.value,
          },
          headers: {
            "Authorization": "Bearer " + store.state.user.token,
          },
          success (resp) {
  
            if (resp.error_message === "success") {
              store.commit("updatePhoto", avatar_url.value);
              avatar_url.value = "";
              Modal.getInstance('#change_avatar').hide();
            } else {
              console.log(resp.error_message);
              avatar_msg.value = resp.error_message;
              setTimeout(() => {
                avatar_msg.value = "";
              }, 2000);
            }
          },
        })
      }
      return {
        change_avatar,
        update_avatar,
        avatar_msg,
        avatar_url,
      };
    }
  }
  </script>
  
  <style scoped>
  .info-username {
    text-align: center;
    font-weight: bold;
  }
  .info-reputation {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    text-align: center;
    font-family: Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif;
    font-size: 13px;
  }
  .info-reputation-follow {
    width: 50%;
    cursor: pointer;
  }
  .info-reputation-fans {
    width: 50%;
    cursor: pointer;
  }
  .info-reputation-name {
    color: #99a2aa;
  }
  .info-reputation-cnt {
    color: #222222;
  }
  .info-reputation-follow:hover .info-reputation-name {
    color: #00a1d6;
  }
  .info-reputation-follow:hover .info-reputation-cnt {
    color: #00a1d6;
  }
  .info-reputation-fans:hover .info-reputation-name {
    color: #00a1d6;
  }
  .info-reputation-fans:hover .info-reputation-cnt {
    color: #00a1d6;
  }
  a {
    text-decoration: none;
  }
  .avatar-info {
    display: flex;
    align-items: center;
  }
  button:hover {
    scale: 1.1;
  }
  .avatar-msg {
    position: absolute;
    top: 65%;
    left: 34%;
    font-size: 14px;
    color: #c3404b;
  }
  .pic {
    position: relative;
    width: 100%; /*相当于100px*/
    padding-top: 100%; /*相当于100px.这是比较关键的一步，margin和padding是相对于其父元素的宽度的100%*/
  }
  </style>
  