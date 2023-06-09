package com.kob.backend.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author dell
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("帖子id")
    private Integer postId;

    @ApiModelProperty("评论本人id")
    private Integer userId;

    @TableField(exist = false)
    @ApiModelProperty("评论本人姓名")
    private String username;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("评论对象Id")
    private Integer targetUserid;

    @TableField(exist = false)
    @ApiModelProperty("评论对象姓名")
    private String targetUsername;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createtime;

}
