  <template>

          <van-row justify="center">
            <h1>Game Of ZZAC</h1> 
          </van-row>



          <van-row >
            <h2>What is it？</h2>
            <div class="desc-simple-content">
          &nbsp;&nbsp;&nbsp;&nbsp;Game of ZZAC是<span>在线的程序对抗平台</span>，玩家可以根据已有的游戏规则进行比赛一决胜负!
            </div>
          </van-row>
          <van-row >
            <h2>Operating mechanism？</h2>
            <div class="desc-simple-content">
          &nbsp;&nbsp;&nbsp;&nbsp;游戏属于回合制，需要用户或Bot进行每一回合的操作，当云端接收到对局中两名用户的操作时，返回对局信息， 如果操作没有使得对局结束， 就会循环往复的进行读取用户或Bot操作， 直到对局结束。
            </div>
          </van-row>
          <van-row >
            <h2>Post？</h2>
            <div class="desc-simple-content">
          &nbsp;&nbsp;&nbsp;&nbsp;为了方便更好的交流AI代码，我们他一共了一个良好的讨论，可在讨论分享中查看，欢迎各位Boter讨论分享
            </div>
          </van-row>
          <van-row >
            <h2>Provide Bug Or Get in touch!</h2>
            <div class="desc-simple-content">
            &nbsp;&nbsp;&nbsp;&nbsp;由于还在开发中，Game of ZZAC 可能有着各种奇怪的问题，遇到这些问题时或有更好的建议，你可以通过邮箱联系作者。
            &nbsp;&nbsp;&nbsp;&nbsp;<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-github" viewBox="0 0 16 16"><path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.012 8.012 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
            <a href="https://github.com/zhoujie0420" class="bi bi-github">GitHub</a>
            </div>
          </van-row>

  


  </template>
  
  <script>

import router from "@/router/index";
import { useStore } from "vuex";
export default {
  components: {

  },
  setup(){
    const store = useStore();
    
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

  }
}
  </script>
  
  <style scoped>

  .my-swipe .van-swipe-item {
    color: #fff;
    font-size: 20px;
    line-height: 150px;
    text-align: center;
    background-color: #39a9ed;
  }
</style>