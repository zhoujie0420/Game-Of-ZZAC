package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jiezhou
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer departmentId;
    private String recordList;
}