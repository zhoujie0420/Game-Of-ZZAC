<template>
  <div class="box container">
    <div class="pre-box">
      <h1>Sign In</h1>
      <p>JOIN US!</p>
      <div class="d-grid gap-2 col-6 mx-auto">
        <button type="button" class="btn btn-outline-info btn-lg btn-location" @click="loginmanner = 1">账号密码登录</button>
        <button type="button" class="btn btn-outline-info btn-lg btn-location" @click="loginmanner = 2">邮箱登录</button>
        <button type="button" class="btn btn-outline-info btn-lg btn-location" @click="loginmanner = 3">扫码登录</button>
      </div>
  
    </div>

    <div class="select-login">

    </div>

    <div v-if="loginmanner === 1" class="login-form" >
      <div class="title-box">
        <h1 class="form-h1-text bounce-top">账号密码登录</h1>
      </div>
      
      <div class="input-box">
        <input v-model="username" type="text" placeholder="用户名" />
        <input v-model="password" type="password" placeholder="密码" />
        
      </div>
      <div class="login-error-msg">{{ error_message }}</div>
      <div class="btn-box">
        <button class="newbutton" @click="login()">登录</button>
      </div>
    </div>

    <div v-if="loginmanner === 2" class="login-form" >
      <div class="title-box">
        <h1 class="form-h1-text bounce-top">邮箱登录</h1>
      </div>
      <div class="login-error-msg">{{ email_error_message }}</div>                                  
      <div class="input-box">
        <input v-model=" email " type="text" placeholder="邮箱" />
        <input v-model="code" type="code" placeholder="验证码" />

        <button class="newbutton" @click="getcode()">获取验证码</button>
      </div>
    <br>
      <div class="btn-box">
        <button class="newbutton" @click="emaillogin()">登录</button>
      </div>
    </div>

    <div v-if="loginmanner === 3" class="login-form" >
      <div class="title-box">
        <h1 class="form-h1-text bounce-top">扫码登录</h1>
      </div>
      <div class="login-error-msg">{{ login_error_msg }}</div>
      <div class="input-box">
        <input v-model="login_username" type="text" placeholder="用户名" />
        <input v-model="login_password" type="password" placeholder="密码" />
      </div>
      <div class="btn-box">
        <button class="newbutton" @click="login()">登录</button>
      </div>
    </div>
  </div>

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
        
          url: "https://zzac.online/api/utils/email/",
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
button {
  width: 100%;
}

