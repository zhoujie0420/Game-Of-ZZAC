<template>
<div class="container">
    <div class="card">
      <div class="card-header">
        <span class="card-header-name">我的Bots</span>
        <button
          class="add-bot float-end"
          data-bs-toggle="modal"
          data-bs-target="#add-bot-button"
        >
          <span>+ Bot</span>
        </button>
        <!-- Modal -->
        <div class="modal fade" id="add-bot-button" tabindex="-1">
          <div class="modal-dialog modal-lg">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">Create Bot</h5>
                <button
                  type="button"
                  class="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                ></button>
              </div>
              <div class="modal-body">
                <div class="mb-2">
                  <label for="bot-title" class="form-label">Bot Title</label>
                  <input
                    v-model="bot.title"
                    type="text"
                    class="form-control"
                    id="bot-title"
                    placeholder="名称"
                  />
                </div>
                <div class="mb-2">
                  <label for="description" class="form-label"
                    >Description</label
                  >
                  <textarea
                    v-model="bot.description"
                    class="form-control"
                    id="description"
                    placeholder="简介"
                    rows="2"
                  ></textarea>
                </div>

                <div class="mb-2">
                  <label for="code" class="form-label">Code</label>
                  <a class="code-template"  target="_blank"   href="https://www.yuque.com/zhoujie-ftecs/dktge5/vck9dr?# 《代码模板》">代码模板</a>
                  
                  
              
                  <VAceEditor
                    v-model:value="bot.content"
                    lang="c_cpp"
                    theme="textmate"
                    style="height: 235px"
                    :options="{
                      fontSize: 16,
                      enableBasicAutocompletion: true,
                      enableSnippets: true,
                      enableLiveAutocompletion: true,
                      showPrintMargin: false,
                      highlightActiveLine: true,
                    }"
                  />
                </div>
              </div>

              <div class="modal-footer">
                <div class="error-msg">{{ bot.error_message }}</div>
                <button
                  type="button"
                  class="btn btn-secondary"
                  data-bs-dismiss="modal"
                >
                  取消
                </button>
                <button
                  type="button"
                  class="btn btn-primary"
                  @click="add_bot()"
                >
                  提交
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="card-body">
        <table class="table" style="text-align: center">
          <thead class="table-dark bot-th">
            <tr>
              <th>名称</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="bot in bots" :key="bot.id" class="bot-tr">
              <td>{{ bot.title }}</td>
              <td>{{ bot.createtime }}</td>
              <td>
                <button
                  class="update-bot"
                  data-bs-toggle="modal"
                  :data-bs-target="'#update-bot-modal-' + bot.id"
                  :id="'update_button' + bot.id"
                >
                  <span>修改</span>
                </button>
                <button
                  class="remove-bot"
                  data-bs-toggle="modal"
                  data-bs-target="#delete_bot"
                  @click="remove_bot(bot.id)"
                >
                  <span style="color: white">删除</span>
                </button>
         
                <!-- 修改model -->
                <div
                  class="modal fade"
                  :id="'update-bot-modal-' + bot.id"
                  tabindex="-1"
                >
                  <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title">Update Bot</h5>
                        <button
                          type="button"
                          class="btn-close"
                          data-bs-dismiss="modal"
                          aria-label="Close"
                        ></button>
                      </div>
                      <div class="modal-body">
                        <div class="mb-2">
                          <label for="bot-title" class="form-label"
                            >Bot Title</label
                          >
                          <input
                            v-model="bot.title"
                            type="text"
                            class="form-control"
                            id="bot-title"
                            placeholder="名称"
                          />
                        </div>
                        <div class="mb-2">
                          <label for="description" class="form-label"
                            >Description</label
                          >
                          <textarea
                            v-model="bot.description"
                            class="form-control"
                            id="description"
                            placeholder="简介"
                            rows="2"
                          ></textarea>
                  
                          <label for="code" class="form-label">Code</label>
                       
                          <VAceEditor
                            @init="editorInit"
                            v-model:value="bot.content"
                            :options="{
                              fontSize: 16,
                              enableBasicAutocompletion: true,
                              enableSnippets: true,
                              enableLiveAutocompletion: true,
                              showPrintMargin: false,
                              highlightActiveLine: true,
                            }"
                            lang="c_cpp"
                            theme="textmate"
                            style="height: 235px"
                          />
                        </div>
                      </div>

                      <div class="modal-footer">
                        <div class="error-msg">{{ bot.error_message }}</div>
                        <button
                          type="button"
                          class="btn btn-secondary"
                          data-bs-dismiss="modal"
                        >
                          取消
                        </button>
                        <button
                          type="button"
                          class="btn btn-primary"
                          @click="update_bot(bot)"
                        >
                          提交
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>


