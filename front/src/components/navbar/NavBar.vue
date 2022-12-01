<template>    
      <van-tabbar route>
        <van-tabbar-item replace to="/" icon="home-o">主页</van-tabbar-item>
        <van-tabbar-item replace to="/record/" icon="coupon-o">对战列表</van-tabbar-item>
        <van-tabbar-item replace to="/ranklist/" icon="bar-chart-o">排行</van-tabbar-item>
        <van-tabbar-item replace to="/dynamics/" icon="description">讨论</van-tabbar-item>
        <van-tabbar-item replace to="/myspace/" icon="user-o">我的</van-tabbar-item>
      </van-tabbar>


  </template>
  
  <script>
  import { useRoute } from 'vue-router'
  import { computed } from 'vue'
  import { useStore } from 'vuex';
  import { ref } from 'vue';


  export default {
      setup() {
        
          const store = useStore();
          const route = useRoute();
          let route_name = computed(() => route.name)
          const active = ref(0);
          const logout = () => {
            store.dispatch("logout");
          }
  
          return {
              route_name,
              logout,
              active
          }
      }
  }
  </script>
  
  <style scoped>
  
  </style>