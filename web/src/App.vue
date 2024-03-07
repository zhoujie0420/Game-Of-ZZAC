<template>
  <NavBar />
  <router-view></router-view>
</template>

<script>
import NavBar from './components/navbar/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

export default {
  components: {
    NavBar
  },
  
}
</script>

<style>
body {
  background-color: #8ad4e1;
  background-size: cover;
}
</style>
