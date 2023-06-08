package com.kob.backend.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.kob.backend.pojo.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends MPJBaseMapper<Post> {
}