div.error-message {
  color: red;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.register-error-msg {
  position: absolute;
  top: 40%;
  font-size: 20px;
  color: #c3404b;
  writing-mode: tb-rl;
  font-family: cursive;
}

.login-error-msg {
  position: absolute;
  top: 40%;
  /* left: 69%; */
  writing-mode: tb-rl;
  font-size: 20px;
  color: #c3404b;
  font-family: cursive;
}

.form-h1-text {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 去除input的轮廓 */
input {
  outline: none;
}

.box {
  height: 100%;
}

.box {
  overflow-x: hidden;
  display: flex;
  background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
}

span {
  position: absolute;
  z-index: 0;
  bottom: 0;
  border-radius: 50%;
  /* 径向渐变 */
  background: radial-gradient(circle at 72% 28%,
      #fff 3px,
      #ff7edf 8%,
      #5b5b5b,
      #aad7f9 100%);
  /* 动画 */
  animation: myMove 4s linear infinite;
}

@keyframes myMove {
  0% {
    transform: translateY(0%);
    opacity: 1;
  }

  50% {
    transform: translate(10%, -1000%);
  }

  75% {
    transform: translate(-20%, -1200%);
  }

  99% {
    opacity: 0.9;
  }

  100% {
    transform: translateY(-1800%) scale(1.5);
    opacity: 0;
  }
}

.box {
  width: 1050px;
  height: 600px;
  display: flex;
  position: relative;
  top: 50px;
  z-index: 2;
  margin: auto;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: 2px 1px 19px rgba(0, 0, 0, 0.1);
}

.pre-box {
  width: calc(1050px / 2);
  height: 100%;
  position: absolute;
  left: 0;
  top: 0;
  z-index: 99;
  border-radius: 4px;
  background-color: #edd4dc;
  box-shadow: 2px 1px 19px rgba(0, 0, 0, 0.1);
  transition: 0.5s ease-in-out;
}

.pre-box h1 {
  margin-top: 150px;
  text-align: center;
  letter-spacing: 5px;
  color: white;
  user-select: none;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.pre-box p {
  height: 30px;
  line-height: 30px;
  text-align: center;
  margin: 20px 0;
  user-select: none;
  font-weight: bold;
  color: white;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.img-box {
  width: 200px;
  height: 200px;
  margin: 20px auto;
  border-radius: 50%;
  user-select: none;
  overflow: hidden;
  box-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.img-box img {
  width: 100%;
  transition: 0.5s;
}

.login-form,
.select-login {
  flex: 1;
  height: 100%;
}

.title-box {
  height: 300px;
  line-height: 500px;
}

.title-box h1 {
  text-align: center;
  color: white;
  user-select: none;
  letter-spacing: 5px;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.input-box {
  display: flex;
  flex-direction: column;
  align-items: center;
}

input {
  width: 60%;
  height: 40px;
  margin-bottom: 20px;
  text-indent: 10px;
  border: 1px solid #fff;
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 120px;
  backdrop-filter: blur(10px);
}

input:focus::placeholder {
  opacity: 0;
}

h1 {
  display: block;
  font-size: 2em;
  margin-block-start: 0.67em;
  margin-block-end: 0.67em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  font-weight: bold;
}

.btn-box {
  display: flex;
  justify-content: center;
}

.newbutton {
  width: 100px;
  height: 30px;
  margin: 0 7px;
  line-height: 30px;
  border: none;
  border-radius: 4px;
  background-color: #69b3f0;
  color: white;
}

button:hover {
  cursor: pointer;
  opacity: 0.8;
  -webkit-animation: wobble-hor-bottom 0.8s both;
  animation: wobble-hor-bottom 0.8s both;
}

.btn-box p {
  height: 30px;
  line-height: 30px;
  user-select: none;
  font-size: 14px;
  color: #426277;
}

.btn-box p:hover {
  cursor: pointer;
  border-bottom: 1px solid #426277;
}

.bounce-top {
  -webkit-animation: bounce-top 0.9s both;
  animation: bounce-top 0.9s both;
}

@-webkit-keyframes bounce-top {
  0% {
    -webkit-transform: translateY(-45px);
    transform: translateY(-45px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
    opacity: 1;
  }

  24% {
    opacity: 1;
  }

  40% {
    -webkit-transform: translateY(-24px);
    transform: translateY(-24px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }

  65% {
    -webkit-transform: translateY(-12px);
    transform: translateY(-12px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }

  82% {
    -webkit-transform: translateY(-6px);
    transform: translateY(-6px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }

  93% {
    -webkit-transform: translateY(-4px);
    transform: translateY(-4px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }

  25%,
  55%,
  75%,
  87% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }

  100% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
    opacity: 1;
  }
}

@keyframes bounce-top {
  0% {
    -webkit-transform: translateY(-45px);
    transform: translateY(-45px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
    opacity: 1;
  }

  24% {
    opacity: 1;
  }

  40% {
    -webkit-transform: translateY(-24px);
    transform: translateY(-24px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }

  65% {
    -webkit-transform: translateY(-12px);
    transform: translateY(-12px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }

  82% {
    -webkit-transform: translateY(-6px);
    transform: translateY(-6px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }

  93% {
    -webkit-transform: translateY(-4px);
    transform: translateY(-4px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }

  25%,
  55%,
  75%,
  87% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }

  100% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
    opacity: 1;
  }
}

@-webkit-keyframes wobble-hor-bottom {

  0%,
  100% {
    -webkit-transform: translateX(0%);
    transform: translateX(0%);
    -webkit-transform-origin: 50% 50%;
    transform-origin: 50% 50%;
  }

  15% {
    -webkit-transform: translateX(-30px) rotate(-6deg);
    transform: translateX(-30px) rotate(-6deg);
  }

  30% {
    -webkit-transform: translateX(15px) rotate(6deg);
    transform: translateX(15px) rotate(6deg);
  }

  45% {
    -webkit-transform: translateX(-15px) rotate(-3.6deg);
    transform: translateX(-15px) rotate(-3.6deg);
  }

  60% {
    -webkit-transform: translateX(9px) rotate(2.4deg);
    transform: translateX(9px) rotate(2.4deg);
  }

  75% {
    -webkit-transform: translateX(-6px) rotate(-1.2deg);
    transform: translateX(-6px) rotate(-1.2deg);
  }
}

@keyframes wobble-hor-bottom {

  0%,
  100% {
    -webkit-transform: translateX(0%);
    transform: translateX(0%);
    -webkit-transform-origin: 50% 50%;
    transform-origin: 50% 50%;
  }

  15% {
    -webkit-transform: translateX(-30px) rotate(-6deg);
    transform: translateX(-30px) rotate(-6deg);
  }

  30% {
    -webkit-transform: translateX(15px) rotate(6deg);
    transform: translateX(15px) rotate(6deg);
  }

  45% {
    -webkit-transform: translateX(-15px) rotate(-3.6deg);
    transform: translateX(-15px) rotate(-3.6deg);
  }

  60% {
    -webkit-transform: translateX(9px) rotate(2.4deg);
    transform: translateX(9px) rotate(2.4deg);
  }

  75% {
    -webkit-transform: translateX(-6px) rotate(-1.2deg);
    transform: translateX(-6px) rotate(-1.2deg);
  }
}

.thrid-party-login {
  margin-top: 2vh;
  text-align: center;
  font-size: 14px;
  user-select: none;
}

.thrid-party-login>p>img {
  height: 4vh;
  margin-right: 1vw;
  cursor: pointer;
}
.location {
  margin-left: 36%;
  justify-content: center;
}
.btn-location {
  margin-top: 40px;
}
</style>