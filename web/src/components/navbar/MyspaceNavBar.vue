<template>
    <ul class="nav nav-tabs" style="margin-top: 15px; margin-left: 20px">
      <li class="nav-item">
        <router-link
          :class="route_name == 'myspace_posts' ? 'nav-link active' : 'nav-link'"
          :to="{ name: 'myspace_posts' }"
          >动态</router-link
        >
      </li>
  
      <li class="nav-item">
        <router-link
          :class="route_name == 'myspace_bots' ? 'nav-link active' : 'nav-link'"
          :to="{ name: 'myspace_bots' }"
          >我的Bots</router-link
        >
      </li>
    </ul>
  </template>
  
  <script>
  import { useRoute } from 'vue-router'
  import { computed } from 'vue'
  export default {
    name: "MyspaceNavBar",
    setup () {
      const route = useRoute();
      let route_name = computed(() => route.name);
      return {
        route_name,
      }
    },
  }
  </script>
  
  <style scoped>
  .nav-link {
    color: #337ab7;
  }
  </style>
  