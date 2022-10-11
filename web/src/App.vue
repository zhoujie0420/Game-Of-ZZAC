<template>
  <NavBar />
  <router-view></router-view>
</template>

<script>
import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import $ from "jquery"
export default {
  components: {
    NavBar
  },
  setup(){
    $.ajax({
      url:"http://127.0.0.1:3000/user/account/token/",
      type :"post",
      data:{
        username : 'z',
        password : 'z',
      },
      success(resp){
        console.log(resp);
      },
      error(resp){
        console.log(resp);
      }
    });
    
   $.ajax({
    url:"http://127.0.0.1:3000/user/account/info/",
    type:"get",
    headers :{
      Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMGIyOWRkZmVhOTY0MzhkYjg3NDQ2NzFkYWY3ZTg0OSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2NTQ2MzA5OCwiZXhwIjoxNjY2NjcyNjk4fQ.6So-Of3krMqCla04ThEA0wou8XqDraod0gH-L9xSVp0",
    },
    success(resp){
      console.log(resp);
    },
    error(resp){
      console.log(resp);
    }
   })
  }
  
}
</script>

<style>
body {
  background-image: url("@/assets/images/background.png");
  background-size: cover;
}
</style>
