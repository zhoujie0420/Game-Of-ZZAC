package com.kob.backend.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: jiezhou
 * 预约表
 * 设计：
 * 1.预约表中的doctorId和patientId都是外键，分别指向医生表和患者表
 * 2.预约表中的appointmentDate是预约日期，timeSlot是预约时间批次，1 代表 9点到 9.30 2 代表 9.30 到 10点 以此类推
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer doctorId;
    private Integer patientId;
    private Date appointmentDate;
    /**
     * 预约时间批次
     */
    private Integer timeSlot;
    private Integer status;
}