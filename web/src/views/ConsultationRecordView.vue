<template>
  <ContentField>
    <table class="table table-striped table-hover" style="text-align: center;">
      <thead>
      <tr>
        <th>{{$store.state.user.role === 1 ? '医生姓名' : '患者姓名'}}</th>
        <th>处方</th>
        <th>诊断结果</th>
        <th>时间</th>
        <th>状态</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="record in records" :key="record.id">
        <td>{{ record.otherName }}</td>
        <td>{{ record.prescription }}</td>
        <td>{{ record.diagnosis }}</td>
        <td>{{ record.consultationDate }}</td>
        <td>{{ record.status === 1 ? '已完成' : '未完成'}}</td>
      </tr>
      </tbody>
    </table>
  </ContentField>
</template>

<script setup>
import ContentField from '../components/ContentField.vue';
import {ref} from "vue";
import store from "@/store";
const records = ref([]); // 创建响应式的userInfos
import $ from 'jquery';



getRecords()
function getRecords() {
  $.ajax({
    url: `http://127.0.0.1:3000/api/record/getRecords/`,
    type: "post",
    headers: {
      Authorization: "Bearer " + store.state.user.token,
    },
    success(resp) {
      if (resp.code === 200) {
        console.log(resp.data);
        records.value = resp.data; // 更新userInfos的值
      } else {
        console.log(resp.message);
      }
    },
  });
}
</script>

<style scoped>

</style>