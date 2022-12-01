<template>
  <van-form class="ct" @submit="register()">
  <van-cell-group inset>
    <van-field
       v-model="username" type="text" placeholder="用户名"
    />
    <van-field
    v-model="email" type="email" placeholder="邮箱" 
    />
    <van-field
    v-model="password" type="password" placeholder="密码"
    />
    <van-field
    v-model="confirmedPassword" type="password" placeholder="确认密码"
    />
  </van-cell-group>
  <div class="login-error-msg">{{ error_message }}</div>
  <div style="margin: 16px;">
    <van-button round block type="primary" native-type="submit">
      注册
    </van-button>
        <router-link  class="col"  :to="{name: 'user_account_login'}" role="button">
              已有账号，去登录
    </router-link>
  </div>
</van-form>

</template>

<script>
import { ref } from 'vue'
import router from '../../../router/index'
import $ from 'jquery'

export default {
    components: {

    },
    setup() {
        let username = ref('');
        let email = ref('');
        let password = ref('');
        let confirmedPassword = ref('');
        let error_message = ref('');

        const register = () => {
            $.ajax({
                url: "https://zzac.online/api/user/account/register/",
                type: "post",
                data: {
                    username: username.value,
                    email: email.value,
                    password: password.value,
                    confirmedPassword: confirmedPassword.value,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        router.push({name: "user_account_login"});
                    } else {
                        error_message.value = resp.error_message;
                    }
                },
            });
        }

        return {
            username,
            password,
            email,
            confirmedPassword,
            error_message,
            register,
        }
    }
}
</script>

<style scoped>
button {
  width: 100%;
}
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