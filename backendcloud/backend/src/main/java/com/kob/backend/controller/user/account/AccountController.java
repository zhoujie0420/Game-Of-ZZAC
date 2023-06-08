package com.kob.backend.controller.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.user.account.AccountService;
import com.kob.backend.service.impl.utils.UtilsService;
import com.kob.backend.utils.rabbitmq.Consumer;
import com.kob.backend.utils.rabbitmq.Producer;
import com.kob.backend.utils.redis.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/account")

public class AccountController {

    private final AccountService accountService;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    Producer producer;

    @RequestMapping("test")
    public String test() throws InterruptedException {
        User user = new User(1,"1","1","1","s",1);
        producer.produce(user);
        Thread.sleep(1000);
        return "success";
    }

    @PostMapping("token") //账号密码获取token
    public Map<String,String> getToken(@RequestParam Map<String,String> map){
        String username =map.get("username");
        String password =map.get("password");
        return accountService.getToken(username,password);
    }

    @PostMapping("emailtoken")  //邮箱code获取token
    public Map<String,String> getEmailToken(@RequestParam Map<String,String> map){
        String email = map.get("email");
        String code = map.get("code");
        return accountService.getEmailToken(email,code);
    }

    @GetMapping("info")
    public Map<String, String> getinfo() {
        return accountService.getinfo();
    }

    @PostMapping("register")
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
        String email = map.get("email");
        return accountService.register(username, password, confirmedPassword,email);
    }

    @PostMapping("photo")
    public Map<String, String> photo(@RequestParam Map<String, String> map) {

        String photo = map.get("photo");
        return accountService.photo(photo);
    }

}
