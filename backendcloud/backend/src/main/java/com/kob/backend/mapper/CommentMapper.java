package com.kob.backend.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.kob.backend.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends MPJBaseMapper<Comment> {
}
