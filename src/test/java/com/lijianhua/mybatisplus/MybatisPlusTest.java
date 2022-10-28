package com.lijianhua.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijianhua.mybatisplus.domain.User;
import com.lijianhua.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 李建华
 * @date 2022/10/27
 * @comment
 */
@SpringBootTest
public class MybatisPlusTest {


    @Autowired
    private UserMapper userMapper; //这里报错没关系，想不报错就在UserMapper接口上加@Repository注解

    //1. 小案例
    @Test
    void test() {
        //mybatis-plus要查询什么表是由实体类名决定的，实体类的属性决定了表中字段
        //如果有条件需传入一个queryWrapper对象，无条件则传入null
        //执行的sql语句：
        //==>  Preparing: SELECT id,name,age,email FROM user
        //==> Parameters:
        //<==    Columns: id, name, age, email
        //<==        Row: 1, Jone, 18, test1@baomidou.com
        //<==        Row: 2, Jack, 20, test2@baomidou.com
        //<==        Row: 3, Tom, 28, test3@baomidou.com
        //<==        Row: 4, Sandy, 21, test4@baomidou.com
        //<==        Row: 5, Billie, 24, test5@baomidou.com
        //<==        Row: 1585263786804191234, 张三, 18, zhangsan@qq.com
        //<==      Total: 6
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    //2. 测试insert
    @Test
    void testInsert() {
        //执行的sql语句：
        //==>  Preparing: INSERT INTO user ( id, name, age, email ) VALUES ( ?, ?, ?, ? )
        //==> Parameters: 1585263786804191234(Long), 张三(String), 18(Integer), zhangsan@qq.com(String)
        //<==    Updates: 1
        //Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@63bde6c2]
        //result：1
        //是否可以获取刚放入数据的主键id：1585263786804191234
        //确定自动获取了主键信息
        User user = new User();
        user.setAge(18);
        user.setName("张三");
        user.setEmail("zhangsan@qq.com");
        int result
                = userMapper.insert(user);
        System.out.println("result：" + result);
        System.out.println("是否可以获取刚放入数据的主键id：" + user.getId());
    }

    //3. 测试delete
    @Test
    void testDelete() {
        //1.根据 主键ID 删除
        //==>  Preparing: DELETE FROM user WHERE id=?
        //==> Parameters: 1585260591944159233(Long)
        //<==    Updates: 0
        int result = userMapper.deleteById(1585260591944159233L);
        System.out.println("result：" + result);
        //2. 根据 columnMap 条件，删除记录
        //==>  Preparing: DELETE FROM user WHERE name = ? AND age = ?
        //==> Parameters: 张三(String), 18(Integer)
        //<==    Updates: 1
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 18);
        result = userMapper.deleteByMap(map);
        System.out.println("result：" + result);
        //3. 删除（根据ID或实体 批量删除）
        //==>  Preparing: DELETE FROM user WHERE id IN ( ? , ? , ? )
        //==> Parameters: 1(Long), 2(Long), 3(Long)
        //<==    Updates: 3
        //Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@15c4af7a]
        //result：3
        List<Long> list = Arrays.asList(1L, 2L, 3L);
//        List<Integer> list = Arrays.asList(1, 2, 3);
        result = userMapper.deleteBatchIds(list);
        System.out.println("result：" + result);
    }

    //4. 测试update
    @Test
    void testUpdate() {
        User user = new User();
        user.setId(5L);
        user.setName("李四");
        user.setEmail("lisi@qq.com");
        /*
        1. 执行的语句：
        ==>  Preparing: UPDATE user SET name=?, email=? WHERE id=?
        ==> Parameters: 李四(String), lisi@qq.com(String), 5(Long)
        <==    Updates: 1
         */
        int result = userMapper.updateById(user);
        System.out.println("result：" + result);
    }

