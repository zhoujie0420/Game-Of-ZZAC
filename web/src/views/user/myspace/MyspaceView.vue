<template>
  <div class="container body-base">
    <div class="row">
      <div class="col-xs-4 col-sm-4 col-md-3">
        <UserProfileInfo />
        <UserProfileWrite />
      </div>
      <div class="col-xs-8 col-sm-8 col-md-9">
        <div class="card" style="margin-top: 20px">
          <MyspaceNavBar />
          <div class="card-body router-border">
            <router-view v-if="isRouterAlive" />
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>

import UserProfileWrite from '../../../components/user/UserProfileWrite.vue'
import UserProfileInfo from '../../../components/user/UserProfileInfo.vue'
import MyspaceNavBar from '../../../components/navbar/MyspaceNavBar'
import { ref, nextTick, provide } from "vue";

export default {
  components: {
    MyspaceNavBar,
    UserProfileWrite,
    UserProfileInfo,
  },
  setup () {
    const isRouterAlive = ref(true);

 


    const reload = () => {
      isRouterAlive.value = false;
      nextTick(() => {
        isRouterAlive.value = true;
      });
    };

    provide("reload", reload);
    return {
      isRouterAlive,
    };
  }
}

</script>

<style scoped>
div.error-message {
    color: red;
}

.user-username {
    color: rgb(14, 1, 2);
    text-align: center;
}
</style>