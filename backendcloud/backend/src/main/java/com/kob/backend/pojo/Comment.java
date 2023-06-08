package com.kob.backend.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer postId;
    private Integer userId;
    @TableField(exist = false)
    private Integer username;
    private String content;
    private Integer commentId;
    private Integer targetUserid;
    @TableField(exist = false)
    private String targetUsername;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createtime;


    @TableField(exist = false) //表示数据库中不存在
    private List<Comment> childrenComment;

}
