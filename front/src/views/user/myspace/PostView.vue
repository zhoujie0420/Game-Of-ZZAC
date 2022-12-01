<template>
  <UserProfilePosts
    :posts="posts"
    :isme="true"
    @delete_a_post="delete_a_post"
  ></UserProfilePosts>
</template>

<script>
import UserProfilePosts from '../../../components/user/UserProfilePosts.vue'
import { reactive } from 'vue'
import { useStore } from 'vuex'
import $ from 'jquery'
export default {
  components: {
    UserProfilePosts,
  },
  setup () {
    const store = useStore();
    const posts = reactive({});
    const refreshPost = () => {
      $.ajax({
        url: "https://zzac.online/api/user/post/getlistbyid",
        type: "post",
        data: {
          id: store.state.user.id,
        },
        headers: {
          "Authorization": "Bearer " + store.state.user.token,
        },
        success (resp) {
            posts.posts = resp;
        }
      })
    }
    const delete_a_post = () => {
      refreshPost();
    };
    refreshPost();

    return {
      delete_a_post,
      posts,
    }
  }
}
</script>

<style scoped>
.card-header-name {
  font-weight: bold;
  font-size: 24px;
}
</style>
