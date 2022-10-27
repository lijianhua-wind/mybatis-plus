package com.lijianhua.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
//设置实体类所对应的表名
//@TableName("t_user")
public class User {

    //将属性所对应的字段指定为主键，不指定默认将属性名为id的字段作为主键，
    //如果未指定@TableId，然后属性名不为id，比如主键名为uid，执行程序时会忽略uid，有点不明白。
    //只是指定@TableId，默认会对应数据库表字段名相同的字段，如果两边名字不同又会出错。
    @TableId
    private Long id;

    private String name;

    private Integer age;

    private String email;
}
