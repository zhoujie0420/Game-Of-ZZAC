  <template>
     <ContentField class="body-base" >
    <div class="row">
      <div
        class="alert alert-info alert-dismissible fade show col-11"
        role="alert"
        style="margin-left: 3.5vw"
      >
        Welcome to <strong>Game Of ZZAC</strong>
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
        <div class="desc-title">What is Game Of ZZAC？</div>

        <div class="desc-simple-content">
          &nbsp;&nbsp;&nbsp;&nbsp;Game of ZZAC是<span>在线的程序对抗平台</span>，玩家可以根据已有的游戏规则进行比赛一决胜负!
        </div>
        <hr />
        <div class="desc-title">How to use Game of ZZAC？</div>
        <div class="desc-how-ans">
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;1.
            <router-link :to="{ name: 'user_account_register' }">注册</router-link> Super
            Of Bots 账户(如果已有账户则跳过此步骤,直接对战)
          </div>
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;2. 按照游戏说明编写自己的<span>AI程序</span>或<span>亲自出马</span>。
          </div>
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;3. 点击右上角账户名，进入个人空间点击我的Bots，创建 Bot 并上传程序。
          </div>
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;4. 进入游戏页面，选择游戏，与他人或其他 Bot 开始对战。
          </div>
        </div>
        <hr />

        <div class="desc-title">Provide Bug Or Get in touch!</div>
        <div class="how-fix-bug">
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;由于还在开发中，Game of ZZAC 可能有着各种奇怪的问题，遇到这些问题时或有更好的建议，你可以通过<a href="mailto:f1747364257@qq.com">邮箱</a>联系作者。
          </div>
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-github" viewBox="0 0 16 16"><path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.012 8.012 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
            <a href="https://github.com/zhoujie0420" class="bi bi-github">GitHub</a>
          </div>
        </div>
      </div>
    </div>
  </ContentField>

  </template>
  
  <script>
import ContentField from '../../components/ContentField'
export default {
  components: {
    ContentField,
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
  </style>
  