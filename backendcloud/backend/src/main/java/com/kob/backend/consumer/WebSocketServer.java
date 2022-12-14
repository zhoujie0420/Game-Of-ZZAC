package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    public static BotMapper botMapper;
    public static RestTemplate restTemplate;
    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";


    public Game game = null;
    public final static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    //ConcurrentHashMap 的优势在于兼顾性能和线程安全，一个线程进行写操作时，
    //它会锁住一小部分，其他部分的读写不受影响，其他线程访问没上锁的地方不会被阻塞。
    //(userId,WebSocketServer实例)



    private User user; //当前用户

    private Session session = null; //session 维护链接

    public static UserMapper userMapper;
    public static RecordMapper recordMapper;


    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate = restTemplate;
    }

    @Autowired// WebSocket不属于Spring的一个组件，不是单例模式,注入mapper方式有点特殊
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

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


    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId),b = userMapper.selectById(bId);

        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);


        Game game = new Game(13, 14, 20, a.getId(), botA,b.getId(),botB);
        game.createMap();
        // 一局游戏一个线程，会执行game类的run方法
        game.start();

        if(users.get(a.getId()) != null)
            users.get(a.getId()).game = game;

        if(users.get(b.getId()) != null)
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
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        // 发送给B的信息
        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("gamemap", game.getG());
        respB.put("game", respGame);
        // 通过userId取出b的连接，给B发送respB
        if(users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }




    private void move(int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {

            if (game.getPlayerA().getBotId().equals(-1))  // 亲自出马
                game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {

            if (game.getPlayerB().getBotId().equals(-1))  // 亲自出马
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
            startMatching(data.getInteger("bot_id"));
        } else if("stop-matching".equals(event)) {
            stopMatching();
        } else if("move".equals(event)) {
            move(data.getInteger("direction"));
        }
    }

    private void startMatching(Integer botId){
        System.out.println("start matching!");

        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id",botId.toString());
        restTemplate.postForObject(addPlayerUrl,data,String.class);
    }



    private void stopMatching(){
        System.out.println("stop matching!");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);

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

