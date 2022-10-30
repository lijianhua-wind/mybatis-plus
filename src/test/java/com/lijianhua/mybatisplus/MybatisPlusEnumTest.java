package com.lijianhua.mybatisplus;

import com.lijianhua.mybatisplus.domain.User;
import com.lijianhua.mybatisplus.enums.SexEnum;
import com.lijianhua.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 李建华
 * @create 2022/10/29 9:26
 * @description 自己写
 * @since 1.0
 */
@SpringBootTest
public class MybatisPlusEnumTest {
    
    @Autowired
    private UserMapper userMapper;

    //枚举类型的字段对应数据库中int类型的字段，默认会报错。
    //解决方法：
    // 1. 在application.yml中配置  type-enums-package 指定要扫描的通用枚举包
    //   （2022-3-17已废弃，直接加注解就行）
    // 2. 在enum类的属性中指定@EnumValue，赋值时就会将这个值赋值给数据库。从而解决问题。
    @Test
    void test() {
        /*  2. 加了@EnumValue注解，成功。
            ==>  Preparing: INSERT INTO t_user ( user_name, age, sex )
                            VALUES ( ?, ?, ? )
            ==> Parameters: admin(String), 33(Integer), 1(Integer)
            <==    Updates: 1
            Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@1702830d]
            result：1
         */
        User user = new User();
        user.setName("admin");
        user.setAge(33);
        user.setSex(SexEnum.MALE);
        int result = userMapper.insert(user);
        /*  1. 未加@EnumValue
            报错原因：将 'MALE' 赋给了 sex(int) 这个字段 自然报错。
            org.springframework.jdbc.UncategorizedSQLException:
            ### Error updating database.  Cause: java.sql.SQLException:
                Incorrect integer value: 'MALE' for column 'sex' at row 1
            ### The error may exist in com/lijianhua/mybatisplus/mapper/UserMapper.java
                (best guess)
            ### The error may involve com.lijianhua.mybatisplus.mapper.UserMapper.insert-Inline
            ### The error occurred while setting parameters
            ### SQL: INSERT INTO t_user  ( user_name, age,  sex )  VALUES  ( ?, ?,  ? )
            ### Cause: java.sql.SQLException:
                Incorrect integer value: 'MALE' for column 'sex' at row 1;
                uncategorized SQLException; SQL state [HY000]; error code [1366];
                Incorrect integer value: 'MALE' for column 'sex' at row 1;
                nested exception is java.sql.SQLException:
                Incorrect integer value: 'MALE' for column 'sex' at row 1
         */
        System.out.println("result：" + result);
    }
}
