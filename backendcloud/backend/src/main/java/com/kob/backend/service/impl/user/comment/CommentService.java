package com.kob.backend.service.impl.user.comment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.CommentMapper;

import com.kob.backend.mapper.PostMapper;
import com.kob.backend.pojo.Comment;
import com.kob.backend.pojo.Post;
import com.kob.backend.pojo.User;
import com.kob.backend.utils.UserUtil;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CommentService {

    private CommentMapper commentMapper;

    private PostMapper postMapper;

    public List<Comment> getlist(Map<String,String> data){ // 获取foreignId的评论
        int foreignId = Integer.parseInt(data.get("foreignId"));

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("foreign_id",foreignId).orderByDesc("createtime");

        List<Comment> comments = commentMapper.selectList(queryWrapper);

        List<Comment> rootComments = comments.stream().filter(comment -> comment.getPid() == null).collect(Collectors.toList());
        // 使用stream() 将rootComments过滤成父级评论

        for (Comment rootComment : rootComments) {
            rootComment.setChildrenComment(comments.stream().filter(comment -> rootComment.getId().equals(comment.getPid())).collect(Collectors.toList()));
        }
        //将 rootComment.getId() == comment.getPid() 匹配的放入ChildrenList
        return rootComments;
    }

    public Map<String,String> add(Map<String,String> data){
        Map<String,String> map = new HashMap<>();
        User user = UserUtil.getUser();
        Date now = new Date();
        int foreignId = Integer.parseInt(data.get("foreignId"));
        String content = data.get("content");
        int pid = Integer.parseInt(data.get("pid"));
        String target = data.get("target");


        Post post = postMapper.selectById(foreignId);
        if(post == null){
            map.put("error_message","该post不存在或已被删除");
            return map;
        }
        if(content == null){
            map.put("error_message","评论不可以为空");
            return map;
        }
        if(content.length() > 100){
            map.put("error_message","内容过长");
            return map;
        }
        if(pid != 0) {
            Comment comment = commentMapper.selectById(pid);
            if (comment == null) {
                map.put("error_message", "评论不存在或已被删除");
                return  map;
            }
        }

        Comment comment = new Comment(null, foreignId, user.getId(), user.getUsername(), content, pid, target, now, null);

        commentMapper.insert(comment);
        map.put("error_message","success");
        return map;
    }

    public Map<String,String> remove(Map<String,String> data){
        Map<String,String> map = new HashMap<>();
        User user = UserUtil.getUser();
        int id = Integer.parseInt(data.get("id"));
        Comment comment = commentMapper.selectById(id);

        if(comment == null){
            map.put("error_message","评论不存在或已被删除");
            return map;
        }
        if(!comment.getUserId().equals(user.getId())){
            map.put("error_message","没有权限删除");
            return map;
        }
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);

        commentMapper.delete(queryWrapper);
        map.put("error_message","success");
        return map;
    }
}
