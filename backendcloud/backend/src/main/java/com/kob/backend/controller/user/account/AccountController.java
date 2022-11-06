package com.kob.backend.controller.user.account;

import com.kob.backend.service.impl.user.account.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
;import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/account")

public class AccountController {

    private final AccountService service;

    @PostMapping("token")
    public Map<String,String> getToken(@RequestParam Map<String,String> map){
        String username =map.get("username");
        String password =map.get("password");
        return service.getToken(username,password);
    }

    @GetMapping("info")
    public Map<String, String> getinfo() {
        return service.getinfo();
    }

    @PostMapping("register")
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
        return service.register(username, password, confirmedPassword);
    }

    @PostMapping("photo")
    public Map<String, String> photo(@RequestParam Map<String, String> map) {

        String photo = map.get("photo");
        return service.photo(photo);
    }

}
