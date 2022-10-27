package com.lijianhua.mybatisplus;

import com.lijianhua.mybatisplus.domain.User;
import com.lijianhua.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

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
