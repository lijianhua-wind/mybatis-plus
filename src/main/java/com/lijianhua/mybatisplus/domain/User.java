package com.lijianhua.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
//设置实体类所对应的表名
//@TableName("t_user")
public class User {

    //1. 将属性所对应的字段指定为主键，不指定默认将属性名为id的字段作为主键
    //如果未指定@TableId，然后属性名不为id，比如主键名为uid，执行程序时会忽略uid，有点不明白。
    //只是指定@TableId，默认会对应数据库表字段名相同的字段，如果两边名字不同又会出错。
    //需要给@TableId的value属性赋值指定对应字段
    //2. mybatis-plus默认会使用雪花算法生成主键id，可以通过设置TableId的type属性修改，详细看源码
    //总结：
    //    @TableId：将属性所对应的字段指定为主键

    //一、@TableId注解的value属性用于指定主键的字段
    //二、@TableId注解的type属性用于设置主键生成策略
//    @TableId(value = "uid", type = IdType.AUTO)
    @TableId(value = "uid")
    private Long id;

    //指定属性所对应的字段名
    @TableField(value = "user_name")
    private String name;

    private Integer age;

    private String email;

    //指定该字段为逻辑删除字段  0：未删除   1：已删除
    @TableLogic
    private Integer isDeleted;
}
