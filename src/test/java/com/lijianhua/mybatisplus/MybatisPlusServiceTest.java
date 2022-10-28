package com.lijianhua.mybatisplus;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.lijianhua.mybatisplus.domain.User;
import com.lijianhua.mybatisplus.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
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

    //1. 测试查询总记录数
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

    //2. 测试批量添加
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

    //3. mybatis-plus默认只会使用表字段名为id的当主键。
    @Test
    void test() {
        //例：设置uid为primary key，使用IService的saveBatch测试
        /*
        执行结果：
        ==>  Preparing: INSERT INTO t_user ( name, age ) VALUES ( ?, ? )
        ==> Parameters: ljh0(String), 20(Integer)
        ==> Parameters: ljh1(String), 21(Integer)
        ==> Parameters: ljh2(String), 22(Integer)
        ==> Parameters: ljh3(String), 23(Integer)
        ==> Parameters: ljh4(String), 24(Integer)
        ==> Parameters: ljh5(String), 25(Integer)
        ==> Parameters: ljh6(String), 26(Integer)
        ==> Parameters: ljh7(String), 27(Integer)
        ==> Parameters: ljh8(String), 28(Integer)
        ==> Parameters: ljh9(String), 29(Integer)
        插入数据时，忽略了uid？默认会认为id就是主键，但是为什么忽略了uid？
        使用@TableId标注在uid属性上指定uid为主键，就行了。
        ==>  Preparing: INSERT INTO t_user ( uid, name, age ) VALUES ( ?, ?, ? )
        ==> Parameters: 1585521457674387457(Long), ljh0(String), 20(Integer)
        ==> Parameters: 1585521457854742529(Long), ljh1(String), 21(Integer)
        ==> Parameters: 1585521457926045698(Long), ljh2(String), 22(Integer)
        ==> Parameters: 1585521457926045699(Long), ljh3(String), 23(Integer)
        ==> Parameters: 1585521457926045700(Long), ljh4(String), 24(Integer)
        ==> Parameters: 1585521457942822913(Long), ljh5(String), 25(Integer)
        ==> Parameters: 1585521457942822914(Long), ljh6(String), 26(Integer)
        ==> Parameters: 1585521457942822915(Long), ljh7(String), 27(Integer)
        ==> Parameters: 1585521457942822916(Long), ljh8(String), 28(Integer)
        ==> Parameters: 1585521457942822917(Long), ljh9(String), 29(Integer)
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

    //4. 测试实体类字段与数据库表字段名不同的情况
    @Test
    void test2() {
        //例：实体类：id  --> 对应t_user表的主键 uid
        /* 显示id不在字段列表
        org.springframework.jdbc.BadSqlGrammarException:
        ### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column 'id' in 'field list'
        ### The error may exist in com/lijianhua/mybatisplus/mapper/UserMapper.java (best guess)
        ### The error may involve com.lijianhua.mybatisplus.mapper.UserMapper.insert-Inline
        ### The error occurred while setting parameters
        ### SQL: INSERT INTO t_user  ( id, name, age )  VALUES  ( ?, ?, ? )
        ### Cause: java.sql.SQLSyntaxErrorException: Unknown column 'id' in 'field list'
        ; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column 'id' in 'field list'
         */
        //解决方法：给@TableId的value属性赋值为uid
        /*
        ==>  Preparing: INSERT INTO t_user ( uid, name, age ) VALUES ( ?, ?, ? )
        ==> Parameters: 1585804033265766402(Long), ljh(String), 20(Integer)
        <==    Updates: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@27df0f3d]
        result：true
        刚添加的用户信息：User(id=1585804033265766402, name=ljh, age=20, email=null)
         */
        User user = new User();
        user.setName("ljh");
        user.setAge(20);
        boolean result = userService.save(user);
        System.out.println("result：" + result);
        System.out.println("刚添加的用户信息：" + user);
    }

    //5. 测试雪花算法
    @Test
    void testAuto() {
        /* 默认情况：雪花算法
        ==>  Preparing: INSERT INTO t_user ( uid, name, age ) VALUES ( ?, ?, ? )
        ==> Parameters: 1585812504363208706(Long), ljh(String), 20(Integer)
        <==    Updates: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@39c385d6]
        result：true
        刚添加的用户信息：User(id=1585812504363208706, name=ljh, age=20, email=null)
         */
        //修改默认的雪花算法生成id改成使用auto_increment
        /*  将@TableId的type属性设置为IdType.AUTO
            ==>  Preparing: INSERT INTO t_user ( name, age ) VALUES ( ?, ? )
            ==> Parameters: ljh(String), 20(Integer)
            <==    Updates: 1
            Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@692e028d]
            result：true
            刚添加的用户信息：User(id=6, name=ljh, age=20, email=null)
         */
        //指定使用自己赋值的uid，默认的雪花算法不会生效
        /*
        ==>  Preparing: INSERT INTO t_user ( uid, name, age ) VALUES ( ?, ?, ? )
        ==> Parameters: 100(Long), ljh(String), 20(Integer)
        <==    Updates: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@39c385d6]
        result：true
        刚添加的用户信息：User(id=100, name=ljh, age=20, email=null)
         */
        //通过配置mybatis-plus的全局配置设置Id-type为auto也可以使用自增id
        /*
        ==>  Preparing: INSERT INTO t_user ( name, age ) VALUES ( ?, ? )
        ==> Parameters: ljh(String), 20(Integer)
        <==    Updates: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3cb8c8ce]
        result：true
        刚添加的用户信息：User(id=6, name=ljh, age=20, email=null)
         */
        User user = new User();
//        user.setId(100L);
        user.setName("ljh");
        user.setAge(20);
        boolean result = userService.save(user);
        System.out.println("result：" + result);
        System.out.println("刚添加的用户信息：" + user);
    }

    //6. 测试mybatis_plus是否配置了下划线转驼峰
    //成功
    @Test
    void testCamel() {
        //private String userName 对应 数据库字段 user_name
        /*
        ==>  Preparing: INSERT INTO t_user ( user_name, age ) VALUES ( ?, ? )
        ==> Parameters: ljh(String), 20(Integer)
        <==    Updates: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3cb8c8ce]
        result：true
        刚添加的用户信息：User(id=7, userName=ljh, age=20, email=null)
         */
        User user = new User();
        user.setName("ljh");
        user.setAge(20);
        boolean result = userService.save(user);
        System.out.println("result：" + result);
        System.out.println("刚添加的用户信息：" + user);
    }

    //7. 测试逻辑删除 @TableLogic
    /*
        物理删除：真实删除，将对应数据从数据库中删除，之后查询不到此条被删除的数据
        逻辑删除：假删除，将对应数据中代表是否被删除字段的状态修改为“被删除状态”，之后在数据库
        中仍旧能看到此条数据记录
        使用场景：可以进行数据恢复

        0：未删除   1：已删除
     */
    @Test
    void testLogic() {
        //1. 测试删除
        /*  仅仅只是把逻辑删除字段改成 1 表示以删除
        ==>  Preparing: UPDATE t_user SET is_deleted=1 WHERE uid=? AND is_deleted=0
        ==> Parameters: 1(Long)
        ==> Parameters: 2(Long)
        ==> Parameters: 3(Long)
         */
//        boolean result = userService.removeBatchByIds(Arrays.asList(1L, 2L, 3L));
//        System.out.println("result：" + result);
        //2. 测试查询
        /*  普通的查询改成了查询is_deleted = 0的数据
        ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0
        ==> Parameters:
        <==    Columns: id, name, age, email, is_deleted
        <==        Row: 4, Sandy, 21, test4@baomidou.com, 0
        <==        Row: 5, Billie, 24, test5@baomidou.com, 0
        <==        Row: 6, ljh, 20, null, 0
        <==        Row: 7, ljh, 20, null, 0
        <==        Row: 8, ljh, 20, null, 0
        <==      Total: 5
         */
        List<User> list = userService.query().list();
        list.forEach(System.out::println);
    }
}
