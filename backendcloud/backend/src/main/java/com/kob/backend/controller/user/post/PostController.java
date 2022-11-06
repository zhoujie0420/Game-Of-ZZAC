package com.kob.backend.controller.user.post;


import com.kob.backend.pojo.Post;
import com.kob.backend.service.impl.user.post.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/post")
public class PostController {
    private PostService postService;


    @PostMapping("add")
    public Map<String,String>add(@RequestParam Map<String,String>data){
        System.out.println(data);
        return postService.add(data);
    }

    @PostMapping("remove")
    public Map<String,String> remove(@RequestParam Map<String,String> data){
        return postService.remove(data);
    }

    @RequestMapping("getlist")
    public List<Post> getlist(){
        return postService.getlist();
    }

    @RequestMapping ("getlistbyid")
    public List<Post> getlistbyid(@RequestParam Map<String,String> data){
        return postService.getlistbyid(data);
    }

}
