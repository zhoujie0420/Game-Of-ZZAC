package com.kob.backend.controller.user.bot;


import com.kob.backend.pojo.Bot;
import com.kob.backend.service.impl.user.bot.BotService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/bot")
public class BotController {

    private BotService botService;
    @PostMapping("add")
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        return botService.add(data);
    }

    @PostMapping("remove")
    public Map<String, String> remove(@RequestParam Map<String, String> data) {
        System.out.println(" data"+data);
        return botService.remove(data);
    }
    @PostMapping("update")
    public Map<String, String> update(@RequestParam Map<String, String> data) {
        return botService.update(data);
    }

    @GetMapping("getlist")
    public List<Bot> getList() {
        return botService.getList();
    }

}


