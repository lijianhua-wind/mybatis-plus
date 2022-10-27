package com.lijianhua.mybatisplus;

import com.lijianhua.mybatisplus.domain.User;
import com.lijianhua.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 李建华
 * @date 2022/10/27
 * @comment
 */
@SpringBootTest
public class MybatisPlusTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testSelect() {
        /* 根据主键查询
        ==>  Preparing: SELECT id,name,age,email FROM user WHERE id=?
        ==> Parameters: 1(Integer)
        <==    Columns: id, name, age, email
        <==        Row: 1, Jone, 18, test1@baomidou.com
        <==      Total: 1
         */
        User user = userMapper.selectById(1);
        System.out.println(user);
    }
}
