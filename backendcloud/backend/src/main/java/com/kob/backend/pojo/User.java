package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 简化了get set tostring
@NoArgsConstructor//无参构造
@AllArgsConstructor//有参构造 显然这两个市成对出现的
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private Integer age;
    private String phone;
    /**
     * 身份 医生 2 患者 1
     */
    private Integer role;
    private Integer roleId;
}
