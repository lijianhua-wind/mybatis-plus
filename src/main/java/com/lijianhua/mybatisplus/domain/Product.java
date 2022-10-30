package com.lijianhua.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * @author 李建华
 * @create 2022/10/28 22:07
 * @description 自己写
 * @since 1.0
 */
@Data
public class Product {
    private Long id;
    private String name;
    private Integer price;

    //乐观锁注解
    //支持的字段类型: long, Long, int, Integer,
    //java.util.Date, java.sql.Timestamp, java.time.LocalDateTime
    @Version
    private Integer version;
}
