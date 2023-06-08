package com.kob.backend.controller.user.comment;


import com.kob.backend.pojo.Comment;
import com.kob.backend.pojo.Post;
import com.kob.backend.service.impl.user.comment.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/comment")
public class CommentController {
    private CommentService commentService;

    @PostMapping("add")
    public Map<String,String> add(@RequestParam Map<String,String> data){
        return commentService.add(data);
    }

    @PostMapping("addChild")
    public Map<String, String> addChild(@RequestParam Map<String ,String> data){
        return commentService.addChild(data);
    }

    @PostMapping("remove")
    public Map<String,String> remove(@RequestParam Map<String,String> data){
        return commentService.remove(data);
    }

    @RequestMapping("getlist")
    public List<Comment> getlist(@RequestParam Map<String,String> data){
        return commentService.getlist(data);
    }
}
