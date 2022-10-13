
<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
</template>


<script>
import PlayGround from '../../components/PlayGround.vue';
import MatchGround from '../../components/MatchGround.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
    components: {
        PlayGround,
        MatchGround,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() => {

            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            });

            socket = new WebSocket(socketUrl);

            // 回调函数：连接时调用
            socket.onopen = () => {//如果连接成功，将socket存储到全局变量中
                console.log("connected!");
                store.commit("updateSocket", socket);
            };


            // 回调函数：接收到后端信息调用
            socket.onmessage = msg => {
                // 返回的信息格式由后端框架定义，django与spring定义的不一样
                const data = JSON.parse(msg.data);
                if(data.event === "start-matching") {
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2000);
                   // 此处加上更新地图
                   store.commit("updateGamemap", data.gamemap);
                }
            }


            // 回调函数：断开连接时调用
            socket.onclose = () => {
                console.log("disconnected!");
            }
        });
        onUnmounted(() => {
            socket.close(); 
            store.commit("updateStatus", "matching");

        });
    }
}
</script>

<style scoped>

</style>
