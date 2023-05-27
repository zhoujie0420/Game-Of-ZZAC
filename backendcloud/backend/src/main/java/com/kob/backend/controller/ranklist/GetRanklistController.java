package com.kob.backend.controller.ranklist;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.ranklist.GetRanklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class GetRanklistController {
    @Autowired
    private GetRanklistService getRanklistService;

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @GetMapping("/api/ranklist/getlist/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        String cacheRankList = stringRedisTemplate.opsForValue().get("cache_rank_list");

        if (StrUtil.isNotBlank(cacheRankList)) {
            return JSONObject.parseObject(cacheRankList);
        }

        Integer page = Integer.parseInt(data.get("page"));
        JSONObject res = getRanklistService.getList(page);
        stringRedisTemplate.opsForValue().set("cache_rank_list", String.valueOf(res));
        return res;
    }
}
