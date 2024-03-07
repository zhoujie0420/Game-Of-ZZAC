package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: elk
 * @create: 2024-01-19 09:48
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @TableId(type = IdType.AUTO)
    private Integer id;
    // ,隔开
    private String recordList;


}