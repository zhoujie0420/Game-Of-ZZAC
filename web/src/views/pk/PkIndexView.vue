PkIndexView.vue

<template>
    <PlayGround />
</template>

<script>
import PlayGround from '../../components/PlayGround.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
    components: {
        PlayGround,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() => {
            socket = new WebSocket(socketUrl);

            // 回调函数：连接时调用
            socket.onopen = () => {
                console.log("connected!");
            };

            // 回调函数：接收到后端信息调用
            socket.onmessage = msg => {
                // 返回的信息格式由后端框架定义，django与spring定义的不一样
                const data = JSON.parse(msg.data);
                console.log(data);
            }

            // 回调函数：断开连接时调用
            socket.onclose = () => {
                console.log("disconnected!");
            }
        });
        onUnmounted(() => {
            socket.close(); 
        });
    }
}
</script>

<style scoped>

</style>
