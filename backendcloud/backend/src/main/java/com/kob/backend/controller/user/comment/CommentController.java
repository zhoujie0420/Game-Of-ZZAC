package com.kob.backend.controller.user.comment;


import com.kob.backend.pojo.Comment;
import com.kob.backend.service.impl.user.comment.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author zhouj
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/user/comment")
public class CommentController {
    private CommentService commentService;

    @PostMapping("add")
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        return commentService.add(data);
    }


    @PostMapping("remove")
    public Map<String, String> remove(@RequestParam Map<String, String> data) {
        return commentService.remove(data);
    }

    @PostMapping("getComment")
    public List<Comment> getlist(@RequestParam Map<String, String> data) {
        int postId = Integer.parseInt(data.get("postId"));
        return commentService.getComment(postId);
    }
}
