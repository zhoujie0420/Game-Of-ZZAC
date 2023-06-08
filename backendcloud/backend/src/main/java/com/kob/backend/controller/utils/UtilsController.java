package com.kob.backend.controller.utils;

import com.kob.backend.service.impl.utils.UtilsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/api/utils")
public class UtilsController {

    private final UtilsService utilsService;

    @GetMapping("email")
    public Map<String,String> sendEmail(@RequestParam Map<String,String> map){
        String email = map.get("email");
        return utilsService.sendEmail(email);
    }

}
