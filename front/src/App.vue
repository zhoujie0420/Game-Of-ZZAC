<template>
  <router-view />
  <NavBar />

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
  background-image: url("https://img.zmtc.com/2019/1126/20191126041135841.jpg"); 



  background-size: cover;
}
</style>
