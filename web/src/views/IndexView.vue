  <template>
     <ContentField class="body-base" >
    <div class="row">
      <div
        class="alert alert-info alert-dismissible fade show col-11"
        role="alert"
        style="margin-left: 3.5vw"
      >
        Hms家庭医生团队提醒您：请勿轻信他人的信息，谨防上当受骗！
        <button
          type="button"
          class="btn-close small"
          data-bs-dismiss="alert"
          aria-label="Close"
        ></button>
      </div>
    </div>
    <div class="row">
      <div class="col-11 desc">
        <div class="desc-title">什么是Hms家庭医生</div>

        <div class="desc-simple-content">
          &nbsp;&nbsp;&nbsp;&nbsp;帮助医生和患者更好的沟通，提供更好的医疗服务。
        </div>
        <hr />
        <div class="desc-title">如何使用Hms家庭医生</div>
        <div class="desc-how-ans">
          &nbsp;&nbsp;&nbsp;&nbsp;Hms家庭医生是一个在线医疗服务平台，您可以通过注册账号，登录后，选择和需要的医生进行签约，签约完成后，医生可以管理你的病例，为你提供更好的医疗服务。
        </div>
        <hr />

      </div>
    </div>
  </ContentField>

  </template>
  
  <script>
import ContentField from '../components/ContentField.vue'
import router from "@/router";
import { useStore } from "vuex";
export default {
  components: {
    ContentField,
  },
  setup(){
    const store = useStore();
    
    const jwt_token = localStorage.getItem("jwt_token");
        if (jwt_token) {
            store.commit("updateToken", jwt_token);
            store.dispatch("getInfo", {
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
  a {
    font-weight: bold;
    text-decoration: none;
    color: rgb(132, 183, 200);
  }
  .desc {
    height: 64vh;
    margin-top: 1vh;
    /* margin-left: 4vh; */
    margin-left: 3.4vw;
    border-radius: 2vh;
    border: 1px solid rgb(211, 217, 224);
    box-shadow: 2px 2px 10px lightgray;
  }
  .desc-title {
    text-align: center;
    margin-top: 1vh;
    font-size: 24px;
    font-weight: bold;
  }
  .desc-simple-content {
    font-size: 16px;
    margin-top: 2vh;
  }
  .desc-how {
    text-align: center;
    margin-top: 1vh;
    font-size: 24px;
    font-weight: bold;
  }
  .desc-how-ans {
    font-size: 16px;
    margin-top: 2vh;
  }
  .desc-how-ans > div {
    padding: 0.3vh;
  }
  span {
    font-weight: bold;
  }
  .how-fix-bug {
    margin-top: 2vh;
    font-size: 16px;
  }
  .how-fix-bug > div {
    padding-top: 1vh;
  }


  .information-title {
  color: #19d3ea;
  font-size: 18px;
  cursor: pointer;  /*鼠标悬停变小手*/
  width: 100%;
  }
  </style>
  