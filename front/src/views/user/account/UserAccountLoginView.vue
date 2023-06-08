<template>

<van-form class="ct" @submit="login()">
  <van-cell-group inset>
    <van-field
      v-model="username"
      name="用户名"
      label="用户名"
      placeholder="用户名"
      :rules="[{ required: true, message: '请填写用户名' }]"
    />
    <van-field
      v-model="password"
      type="password"
      name="密码"
      label="密码"
      placeholder="密码"
      :rules="[{ required: true, message: '请填写密码' }]"
    />
  </van-cell-group>
  <div class="login-error-msg">{{ error_message }}</div>
  <div style="margin: 16px;">
    <van-button round block type="primary" native-type="submit">
      登录
    </van-button>
    <router-link  class="col"     :to="{name: 'user_account_register'}" role="button">
              还没有账号，去注册
    </router-link>
  </div>
</van-form>


</template>

<script>

import { useStore } from 'vuex'
import { ref } from 'vue'
import router from '../../../router/index'
import $ from 'jquery'



export default {
  components: {

},
  setup() {
    const store = useStore();
    let username = ref('');
    let password = ref('');
    let email = ref('');
    let code = ref('');
    let error_message = ref('');
    let loginmanner = ref(1);
    let email_error_message = ref('');



    const jwt_token = localStorage.getItem("jwt_token");
    if (jwt_token) {
      store.commit("updateToken", jwt_token);
      store.dispatch("getinfo", {
        success() {
          router.push({ name: "home" });
          store.commit("updatePullingInfo", false);
        },
        error() {
          store.commit("updatePullingInfo", false);
        }
      })
    } else {
      store.commit("updatePullingInfo", false);
    }

    const login = () => {
      console.log(username.value)
      error_message.value = "";
      store.dispatch("login", {
        username: username.value,
        password: password.value,
        success() {
          store.dispatch("getinfo", {
            success() {
              router.push({ name: 'home' });
            }
          })
        },
        error() {
          error_message.value = "用户名或密码错误";
        }
      })
    }

    const emaillogin = () => {
      error_message.value = "";
      store.dispatch("emaillogin", {
        email: email.value,
        code: code.value,
        success() {
          store.dispatch("getinfo", {
            success() {
              router.push({ name: 'home' });
            }
          })
        },
        error() {
          error_message.value = "验证码错误";
        }
      })
    }

    const getcode = () => {
      console.log(111)
      console.log(email.value)
      $.ajax({
        
          url: "http://127.0.0.1:3000/api/utils/email/",
          type: "get",
          data: {
              email: email.value,
          },
          success(resp) {
              if (resp.error_message === "success") {
                  console.log(resp);
              } else {
                  error_message.value = resp.error_message;
              }
          },
      })
    }



    return {
      username,
      password,
      email,
      code,
      error_message,
      login,
      loginmanner,
      email_error_message,
      getcode,
      emaillogin,
    }
  }
}
</script>

<style scoped>
.ct {
  margin-top: 40%;

}
.col {
  margin-left: 30%;
  color: black;
}
.login-error-msg {

top: 40%;

font-size: 20px;
color: #c3404b;

}
</style>