    //5. 测试select
    @Test
    void testSelect() {
        /*
        1. 通过主键id查询用户信息
        ==>  Preparing: SELECT id,name,age,email FROM user WHERE id=?
        ==> Parameters: 1(Long)
        <==    Columns: id, name, age, email
        <==        Row: 1, Jone, 18, test1@baomidou.com
        <==      Total: 1
         */
        User user = userMapper.selectById(1L);
        System.out.println(user);
        /*
        2. 通过多个id值查询用户信息
        ==>  Preparing: SELECT id,name,age,email FROM user WHERE id IN ( ? , ? , ? , ? , ? )
        ==> Parameters: 1(Long), 2(Long), 3(Long), 4(Long), 5(Long)
        <==    Columns: id, name, age, email
        <==        Row: 1, Jone, 18, test1@baomidou.com
        <==        Row: 2, Jack, 20, test2@baomidou.com
        <==        Row: 3, Tom, 28, test3@baomidou.com
        <==        Row: 4, Sandy, 21, test4@baomidou.com
        <==        Row: 5, Billie, 24, test5@baomidou.com
        <==      Total: 5
         */
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        users.forEach(System.out::println);
        /*
        3. 根据map指定的条件查询user信息。
        ==>  Preparing: SELECT id,name,age,email FROM user WHERE name = ? AND age = ?
        ==> Parameters: Jone(String), 18(Integer)
        <==    Columns: id, name, age, email
        <==        Row: 1, Jone, 18, test1@baomidou.com
        <==      Total: 1
         */
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Jone");
        map.put("age", 18);
        List<User> userList = userMapper.selectByMap(map);
        userList.forEach(System.out::println);
        /*
        4. 根据设定的条件查询user信息，没有就是所有user
           userMapper.ge方法测试condition有什么用？
            1. condition为true：会执行设置的条件
            ==>  Preparing: SELECT id,name,age,email FROM user WHERE (id >= ?)
            ==> Parameters: 2(Integer)
            <==    Columns: id, name, age, email
            <==        Row: 2, Jack, 20, test2@baomidou.com
            <==        Row: 3, Tom, 28, test3@baomidou.com
            <==        Row: 4, Sandy, 21, test4@baomidou.com
            <==        Row: 5, Billie, 24, test5@baomidou.com
            <==        Row: 999, 李四, 999, lisi@qq.com
            <==      Total: 5
            2. condition为false: 不会执行这个条件
            ==>  Preparing: SELECT id,name,age,email FROM user
            ==> Parameters:
            <==    Columns: id, name, age, email
            <==        Row: 1, Jone, 18, test1@baomidou.com
            <==        Row: 2, Jack, 20, test2@baomidou.com
            <==        Row: 3, Tom, 28, test3@baomidou.com
            <==        Row: 4, Sandy, 21, test4@baomidou.com
            <==        Row: 5, Billie, 24, test5@baomidou.com
            <==        Row: 999, 李四, 999, lisi@qq.com
            <==      Total: 6
         */
        List<User> list = userMapper.selectList(new QueryWrapper<User>().
                ge(false, "id", 2));
        list.forEach(System.out::println);
    }

    // 测试自定义sql语句
    @Test
    void testCustom() {
        /*
        1. 没加@Mapkey注解，如果查询出多个结果会报错
        ==>  Preparing: select id, name, age, email from user where id = ?
        ==> Parameters: 1(Long)
        <==    Columns: id, name, age, email
        <==        Row: 1, Jone, 18, test1@baomidou.com
        <==      Total: 1
        输出结果：{name=Jone, id=1, age=18, email=test1@baomidou.com}
        2. 加了@Mapkey注解，查询出多个结果也不会报错，key为@Mapkey注解指定的字段值
        ==>  Preparing: select id, name, age, email from user where id = ?
        ==> Parameters: 1(Long)
        <==    Columns: id, name, age, email
        <==        Row: 1, Jone, 18, test1@baomidou.com
        <==      Total: 1
        输出结果：{1={name=Jone, id=1, age=18, email=test1@baomidou.com}}
         */
        Map<String, Object> map = userMapper.selectMapById(1L);
        System.out.println(map);
    }

    //证明测试查询的表名默认由实体类名确定
    //可以在实体类设置@TableName为指定的表名解决问题
    @Test
    void testSelectTableName() {
        //Table 'mybatis_plus.user' doesn't exist，证明使用实体类名查询
        User user = userMapper.selectById(1);
        System.out.println(user);
        /*
        Caused by: java.sql.SQLSyntaxErrorException: Table 'mybatis_plus.user' doesn't exist
        at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:120)
        at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122)
        at com.mysql.cj.jdbc.ClientPreparedStatement.executeInternal(ClientPreparedStatement.java:916)
        at com.mysql.cj.jdbc.ClientPreparedStatement.execute(ClientPreparedStatement.java:354)
        at com.zaxxer.hikari.pool.ProxyPreparedStatement.execute(ProxyPreparedStatement.java:44)
        at com.zaxxer.hikari.pool.HikariProxyPreparedStatement.execute(HikariProxyPreparedStatement.java)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at org.apache.ibatis.logging.jdbc.PreparedStatementLogger.invoke(PreparedStatementLogger.java:59)
        at com.sun.proxy.$Proxy87.execute(Unknown Source)
        at org.apache.ibatis.executor.statement.PreparedStatementHandler.query(PreparedStatementHandler.java:64)
        at org.apache.ibatis.executor.statement.RoutingStatementHandler.query(RoutingStatementHandler.java:79)
        at org.apache.ibatis.executor.SimpleExecutor.doQuery(SimpleExecutor.java:63)
        at org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:325)
        at org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156)
        at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:109)
        at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:89)
        at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:151)
        at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:145)
        at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:140)
        at org.apache.ibatis.session.defaults.DefaultSqlSession.selectOne(DefaultSqlSession.java:76)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:427)
        ... 87 more
         */
    }


}
