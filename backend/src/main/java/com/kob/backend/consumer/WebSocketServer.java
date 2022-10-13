package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
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
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private Game game = null;
    public final static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    //ConcurrentHashMap 的优势在于兼顾性能和线程安全，一个线程进行写操作时，
    //它会锁住一小部分，其他部分的读写不受影响，其他线程访问没上锁的地方不会被阻塞。
    //(userId,WebSocketServer实例)


    // 线程安全的set
    private static final CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();

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
            matchPool.remove(this.user);
        }
    }



    private void move(int direction) {
        if(game.getPlayerA().getId().equals(user.getId())) {
            game.setNextStepA(direction);
        } else if(game.getPlayerB().getId().equals(user.getId())) {
            game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message: " + message);
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)) {
            startMatching();
        } else if("stop-matching".equals(event)) {
            stopMatching();
        } else if("move".equals(event)) {
            move(data.getInteger("direction"));
        }
    }

    private void startMatching(){
        System.out.println("start matching!");
        matchPool.add(this.user);
        while(matchPool.size() >= 2) {

            Iterator<User> it = matchPool.iterator();
            // 匹配成功
            User a = it.next(), b = it.next();
            matchPool.remove(a);
            matchPool.remove(b);




            Game game = new Game(13, 14, 20, a.getId(), b.getId());
            game.createMap();
            // 一局游戏一个线程，会执行game类的run方法
            game.start();

            users.get(a.getId()).game = game;
            users.get(b .getId()).game = game;


            JSONObject respGame = new JSONObject();
            // 玩家的id以及横纵信息
            respGame.put("a_id", game.getPlayerA().getId());
            respGame.put("a_sx", game.getPlayerA().getSx());
            respGame.put("a_sy", game.getPlayerA().getSy());
            respGame.put("b_id", game.getPlayerB().getId());
            respGame.put("b_sx", game.getPlayerB().getSx());
            respGame.put("b_sy", game.getPlayerB().getSy());
            respGame.put("map", game.getG());

            // 发送给A的信息
            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            respA.put("gamemap", game.getG());
            respA.put("game", respGame);
            // 通过userId取出a的连接，给A发送respA
            users.get(a.getId()).sendMessage(respA.toJSONString());

            // 发送给B的信息
            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            respB.put("gamemap", game.getG());
            respB.put("game", respGame);
            // 通过userId取出b的连接，给B发送respB
            users.get(b.getId()).sendMessage(respB.toJSONString());


        }
    }



    private void stopMatching(){
        System.out.println("stop matching!");
        matchPool.remove(this.user);
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

    private void move(Integer direction) {
        //判断是A玩家还是B玩家在操作
        if(game.getPlayerA().getId().equals(user.getId())){
            game.setNextStepA(direction);
        }else if (game.getPlayerB().getId().equals(user.getId())) {
            game.setNextStepB(direction);
        } else {
            Exception e = new Exception("Error");
            e.printStackTrace();
        }
    }

}

