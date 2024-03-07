<template>
  <ContentField>
    <table class="table table-striped table-hover" style="text-align: center;">
      <thead>
      <tr>
        <th>{{ $store.state.user.role === 1 ? '医生姓名' : '患者姓名' }}</th>
        <th>更新时间</th>
        <th>签约文档</th>
        <th>状态</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="record in signRecords" :key="record.id">
        <td>{{ record.otherName }}</td>
        <td>{{ record.createTime }}</td>
        <td @click="redirectToUrl(record.docUrl)">
          {{ record.docUrl === null ? '待上传' : '点击查看' }}
        </td>
        <td>{{ record.status === 1 ? '已完成' : '未完成' }}</td>
        <td>
          <div class="custom-file">
            <label :for="'customFile_' + record.id" class="custom-file-upload">
              <input type="file" @change="upload(record.id,record.doctorId, $event)" :id="'customFile_' + record.id" style="display: none;">
              上传文件
            </label>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
  </ContentField>
</template>

<script setup>
import ContentField from '../components/ContentField.vue';
import {ref} from "vue";
import store from "@/store";

const signRecords = ref([]); // 创建响应式的userInfos
import $ from 'jquery';

getSigns()

function upload(id,doctorId,e) {
  let file = e.target.files[0];
  let param = new FormData();
  param.append("file", file);
  $.ajax({
    url: "http://127.0.0.1:3000/api/aliyun/oss/upload",
    type: "post",
    data: param,
    processData: false,
    contentType: false,
    headers: {
      "Authorization": "Bearer " + store.state.user.token,
    },
    success(resp) {
      console.log("record =====" + id);
      console.log(resp);
      updateDocUrl(id,doctorId, resp);
    }
  })
}

function updateDocUrl(id , doctorId,url) {
  $.ajax({
    url: `http://127.0.0.1:3000/api/signing/updateSigning/`,
    type: "post",
    data:
        {
          "id": id,
          "docUrl": url,
          "doctorId": doctorId,
        },
    headers: {
      Authorization: "Bearer " + store.state.user.token,
    },
    success(resp) {
      if (resp.code === 200) {
        getSigns()
      }
    },
  });
}

function redirectToUrl(url) {
  if (url !== null) {
    window.open(url, '_blank');
  }
}
function getSigns() {
  $.ajax({
    url: `http://127.0.0.1:3000/api/signing/getSigning/`,
    type: "post",
    headers: {
      Authorization: "Bearer " + store.state.user.token,
    },
    success(resp) {
      if (resp.code === 200) {
        console.log(resp.data);
        signRecords.value = resp.data; // 更新userInfos的值
      } else {
        console.log(resp.message);
      }
    },
  });
}
</script>

<style scoped>
.custom-file-upload {
  display: inline-block;
  padding: 2px 15px;
  border: 2px solid #ccc;
  border-radius: 5px;
  cursor: pointer;
}
</style>