<script>
import { ref, reactive } from 'vue'
import $ from 'jquery'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';


export default {
    components: {
        VAceEditor
    },
    setup() {
        ace.config.set("basePath", "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

        const store = useStore();
        let bots = ref([]);


        const bot = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        });

        

        const confirm_bot_id = (id) => {
            bot.id.value = id;
        }


        const update_bot = (bot) => {
            bot.error_message = "";
            console.bot;
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        bot.title = "";
                        bot.description = "";
                        bot.content = "";
                        Modal.getInstance('#update-bot-modal-' + bot.id).hide();

                    } else {
                        bot.error_message = resp.error_message;
                        setTimeout(() => { bot.error_message = ""; }, 2000);
                        Modal.getInstance('#update-bot-modal-' + bot.id).show();
                    }
                    refresh_bots();
                }
            })
        }


        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
    
                }
            })
        }

        refresh_bots();

        const add_bot = () => {
            bot.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/add/",
                type: "post",
                data: {
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        bot.title = "";
                        bot.description = "";
                        bot.content = "";
                        Modal.getInstance("#add-bot-button").hide();
                        refresh_bots();
                    } else {
                        bot.error_message = resp.error_message;
                    }
                }
            })
        }

        const remove_bot = (bot) => {
          $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/remove/",
                type: "post",
                data: {
                    id: bot,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
 
                    if (resp.error_message === "success") {
                        refresh_bots();
                    }
                }
            })
        }


        function editorInit() {  //代码补全

            require("ace-builds/src-noconflict/ext-language_tools");
            require("ace-builds/src-noconflict/snippets/sql");
            require("ace-builds/src-noconflict/snippets/c_cpp");
            require("ace-builds/src-noconflict/snippets/java");
            // require("ace-builds/src-noconflict/mode-sql");
            require("ace-builds/src-noconflict/mode-c_cpp");
            require("ace-builds/src-noconflict/mode-java");
            //require("ace-builds/src-noconflict/theme-monokai");
            require("ace-builds/src-noconflict/mode-html");
            require("ace-builds/src-noconflict/mode-html_elixir");
            require("ace-builds/src-noconflict/mode-html_ruby");
            require("ace-builds/src-noconflict/mode-javascript");
            require("ace-builds/src-noconflict/mode-python");
            require("ace-builds/src-noconflict/snippets/less");
            //require("ace-builds/src-noconflict/theme-chrome");
            require("ace-builds/src-noconflict/ext-static_highlight");
            require("ace-builds/src-noconflict/ext-beautify");
        }



        return {
            bots,
            bot,
            add_bot,
            update_bot,
            remove_bot,
            editorInit,
            confirm_bot_id,
        }
    }
}
</script>

<style scoped>
.card-header-name {
  font-weight: bold;
  font-size: 24px;
  margin: 0 auto;
}
.error-msg {
  margin-right: 20px;
  font-size: 16px;
  color: #c3404b;
  font-family: Verdana, Geneva, Tahoma, sans-serif;
}
.add-bot {
  width: 60px;
  border: 1px;
  border-radius: 5px;
  background-color: #409eff;
  margin-top: 5px;
  outline: none;
}
.update-bot {
  width: 60px;
  border: 1px;
  border-radius: 5px;
  background-color: #b7c5d2;
  outline: none;
}
.remove-bot {
  margin-left: 10px;
  width: 60px;
  border: 1px;
  border-radius: 5px;
  background-color: #dd3545;
  outline: none;
}
button > span {
  font-size: 16px;
  color: rgb(23, 18, 18);
}
button:hover {
  scale: 1.1;
}
.bot-tr:hover {
  background-color: #d7d9da;
}
.bot-th {
  font-family: Cambria, Cochin, Georgia, Times, "Times New Roman", serif;
}
.code-template {
  float: right;
  font-weight: bold;
  text-decoration: none;
  color: rgb(132, 183, 200);
}

</style>