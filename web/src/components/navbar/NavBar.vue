<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <router-link class="navbar-brand" :to="{name: 'home'}">Hms家庭医生</router-link>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link :class="route_name === 'signing_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'signing_index'}">签约</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name === 'consultations_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'consultations_index'}">医疗记录</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name === 'reserve_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'reserve_index'}">预约挂号</router-link>
          </li>
        </ul>
        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              {{ $store.state.user.username }} / {{ $store.state.user.role === 1 ? '患者' : '医生'}}
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li>
  
                  <router-link class="dropdown-item" :to="{name: 'myspace_index'}">我的空间</router-link>
              </li>
              <li><hr class="dropdown-divider"></li>
              <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
            </ul>
          </li>
        </ul>
        <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
          <li class="nav-item">
            <router-link class="nav-link" :to="{name: 'user_account_login' }" role="button">
              登录
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
              注册
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  </template>
  
  <script>
  import { useRoute } from 'vue-router'
  import { computed } from 'vue'
  import { useStore } from 'vuex';
  
  export default {
      setup() {
          const store = useStore();
          const route = useRoute();
          let route_name = computed(() => route.name)
          const logout = () => {
            store.dispatch("logout");
          }
  
          return {
              route_name,
              logout
          }
      }
  }
  </script>
  
  <style scoped>
  
  </style>