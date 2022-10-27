package com.lijianhua.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
//设置实体类所对应的表名
@TableName("t_user")
public class User {

    private Long id;
    private String name;
    private Integer age;
    private String email;
}
