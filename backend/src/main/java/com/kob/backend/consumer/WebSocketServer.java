package com.kob.backend.consumer;

import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    //ConcurrentHashMap 的优势在于兼顾性能和线程安全，一个线程进行写操作时，
    //它会锁住一小部分，其他部分的读写不受影响，其他线程访问没上锁的地方不会被阻塞。
    //(userId,WebSocketServer实例)
    private static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();
    private User user; //当前用户
    private Session session = null; //session 维护链接

    private static UserMapper userMapper;
    @Autowired // WebSocket不属于Spring的一个组件，不是单例模式,注入mapper方式有点特殊
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        System.out.println("connected!");
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if (this.user != null) {
            users.put(userId, this);//加入到users
        } else {
            this.session.close();
        }

        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected");
        if(this.user != null){
            users.remove(this.user.getId());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message");

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
        synchronized (this.session){
            try {
                this.session.getBasicRemote().sendText(message);

            } catch (IOException e) {
                 e.printStackTrace();
            }
        }
    }
}

