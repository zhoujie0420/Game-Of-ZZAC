package com.kob.backend.service.impl.user.comment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.CommentMapper;
import com.kob.backend.mapper.PostMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Comment;
import com.kob.backend.pojo.User;
import com.kob.backend.utils.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author dell
 */
@Service
@AllArgsConstructor
public class CommentService {

    private CommentMapper commentMapper;

    private PostMapper postMapper;

    private UserMapper userMapper;

    /**
     * 获取评论
     *
     * @param id
     * @return
     */
    public List<Comment> getComment(int id) {

        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("post_id", id).orderByDesc("createtime");
        List<Comment> comments = commentMapper.selectList(qw);
        for (Comment comment : comments) {
            Integer userId = comment.getUserId();
            User user = userMapper.selectById(userId);
            comment.setUsername(user.getUsername());

            Integer targetUserId = comment.getTargetUserid();
            User targetUser = userMapper.selectById(targetUserId);
            comment.setTargetUsername(targetUser.getUsername());
        }

        return comments;
    }

    /**
     * 添加评论
     *
     * @param data
     * @return
     */
    public Map<String, String> add(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        User user = UserUtil.getUser();
        Date now = new Date();
        int postId = Integer.parseInt(data.get("postId"));
        String content = data.get("content");
        int targetUserId = Integer.parseInt(data.get("targetUserId"));
        if (content == null || content.length() == 0) {
            map.put("error_message", "评论不可以为空");
            return map;
        }
        if (content.length() > 100) {
            map.put("error_message", "内容过长");
            return map;
        }
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(user.getId());
        comment.setContent(content);
        comment.setTargetUserid(targetUserId);
        comment.setCreatetime(now);
        commentMapper.insert(comment);
        map.put("error_message", "success");
        return map;
    }

    public Map<String, String> remove(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        User user = UserUtil.getUser();
        int id = Integer.parseInt(data.get("id"));
        Comment comment = commentMapper.selectById(id);

        if (comment == null) {
            map.put("error_message", "评论不存在或已被删除");
            return map;
        }
        if (!comment.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限删除");
            return map;
        }
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);

        commentMapper.delete(queryWrapper);
        map.put("error_message", "success");
        return map;
    }

}
