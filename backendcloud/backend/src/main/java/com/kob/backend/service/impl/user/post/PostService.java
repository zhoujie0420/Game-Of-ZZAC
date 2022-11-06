package com.kob.backend.service.impl.user.post;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.kob.backend.mapper.PostMapper;
import com.kob.backend.pojo.Post;
import com.kob.backend.pojo.User;
import com.kob.backend.utils.UserUtil;

import javafx.geometry.Pos;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PostService {
    private PostMapper postMapper;

    public List<Post> getlist(){  //获取所有post
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createtime");
        return postMapper.selectList(queryWrapper);
    }

    public List<Post> getlistbyid(Map<String,String> data){  //获取id用户的post

        int id = Integer.parseInt(data.get("id"));

        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id).orderByDesc("createtime");
        return postMapper.selectList(queryWrapper);
    }

    public Map<String,String>  add (Map<String,String> data){  //增加post
        Map<String, String> map = new HashMap<>();
        User user = UserUtil.getUser();
//        private Integer id;
//        private String username;
//        private String photo;
//        private Integer userId;
//        private String content;
//        private Integer reply;
//        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
//        private Date createTime;
        String username = user.getUsername();
        String photo = user.getPhoto();
        Integer userid = user.getId();
        String content = data.get("content");
        Date now = new Date();

        if(content.length() == 0){
            map.put("error_message","内容不能为空");
            return map;
        }

        if(content.length() >16000){
            map.put("error_message","内容过长");
            return map;
        }

        Post post = new Post(null,username,photo,userid,content,0,now);

        postMapper.insert(post);
        map.put("error_message", "success");
        return map;
    }

    public Map<String,String> remove(Map<String,String> data){  //删除post
        Map<String, String> map = new HashMap<>();

        User user = UserUtil.getUser();


        int id = Integer.parseInt(data.get("id"));

        Post post = postMapper.selectById(id);


        if(post == null){
            map.put("error_message","Post不存在或已被删除");
            return map;
        }
        if(!post.getUserId().equals(user.getId())){
            System.out.println(post.getUserId());
            System.out.println(user.getId());
            map.put("error_message","没有权限删除");
            return map;
        }

        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id" ,id).eq("user_id",user.getId());

        postMapper.delete(queryWrapper);
        map.put("error_message" , "success");
        return map;
    }


}
