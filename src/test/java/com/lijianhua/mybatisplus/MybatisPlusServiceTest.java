package com.lijianhua.mybatisplus;

import com.lijianhua.mybatisplus.domain.User;
import com.lijianhua.mybatisplus.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李建华
 * @date 2022/10/27
 * @comment
 */
@SpringBootTest
public class MybatisPlusServiceTest {

    @Autowired
    private UserService userService;

    //测试查询总记录数
    @Test
    void testGetCount() {
        /* 1. 查询总记录数
        ==>  Preparing: SELECT COUNT( * ) FROM user
        ==> Parameters:
        <==    Columns: COUNT( * )
        <==        Row: 5
        <==      Total: 1
         */
        long count = userService.count();
        System.out.println("总记录数：" + count);
    }

    //测试批量添加
    @Test
    void testInsertMore() {
        /* 1. default boolean saveBatch(Collection<T> entityList)
        ==>  Preparing: INSERT INTO user ( id, name, age ) VALUES ( ?, ?, ? )
        ==> Parameters: 1585466076738695169(Long), ljh0(String), 20(Integer)
        ==> Parameters: 1585466076864524290(Long), ljh1(String), 21(Integer)
        ==> Parameters: 1585466076864524291(Long), ljh2(String), 22(Integer)
        ==> Parameters: 1585466076864524292(Long), ljh3(String), 23(Integer)
        ==> Parameters: 1585466076864524293(Long), ljh4(String), 24(Integer)
        ==> Parameters: 1585466076864524294(Long), ljh5(String), 25(Integer)
        ==> Parameters: 1585466076864524295(Long), ljh6(String), 26(Integer)
        ==> Parameters: 1585466076864524296(Long), ljh7(String), 27(Integer)
        ==> Parameters: 1585466076864524297(Long), ljh8(String), 28(Integer)
        ==> Parameters: 1585466076864524298(Long), ljh9(String), 29(Integer)
        是否成功：true
         */
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("ljh" + i);
            user.setAge(20 + i);
            list.add(user);
        }
        boolean result = userService.saveBatch(list);
        System.out.println("是否成功：" + result);
    }


}
