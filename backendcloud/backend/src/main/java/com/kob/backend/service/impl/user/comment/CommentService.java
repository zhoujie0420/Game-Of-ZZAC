package com.kob.backend.service.impl.user.comment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import com.kob.backend.mapper.CommentMapper;

import com.kob.backend.mapper.PostMapper;
import com.kob.backend.mapper.UserMapper;
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


/**
 * @author dell
 */
@Service
@AllArgsConstructor
public class CommentService {

    private CommentMapper commentMapper;

    private PostMapper postMapper;

    private UserMapper userMapper;

    public List<Comment> getlist(Map<String,String> data){ // 获取foreignId的评论
        int postId = Integer.parseInt(data.get("postId"));

        MPJQueryWrapper<Comment> qw = new MPJQueryWrapper<>();
        qw.leftJoin("user userdb on userdb.id = t.user_id");
        qw.select("userdb.username as username");
        qw.select("t.*");
        qw.eq("post_id",postId).orderByDesc("createtime");

        List<Comment> comments = commentMapper.selectList(qw);

        List<Comment> rootComments = comments.stream().filter(comment -> comment.getCommentId() == 0).collect(Collectors.toList());
        // 使用stream() 将rootComments过滤成父级评论

        for (Comment rootComment : rootComments) {
            rootComment.setChildrenComment(comments.stream().filter(comment -> rootComment.getId().equals(comment.getCommentId())).collect(Collectors.toList()));
        }
        //将 rootComment.getId() == comment.getPid() 匹配的放入ChildrenList
        return rootComments;
    }

    /**
     * 添加评论---一级评论
     * @param data
     * @return
     */
    public Map<String,String> add(Map<String,String> data){
        Map<String,String> map = new HashMap<>();
        User user = UserUtil.getUser();
        Date now = new Date();
        int postId = Integer.parseInt(data.get("postId"));
        String content = data.get("content");
        if(content == null || content.length() == 0){
            map.put("error_message","评论不可以为空");
            return map;
        }
        if(content.length() > 100){
            map.put("error_message","内容过长");
            return map;
        }

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(user.getId());
        comment.setContent(content);
        comment.setCommentId(0);
        comment.setTargetUserid(0);
        comment.setCreatetime(now);
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

    /**
     * 添加评论---二级评论
     * @param data
     * @return
     */
    public Map<String, String> addChild(Map<String, String> data) {
        Map<String,String> map = new HashMap<>();
        User user = UserUtil.getUser();
        Date now = new Date();
        int postId = Integer.parseInt(data.get("postId"));
        int commitId = Integer.parseInt(data.get("commitId"));
        int targetUserid = Integer.parseInt(data.get("targetUserid"));
        String content = data.get("content");
        Post post = postMapper.selectById(postId);
        if(content == null || content.length() == 0){
            map.put("error_message","评论不可以为空");
            return map;
        }
        if(content.length() > 100){
            map.put("error_message","内容过长");
            return map;
        }

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(user.getId());
        comment.setContent(content);
        comment.setCommentId(commitId);
        comment.setTargetUserid(targetUserid);
        comment.setCreatetime(now);
        commentMapper.insert(comment);
        map.put("error_message","success");
        return map;
    }
}
